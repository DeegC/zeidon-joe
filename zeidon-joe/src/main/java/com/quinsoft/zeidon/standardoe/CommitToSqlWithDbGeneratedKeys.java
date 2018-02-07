/**
    This file is part of the Zeidon Java Object Engine (Zeidon JOE).

    Zeidon JOE is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Zeidon JOE is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Zeidon JOE.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009-2015 QuinSoft
 */
package com.quinsoft.zeidon.standardoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import com.quinsoft.zeidon.CommitOptions;
import com.quinsoft.zeidon.Committer;
import com.quinsoft.zeidon.EntityInstance;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.View;
import com.quinsoft.zeidon.ZeidonException;
import com.quinsoft.zeidon.dbhandler.DbHandler;
import com.quinsoft.zeidon.dbhandler.JdbcHandlerUtils;
import com.quinsoft.zeidon.objectdefinition.AttributeDef;
import com.quinsoft.zeidon.objectdefinition.DataRecord;
import com.quinsoft.zeidon.objectdefinition.EntityDef;
import com.quinsoft.zeidon.objectdefinition.RelField;
import com.quinsoft.zeidon.objectdefinition.RelRecord;

/**
 * Commits a list of OIs to a sql DB.  The keys will be generated by the DB.  Thus inserts are
 * done serially.
 *
 * @author dgc
 *
 */
class CommitToSqlWithDbGeneratedKeys implements Committer
{
    private List<ViewImpl>       viewList;
    private DbHandler            dbHandler;
    private CommitOptions        options;

    /**
     * Keeps track of the FK source instances for every EI.
     *
     *      key = EI that is to be committed to DB
     *      value = set of EIs that are the source of the key's FKs.
     */
    private Map<EntityInstanceImpl, Set<EntityInstanceImpl>> fkSourcesForEi;

    /* (non-Javadoc)
     * @see com.quinsoft.zeidon.standardoe.Committer#init(com.quinsoft.zeidon.standardoe.TaskImpl, java.util.List)
     */
    @Override
    public void init( Task task, List<? extends View> list, CommitOptions options )
    {
        this.viewList = new ArrayList<ViewImpl>();
        for ( View v : list )
            viewList.add( ((InternalView) v).getViewImpl() );

        // Grab the first view to use as a task qualifier and for getting a dbhandler.
        // TODO: We're using the first view in the list but maybe we should do something different?
        //       We'd like to some day support commits across multiple DBs.
        ViewImpl firstView = viewList.get( 0 );
        this.options = options;
        JdbcHandlerUtils helper = new JdbcHandlerUtils( this.options, firstView.getLodDef().getDatabase() );
        dbHandler = helper.getDbHandler();
    }

    @Override
    public List<? extends View> commit()
    {
        try
        {
            // If there are no views to commit, return.
            if ( viewList.isEmpty() )
                return viewList;

            dbHandler.setDbGenerateKeys( true );

            fkSourcesForEi = new HashMap<EntityInstanceImpl, Set<EntityInstanceImpl>>();

            // Reset flags needed for commit processing.
            for ( ViewImpl view : viewList )
            {
                if ( ! view.getObjectInstance().isUpdated() )
                    continue;

                view.getObjectInstance().dbhNeedsForeignKeys = false;
                view.getObjectInstance().dbhNeedsGenKeys = false;

                final RelFieldParser p = new RelFieldParser();
                for ( EntityInstanceImpl ei : view.getObjectInstance().getEntities() )
                {
                    ei.dbhCreated = false;
                    ei.dbhDeleted = false;
                    ei.dbhExcluded = false;
                    ei.dbhIncluded = false;
                    ei.dbhUpdated = false;
                    ei.dbhNeedsInclude = false;
                    ei.dbhSeqUpdated = false;
                    ei.dbhGenKeyNeeded = false;
                    ei.dbhNoGenKey = false;
                    ei.dbhForeignKey = false;

                    // Keep track of the FK sources for this EI.
                    final EntityDef entityDef = ei.getEntityDef();
                    final DataRecord dataRecord = entityDef.getDataRecord();
                    if ( dataRecord != null )
                    {
                        final RelRecord relRecord = dataRecord.getRelRecord();
                        if ( relRecord != null )
                        {
                            for ( final RelField relField : relRecord.getRelFields() )
                            {
                                // If there is no rel data field then there's nothing to set.
                                if ( relField.getRelDataField() == null )
                                    continue;

                                p.parse( relField, ei );
                                if ( ! fkSourcesForEi.containsKey( p.relInstance ) )
                                    fkSourcesForEi.put( p.relInstance, new HashSet<EntityInstanceImpl>() );

                                // Add the srcInstance to the set of FK sources for relInstance.
                                fkSourcesForEi.get( p.relInstance ).add( p.srcInstance );
                            }
                        }
                    }
                }
            }

            /**
             * Determines if we should commit or rollback the current transaction.
             */
            boolean commit = false;

            dbHandler.beginTransaction( null );
            try
            {
                // First do all the inserts.
                performCreates();

                // Now do the rest of the updates by view.
                for ( ViewImpl view : viewList )
                {
                    if ( ! view.getObjectInstance().isUpdated() )
                        continue;

                    commitView( view.newInternalView() );
                }
                commit = true;
            }
            catch ( Exception e )
            {
                ZeidonException ze = ZeidonException.wrapException( e );
                throw ze;
            }
            finally
            {
                dbHandler.endTransaction( commit );
            }

            // If we get here then everything worked!  Turn off update flags for all the
            // entities/attributes and remove deleted/excluded entities from the OI chain.
            for ( ViewImpl view : viewList )
                cleanupOI( view.getObjectInstance() );

            return viewList;
        }
        finally
        {
        }
    }

    /**
     * Called after a commit. Reset the entity/attribute update flags to false,
     * remove deleted entities from the OI.
     * @param oi
     */
    private void cleanupOI(ObjectInstance oi)
    {
        CommitHelper.cleanupOI( oi );
    }

    private void commitView(ViewImpl view)
    {
        // Set up view to allow the DBhandler to access hidden entities.
        view.setAllowHiddenEntities( true );

        ObjectInstance oi = view.getObjectInstance();

        // TODO: implement optimistic locking check.

        EntityInstanceImpl lastEntityInstance = oi.getLastEntityInstance();

        commitExcludes( view, oi, lastEntityInstance );
        commitDeletes( view, oi, lastEntityInstance );
        commitIncludes( view, oi );
        commitUpdates( view, oi );
    }

    private void commitExcludes(View view, ObjectInstance oi, EntityInstanceImpl lastEntityInstance)
    {
        for ( EntityInstanceImpl ei = lastEntityInstance;
              ei != null;
              ei = ei.getPrevHier() )
        {
            EntityDef entityDef = ei.getEntityDef();
            DataRecord dataRecord = entityDef.getDataRecord();
            if ( dataRecord == null )
                continue;

            // EIs down a derived path don't get committed to the database.
            // Since all children of a derived EI are also derived we can skip
            // the twins of the current EI.
            if ( entityDef.isDerivedPath() )
            {
                while ( ei.getPrevTwin() != null )
                    ei = ei.getPrevTwin();

                continue;
            }

            // Skip the entity if we don't allow excludes.
            if ( ! entityDef.isExclude() )
                continue;

            // Can't exclude an entity that wasn't excluded...
            if ( !ei.isExcluded() )
                continue;

            // Skip it if the entity was already excluded via a linked instance.
            if ( ei.dbhExcluded )
                continue;

            // If the relationship is many-to-one then the FK is stored in the
            // parent EI.  Since we are excluding then set the FK in the parent
            // to null.
            RelRecord relRecord = dataRecord.getRelRecord();
            if ( relRecord.getRelationshipType().isManyToOne() )
            {
                assert relRecord.getRelFields().size() == 1;

                RelField relField = relRecord.getRelFields().get( 0 );
                RelFieldParser parser = new RelFieldParser( relField, ei );

                // Don't bother setting the FK if the min cardinality is > 0.
                // If we get here then we've passed the cardinality check so we
                // must be including a different entity which will update the FK.
                if ( entityDef.getMinCardinality() == 0 && ! parser.relInstance.isDeleted() )
                    parser.relInstance.getAttribute( parser.relAttributeDef ).setInternalValue( null, true );
            }
            else
                setForeignKeys( ei );

            view.cursor( entityDef ).setCursor( ei );
            dbHandler.deleteRelationship( view, ei );
            markDuplicateRelationships( ei );
            ei.dbhExcluded = true;
        }
    }

    private void commitDeletes(ViewImpl view, ObjectInstance oi, EntityInstanceImpl lastEntityInstance)
    {
        for ( EntityInstanceImpl ei = lastEntityInstance;
              ei != null;
              ei = ei.getPrevHier() )
        {
            EntityDef entityDef = ei.getEntityDef();
            DataRecord dataRecord = entityDef.getDataRecord();
            if ( dataRecord == null )
                continue;

            // EIs down a derived path don't get committed to the database.
            // Since all children of a derived EI are also derived we can skip
            // the twins of the current EI.
            if ( entityDef.isDerivedPath() )
            {
                while ( ei.getPrevTwin() != null )
                    ei = ei.getPrevTwin();

                continue;
            }

            // Skip the entity if we don't allow deletes.
            if ( ! entityDef.isDelete() )
                continue;

            // Can't exclude an entity that wasn't deleted...
            if ( !ei.isDeleted() )
                continue;

            // If the EI was also created then there's nothing to persist.
            if ( ei.isCreated() )
                continue;

            // Skip it if the entity was already deleted via a linked instance.
            if ( ei.dbhDeleted )
                continue;

            // TODO: add code to delete all children?

            view.cursor( entityDef ).setCursor( ei );
            dbHandler.deleteEntity( view, ei );

            // Flag all linked entities (including 'ei') as having been deleted.
            for ( EntityInstanceImpl linked : ei.getAllLinkedInstances() )
                linked.dbhDeleted = true;
        }
    }

    /**
     * Determines if ei needs to be created.
     *
     * @param ei
     * @return
     */
    private boolean requiresCreate( EntityInstanceImpl ei )
    {
        // Can't create an entity that wasn't created...
        if ( !ei.isCreated() )
            return false;

        // Skip deleted entities; they've been created then deleted so no need to save them.
        if ( ei.isDeleted() )
            return false;

        // Check to see if this EI has already been inserted into the DB.
        if ( ei.dbhCreated )
            return false;

        EntityDef entityDef = ei.getEntityDef();
        if ( entityDef.isDerivedPath() )
            return false;

        // Skip the entity if we don't allow creates.
        if ( ! entityDef.isCreate() )
            return false;

        DataRecord dataRecord = entityDef.getDataRecord();
        if ( dataRecord == null )
            return false;

        return true;
    }

    private void commitIncludes(ViewImpl view, ObjectInstance oi)
    {
        for ( EntityInstanceImpl ei : oi.getEntities() )
        {
            // Donn't include an entity that wasn't included...
            if ( ! ei.isIncluded() && ! ei.dbhNeedsInclude )
                continue;

            // Skip it if the entity was already included via a linked instance.
            if ( ei.dbhIncluded )
                continue;

            ei.dbhIncluded = true;

            // Nothing to include if this is the root.
            if ( ei.getParent() == null )
                continue;

            EntityDef entityDef = ei.getEntityDef();
            DataRecord dataRecord = entityDef.getDataRecord();
            if ( dataRecord == null )
                continue;

            if ( entityDef.isDerivedPath() )
                continue;

            RelRecord relRecord = entityDef.getDataRecord().getRelRecord();

            // Skip the entity if we don't allow includes unless this is a many-to-many
            // relationship.  Those need to have their correspondance table updated.
            if ( ! entityDef.isInclude() && relRecord.getRelationshipType() != RelRecord.MANY_TO_MANY)
                continue;

            // Since all new entities have been created by now there should be no problems
            // setting FKs.
            if ( ! setForeignKeys( ei ) )
                throw new ZeidonException( "Internal error: FKs for include were not set.  This should never happen" );

            // setForeignKeys only copies keys for created instances.  Do another copy
            // to set keys for instances that were included only.
            if ( relRecord.getRelationshipType() != RelRecord.MANY_TO_MANY )
                copyFksToParents( ei, dataRecord );
            else
            {
                view.cursor( entityDef ).setCursor( ei );
                dbHandler.insertRelationship( view, ei );
            }
            markDuplicateRelationships( ei );
        }
    }

    /**
     * EI has been included.  Copy any FKs that involve ei.
     * Note: This may re-copy some FKs that have already been copied.
     *
     * @param ei
     */
    private void copyFksToParents( EntityInstanceImpl ei, DataRecord dataRecord )
    {
        RelRecord relRecord = dataRecord.getRelRecord();
        for ( RelField relField : relRecord.getRelFields() )
        {
            final RelFieldParser p = new RelFieldParser();
            p.parse( relField, ei );
            p.copySrcToRel();
        }
    }

    /**
     * EI has been included.  Set any FKs that involve ei to null
     * Note: This may reset some FKs that have already been set.
     *
     * @param ei
     */
    private void excludeFksFromParents( EntityInstanceImpl ei, DataRecord dataRecord )
    {
        RelRecord relRecord = dataRecord.getRelRecord();
        for ( RelField relField : relRecord.getRelFields() )
        {
            final RelFieldParser p = new RelFieldParser();
            p.parse( relField, ei );
            p.relInstance.getAttribute( p.relAttributeDef ).setInternalValue( null, true );
        }
    }

    private void commitUpdates(View view, ObjectInstance oi)
    {
        for ( EntityInstanceImpl ei : oi.getEntities() )
        {
            EntityDef entityDef = ei.getEntityDef();
            DataRecord dataRecord = entityDef.getDataRecord();
            if ( dataRecord == null )
                continue;

            if ( entityDef.isDerivedPath() )
                continue;

            // Skip the entity if we don't allow updates
            if ( ! entityDef.isUpdate() )
            {
                // We might need an update because the entity is included/excluded.
                if ( ! ei.isIncluded() && ! ei.isExcluded() )
                    continue;
            }

            // Can't update an entity that wasn't updated...
            if ( !ei.isUpdated() )
                continue;

            // Skip it if the entity was already updated via a linked instance.
            if ( ei.dbhUpdated )
                continue;

            // Skip if the entity was created or deleted.
            if ( ei.dbhCreated || ei.dbhDeleted )
                continue;

            if ( entityDef.getDbUpdatedTimestamp() != null )
                ei.getAttribute( entityDef.getDbUpdatedTimestamp() ).setValue( new DateTime() );

            view.cursor( entityDef ).setCursor( ei );
            dbHandler.updateEntity( view, ei );

            // Flag all linked entities (including 'ei') as having been updated.
            for ( EntityInstanceImpl linked : ei.getAllLinkedInstances() )
                linked.dbhUpdated = true;
        }
    }

    /**
     // This function is called after an EI has been included into the DB.  This
     // function sets the bDBHIncluded/bDBHExcluded flag for all linked EIs in the
     // same OI that have the same relationship.
     */
    private void markDuplicateRelationships(EntityInstanceImpl ei)
    {
        EntityInstanceImpl parent = ei.getParent();
        EntityDef         entityDef = ei.getEntityDef();

        // Duplicate relationship searching phase I, see if a linked instance to
        // the target instance in the same object instance represents the
        // same relationship type AND has the same parent.
        for ( EntityInstanceImpl linked : ei.getLinkedInstances() )
        {
            // Check to make sure linked EI has a parent--it is possible for a root
            // to be flagged as included and we don't care about roots.
            if ( linked.isDeleted() || linked.getParent() == null )
                continue;

            if ( ei.isExcluded() )
            {
                if ( ! linked.isExcluded() )
                    continue;
            }
            else
            {
                if ( ! linked.isIncluded() )
                    continue;
            }

            EntityDef linkedEntityDef = linked.getEntityDef();

            // Linked EI must have the same relationship and it can't be derived.
            if ( linkedEntityDef.getErRelToken() == entityDef.getErRelToken() ||
                 linkedEntityDef.isDerivedPath() )
            {
                continue;
            }

            // Now check to see if the parents are linked.
            EntityInstanceImpl linkParent = linked.getParent();
            for ( EntityInstanceImpl parentLinked : linkParent.getLinkedInstances() )
            {
                if ( parentLinked == parent )
                {
                    if ( ei.isExcluded() )
                        linked.dbhExcluded = true;
                    else
                        linked.dbhIncluded = true;

                    break;
                }
            }
        } // for each linked instance...

        // Duplicate relationship searching, phase II, see if the parent of
        // the instance has a linked instance representing the same relationship
        // type which is also a child of one of the targets linked instances.

        // If the parent isn't linked then there are no duplicate relationships.
        if ( parent.getLinkedInstances().size() == 0 )
            return;

        for ( EntityInstanceImpl linked : parent.getLinkedInstances() )
        {
            // Check for appropriate include/exclude flag.
            if ( ei.isExcluded() )
            {
                if ( ! linked.isExcluded() )
                    continue;
            }
            else
            {
                if ( ! linked.isIncluded() )
                    continue;
            }

            EntityDef linkedEntityDef = linked.getEntityDef();

            // Check to see if the relationship for the EI linked to the parent is
            // the same as the relationship of the original EI.
            if ( linkedEntityDef.getErRelToken() != entityDef.getErRelToken() )
                continue; // Nope.

            // OK, we have an EI ('linked') that has the same relationship as
            // ei.  Check to see if the parent of 'linked' (grandParent)
            // is linked with ei.  If they are linked then 'linked'
            // has the same physical relationship as ei.
            EntityInstanceImpl grandParent = linked.getParent();
            for ( EntityInstanceImpl gp : ei.getLinkedInstances() )
            {
                if ( gp == grandParent )
                {
                    if ( ei.isExcluded() )
                        linked.dbhExcluded = true;
                    else
                        linked.dbhIncluded = true;

                    break;
                }
            }
        } // for each linked instance of parent...
    } // markDuplicateRelationships()

    /**
     * Create each of the entities in all the OIs.
     */
    private void performCreates()
    {
        // Create entities.  We have to loop possibly many times to create the entities
        // because we have to set the FKs.  It's
        // possible that the source for a FK is a FK from yet another EI.  We don't
        // want to copy a FK until we know that the source for a FK has been
        // properly set.  We also want to make sure we set the FK's for the EIs that
        // have been excluded/deleted before we copy FKs for the included/created.
        int     debugCnt   = 0;     // We'll keep a counter in case we get an infinite loop.

        // creatingEntities will be true for as long as we think we need to create more entities.
        boolean creatingEntities = true;
        while ( creatingEntities )
        {
            // We'll hope that we're done creating entities after this iteration.  If we
            // find we need to create more then we'll turn it back on.
            creatingEntities = false;

            if ( debugCnt++ > 100 )
                throw new ZeidonException("Internal error: too many times creating entities.");

            for ( ViewImpl view : viewList )
            {
                ObjectInstance oi = view.getObjectInstance();

                for ( final EntityInstanceImpl ei : oi.getEntities() )
                {
                    if ( ! requiresCreate( ei ) )
                        continue;

                    assert ! ei.getEntityDef().isDerivedPath();

                    if ( ! createInstance( view, ei ) )
                        creatingEntities = true;  // We weren't able to create ei.  Try again later.
                }
            }
        } // while creatingEntities...
    }

    /**
     * This will attempt to set all the FKs for ei.  We've created the map fkSourcesForEi
     * which keeps track of all the EIs that are the source for ei's foreign keys.
     * We'll loops through all the source instances for ei and attempt to copy the
     * FK from srcInstance to ei.
     *
     * However, if the srcInstance hasn't been created in the DB it won't have its
     * key set yet.  We'll have to come back after srcInstance is created to set
     * the FKs for ei.
     *
     * @param ei
     * @return true of all FKs were set, false if there are still some FKs that need to be set.
     */
    private boolean setForeignKeys( EntityInstanceImpl ei )
    {
        final EntityDef entityDef = ei.getEntityDef();
        final Set<EntityInstanceImpl> srcInstances = fkSourcesForEi.get( ei );
        if ( srcInstances == null || srcInstances.size() == 0 )
            return true;  // No FKs to set.

        // Loop through all the source instances for ei's FKs.
        // Use an iterator because we may need to remove the srcInstance.
        for ( Iterator<EntityInstanceImpl> iter = srcInstances.iterator(); iter.hasNext(); )
        {
            EntityInstanceImpl srcInstance = iter.next();

            // If the source instance hasn't had been created yet then we need to wait
            // and try again later.
            if ( requiresCreate( srcInstance ) )
                continue;

            // The RelRecord that defines the relationship between ei and srcInstance
            // will be in the entity that is a child (or descendant) of the parent.  Find which
            // entity is below the other.
            DataRecord dataRecord;
            EntityInstanceImpl childInstance;
            if ( srcInstance.getDepth() < ei.getDepth() )
            {
                // ei is under srcInstance
                dataRecord = entityDef.getDataRecord();
                childInstance = ei;
            }
            else
            {
                // srcInstance is under ei.
                dataRecord = srcInstance.getEntityDef().getDataRecord();
                childInstance = srcInstance;
            }

            final RelRecord relRecord = dataRecord.getRelRecord();
            final RelFieldParser p = new RelFieldParser();

            // Loop through the relationships for childInstance and find the one(s)
            // for srcInstance-ei
            for ( final RelField relField : relRecord.getRelFields() )
            {
                p.parse( relField, childInstance );

                // We only want the relField that copies a FK from srcInstance to ei.
                if ( p.srcInstance != srcInstance )
                    continue;

                assert p.relInstance == ei;

                if ( ei.isCreated() || ei.isIncluded() )
                {
                    p.copySrcToRel();

                    // We've copied the FK to ei.  Remove srcInstance from the list of required sources.
                    iter.remove();
                }
                else
                {
                    // If the foreign key is a key to the target entity, then
                    // we cannot null the key because we would lose the
                    // capability of updating the entity (in this case it
                    // better be deleted!!!)
                    assert ! p.relAttributeDef.isKey();
                    assert ei.isExcluded(); // EI is hidden and we've ignored deleted EIs above.

                    // If the EI is excluded and the min cardinality is 1, we'll get an error
                    // if we set the FK to null.  We will assume that a different entity is being
                    // included which will set the FK to a non-null value.  We can assume this because
                    // the OI has passed cardinality validation and if no EI was being included it
                    // would have thrown a validation exception.
                    if ( entityDef.getMinCardinality() == 0)
                        p.relInstance.getAttribute( p.relAttributeDef ).setInternalValue( null, true );
                }

                // Turn off the dbh flag to make sure that the DBHandler updates
                // the instance.
                ei.dbhUpdated = false;

            } // for each rel field...
        }

        // If fkSourcesForEi contains an entry for ei then we are done setting FKs only
        // if there are no more source instances for ei.
        if ( fkSourcesForEi.containsKey( ei ) )
            return fkSourcesForEi.get( ei ).size() == 0;

        return true;
    }

    /**
     * Attempts to insert ei into the DB.  It will fail (and return false) if ei relies
     * on a FK from an EI that hasn't been created yet.
     *
     * Returns true  if the EI was created in the DB
     *         false if the EI couldn't be created because it relies on uncreated FKs.
     *
     **/
    private boolean createInstance( View view, final EntityInstanceImpl ei )
    {
        final EntityDef entityDef = ei.getEntityDef();

        // Try to set the FKs for this instance.  It will fail (return false) if one the source EI
        // for one of the FKs hasn't been created yet.  This means it doesn't have a key.
        if ( ! setForeignKeys( ei ) )
            return false;

        // If we get here than all the FK's were copied successfully and we can create this instance.
        // We'll retrieve the generated key from the DB after the row has been inserted.
        try
        {
            if ( entityDef.getDbCreatedTimestamp() != null )
            {
                AttributeInstanceImpl timestamp = ei.getAttribute( entityDef.getDbCreatedTimestamp() );
                if ( ! timestamp.isNull() )
                {
                    DateTime now = new DateTime();
                    timestamp.setValue( now );

                    if ( entityDef.getDbUpdatedTimestamp() != null )
                        ei.getAttribute( entityDef.getDbUpdatedTimestamp() ).setValue( now );
                }
            }

            // We need to handle multiple entities being created.
            List<EntityInstance> list = new ArrayList<EntityInstance>();
            list.add( ei );
            dbHandler.insertEntity( view, list );

            AttributeDef genkey = entityDef.getGenKey();
            if ( genkey != null )
            {
                List<Object> keys = dbHandler.getKeysGeneratedByDb();
                if ( keys.size() != 1 )
                    throw new ZeidonException("Unexpected number of keys found: %s", keys );

                AttributeDef keyAttrib = entityDef.getKeys().get( 0 );
                ei.getAttribute( keyAttrib ).setValue( keys.get( 0 ) );
            }

            // Set the dbhCreated flag for ei and all its linked instances.  This
            // will prevent us from trying to insert it again.
            for ( EntityInstanceImpl linked : ei.getAllLinkedInstances() )
            {
                linked.dbhCreated = true;

                // If the linked instance is flagged as created then we need
                // to set its included flag on so that the *relationship*
                // is still created.
                if ( linked.isCreated() )
                    linked.dbhNeedsInclude = true;
            }
        }
        catch ( Exception e )
        {
            throw ZeidonException.wrapException( e ).prependEntityInstance( ei );
        }

        return true;
    }
}
