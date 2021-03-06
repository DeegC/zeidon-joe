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

import java.util.EnumSet;

import com.quinsoft.zeidon.ActivateFlags;
import com.quinsoft.zeidon.ActivateOptions;
import com.quinsoft.zeidon.Activator;
import com.quinsoft.zeidon.Pagination;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.View;
import com.quinsoft.zeidon.ZeidonException;
import com.quinsoft.zeidon.dbhandler.DbHandler;
import com.quinsoft.zeidon.dbhandler.JdbcHandlerUtils;
import com.quinsoft.zeidon.dbhandler.PessimisticLockingHandler;
import com.quinsoft.zeidon.objectdefinition.AttributeDef;
import com.quinsoft.zeidon.objectdefinition.EntityDef;
import com.quinsoft.zeidon.objectdefinition.LodDef;
import com.quinsoft.zeidon.utils.Timer;

/**
 * @author DG
 *
 */
class ActivateOiFromDB implements Activator
{
    private TaskImpl  task;
    private ViewImpl  view;
    private View      qual;
    private EnumSet<ActivateFlags> control;
    private LodDef      lodDef;
    private DbHandler   dbHandler;
    private ActivateOptions options;

    private PessimisticLockingHandler pessimisticLock;

    @Override
    public View init(Task task, View view, ActivateOptions options )
    {
        assert options != null;

        this.task = (TaskImpl) task;

        // View will be non-null when we're performing a lazy-load.
        if ( view == null )
            this.view = (ViewImpl) task.activateEmptyObjectInstance( options.getLodDef() );
        else
            this.view = ((InternalView) view).getViewImpl();

        this.qual = options.getQualificationObject();
        this.options = options;
        control = options.getActivateFlags();
        lodDef = options.getLodDef();
        if ( ! lodDef.hasPhysicalMappings() )
            throw new ZeidonException("Attempting to activate OI with no physical mappings.  LOD = %s", lodDef.getName() );

        JdbcHandlerUtils helper = new JdbcHandlerUtils( options, lodDef.getDatabase() );
        dbHandler = helper.getDbHandler();
        dbHandler.beginActivate( this.view, qual, control );
        return this.view;
    }

    /**
     * Activate the OI.  This gets called for the initial activate of the OI.
     * This will not be called for lazy-loads.
     *
     * @return
     */
    @Override
    public ViewImpl activate()
    {
        Timer timer = new Timer();
        ObjectInstance oi = view.getObjectInstance();

        // Store the activate options for later use.
        oi.setActivateOptions( options );

        // Get pessimistic lock handler.
        pessimisticLock = dbHandler.getPessimisticLockingHandler( options, view );

        // If we are activating with rolling pagination then replace the root cursor
        // with a special one that will attempt to load the next page when required.
        Pagination pagingOptions = options.getPagingOptions();
        if ( pagingOptions != null && pagingOptions.isRollingPagination() )
        {
            ViewCursor viewCursor = view.getViewCursor();
            EntityCursorImpl newCursor = new RollingPaginationEntityCursorImpl( viewCursor, lodDef.getRoot(), options );
            viewCursor.replaceEntityCursor( newCursor );
        }

        try
        {
            pessimisticLock.acquireGlobalLock( view );

            EntityDef rootEntity = lodDef.getRoot();
            activate( rootEntity );

            if ( oi.getRootEntityInstance() != null ) // Did we load anything?
    		{
                boolean isLocked = pessimisticLock.acquireOiLocks( view );

                view.reset();
                if ( options.isReadOnly() )
                    view.getObjectInstance().setReadOnly( true );
                else
                    view.getObjectInstance().setLocked( isLocked );

                view.getLodDef().executeActivateConstraint( view );
    		}

            task.getObjectEngine().getOeEventListener().objectInstanceActivated( view, qual, timer.getMilliTime(), null );

            return view;
        }
        finally
        {
            pessimisticLock.releaseGlobalLock( view );
        }
    }

    /**
     * Activate a subobject with subobjectRootEntity as the root.  If subobjectRootootEntity = LodDef.root then
     * this activates the whole OI.
     *
     * @param subobjectRootEntity
     * @return
     */
    @Override
    public int activate( EntityDef subobjectRootEntity )
    {
        Timer timer = new Timer();
        int rc = 0;
        boolean transactionStarted = false;
        boolean commit = false;
        String viewName = "__Load in progress " + lodDef.getName();
        ObjectInstance oi = view.getObjectInstance();
        try
        {
            // Set flag to tell cursor processing to not bother with checking for lazy-loaded
            // entities.  Since we're activating we know we don't want to load lazy entities
            // via cursor access.
            oi.setIgnoreLazyLoadEntities( true );

            task.log().debug( "Activating %s from DB", lodDef.getName() );
            view.setName( viewName );

            dbHandler.beginTransaction(view);
            transactionStarted = true;

            rc = singleActivate( subobjectRootEntity );

            commit = true;
        }
        catch ( Exception e )
        {
            ZeidonException ze = ZeidonException.wrapException( e ).prependLodDef( view.getLodDef() );
            if ( task.log().isDebugEnabled() )
                ze.appendMessage( "XOD: %s", this.lodDef.getFileName() );
            task.getObjectEngine().getOeEventListener().objectInstanceActivated( view, qual, timer.getMilliTime(), ze );
            throw ze;
        }
        finally
        {
            if ( transactionStarted )
                dbHandler.endTransaction( commit );

            oi.setIgnoreLazyLoadEntities( false );
            view.dropNameForView( viewName );

            if ( timer.getMilliTime() > 2000 )
                task.log().info( "==> Activate for %s took %s seconds", lodDef, timer );
            else
                task.log().info( "==> Activate for %s took %d milliseconds", lodDef, timer.getMilliTime() );
        }

        return rc;
    }

    /**
     * Activate all the child entities for all the twins in parentCursor.
     * @param parentCursor
     */
    private void activateChildren( EntityCursorImpl parentCursor )
    {
        if ( ! parentCursor.setFirst().isSet() )
            return;

        EntityDef parentEntityDef = parentCursor.getEntityDef();

        /*
         * Is parent recursive?  If so, then we have a situation like this:
         *  A
         *  |
         *  B
         * Where B and A are the same ER entity and parentCursor points to B.
         * We need to iterate through each of the B's and set the recursive
         * structure so that B because the subobject root.
         */
        boolean recursive = parentEntityDef.isRecursive();
        if ( recursive )
        {
            // Reset parentEntityDef to point to A from the example above.
            parentEntityDef = parentEntityDef.getRecursiveParent();
        }

        // We can't use setNextContinue() because the recursiveness throws off the cursor.
        // We don't want to use setFirst()/setNext() because it has performance issues, so we'll
        // go through the twins manually and set the parentCursor.
        for ( EntityInstanceImpl parentEi = parentCursor.getEntityInstance();
                                 parentEi != null;
                                 parentEi = parentEi.getNextTwin() )
        {
            parentCursor.setCursor( parentEi );

            if ( recursive )
                parentCursor.setToSubobject(); // B will now become accessed via entity A.

            for ( EntityDef childEntityDef : parentEntityDef.getChildren() )
            {
                if ( childEntityDef.isDerived() || childEntityDef.isDerivedPath() )
                    continue;

                if ( childEntityDef.getDataRecord() == null )
                    continue;

                if ( isLazyLoad( childEntityDef ) )
                {
                    view.dblog().trace( "Entity %s is flagged as LazyLoad. Skipping activate", childEntityDef.getName() );
                    continue;
                }

                // If the child entity has qualification then we won't have all of the children
                // loaded.  Call setIncomplete to prevent this entity from being deleted.
                if ( dbHandler.isQualified( childEntityDef ) )
                    parentEi.setIncomplete( childEntityDef );

                int load = dbHandler.loadEntity( view, childEntityDef );
                if ( load == DbHandler.LOAD_DONE )
                    activateChildren( view.cursor( childEntityDef ) );
            }

            if ( recursive )
                parentCursor.resetSubobjectToParent();
        }
    }

    /**
     * returns true if entityDef should be loaded lazily.
     *
     * @param entityDef
     * @return
     */
    private boolean isLazyLoad( EntityDef entityDef )
    {
        return entityDef.getLazyLoadConfig().isLazyLoad() && ! control.contains( ActivateFlags.fINCLUDE_LAZYLOAD );
    }

    /**
     * Assert that there are no incremental flags still set.  Called after a commit.
     *
     * @param ei
     * @return
     */
    private boolean assertFlagsAreOff( EntityInstanceImpl ei )
    {
        for ( AttributeDef attributeDef : ei.getNonNullAttributeList() )
        {
            AttributeValue attrib = ei.getInternalAttribute( attributeDef );
            if ( attrib.isUpdated() )
            {
                task.log().error( "Assert: Attribute %s %s is flagged as updated", attributeDef, attrib );
                return false;
            }
        }

        return true;
    }

    /**
     * Activate a single object instance or subobject, starting with rootEntityDef.
     * @return
     */
    private int singleActivate( EntityDef rootEntityDef )
    {
        // Are we loading the OI root?  If so we're loading the whole OI.
        boolean isOiRoot = rootEntityDef.getParent() == null;

        assert isOiRoot || rootEntityDef.getLazyLoadConfig().isLazyLoad() : "We're loading an entity that is neither root or lazyload";

        ObjectInstance oi = view.getObjectInstance();

        // We'll be setting the update flag for the OI after we finish the activate.
        // If we're loading the OI then we'll be setting them to false.  If we're
        // doing a lazy load then we need to reset the OI flags to what they are
        // currently.
        boolean isUpdated = false;
        boolean isUpdatedFile = false;
        if ( ! isOiRoot )
        {
            // Save the OI flags for later.
            isUpdated = oi.isUpdated();
            isUpdatedFile = oi.isUpdatedFile();
        }

        int rc = dbHandler.loadEntity( view, rootEntityDef );

        if ( isOiRoot )
            pessimisticLock.acquireRootLocks( view );

        // Activate the child entities unless we're activating the root and ROOT_ONLY is set.
        if ( isOiRoot && control.contains( ActivateFlags.fROOT_ONLY ) )
        {
            // We're only loading the roots.  Set the incomplete flag for each of the entities.
            for ( EntityInstanceImpl ei : oi.getEntities() )
            {
                assert ei.getParent() == null;
                ei.setIncomplete( null );
            }
        }
        else
            activateChildren( view.cursor( rootEntityDef ) );

        if ( rc == 0 ) // Did we load anything?
        {
            // Loop through the newly loaded EIs and reset their flags.
            if ( isOiRoot )
            {
                for ( EntityInstanceImpl ei : oi.getEntities() )
                {
                    assert assertFlagsAreOff( ei ) : "Error in attrib flags.  See log for more.";
                    assert ei.dbhLoaded == true;
                    ei.setCreated( false );
                    ei.setUpdated( false );
                    ei.setIncluded( false );
                    ei.dbhLoaded = false;
                }
            }
            else
            {
                // If we get here then we should be lazy-loading the EI.
                assert isLazyLoad( rootEntityDef );

                EntityInstanceImpl loadedRoot = view.cursor( rootEntityDef.getParent() ).getEntityInstance();
                for ( EntityInstanceImpl ei : loadedRoot.getChildrenHier( true, false, false ) )
                {
                    if ( ! ei.dbhLoaded )
                        continue;

                    assert ! ei.isHidden() : "We have a newly loaded EI that is hidden.";
                    assert assertFlagsAreOff( ei ) : "Error in attrib flags.  See log for more.";
                    ei.setCreated( false );
                    ei.setUpdated( false );
                    ei.dbhLoaded = false;
                }
            }

            oi.setUpdated( isUpdated );
            oi.setUpdatedFile( isUpdatedFile );
        }

        return rc;
    }

    @Override
    public void dropOutstandingLocks()
    {
        pessimisticLock = dbHandler.getPessimisticLockingHandler( options, view );
        pessimisticLock.dropOutstandingLocks();
    }
}
