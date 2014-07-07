/**
 *
 */
package com.quinsoft.zeidon.dbhandler;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.MapMaker;
import com.quinsoft.zeidon.AbstractOptionsConfiguration;
import com.quinsoft.zeidon.ActivateFlags;
import com.quinsoft.zeidon.Application;
import com.quinsoft.zeidon.CreateEntityFlags;
import com.quinsoft.zeidon.CursorPosition;
import com.quinsoft.zeidon.EntityCursor;
import com.quinsoft.zeidon.EntityInstance;
import com.quinsoft.zeidon.ObjectEngine;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.View;
import com.quinsoft.zeidon.ZeidonException;
import com.quinsoft.zeidon.domains.Domain;
import com.quinsoft.zeidon.objectdefinition.DataField;
import com.quinsoft.zeidon.objectdefinition.DataRecord;
import com.quinsoft.zeidon.objectdefinition.ViewAttribute;
import com.quinsoft.zeidon.objectdefinition.ViewEntity;
import com.quinsoft.zeidon.utils.IntegerLinkedHashMap;

/**
 * A DB handler for JDBC.
 *
 * @author DG
 *
 */
public class JdbcHandler extends AbstractSqlHandler
{
    private static final EnumSet<CreateEntityFlags> CREATE_FLAGS = EnumSet.of( CreateEntityFlags.fNO_SPAWNING,
                                                                               CreateEntityFlags.fIGNORE_MAX_CARDINALITY,
                                                                               CreateEntityFlags.fDONT_UPDATE_OI,
                                                                               CreateEntityFlags.fDONT_INITIALIZE_ATTRIBUTES,
                                                                               CreateEntityFlags.fIGNORE_PERMISSIONS );

    private final Map<String, PreparedStatementCacheValue> cachedStatements;
    private       JdbcConnectionPool connectionPool;

    /**
     * The number of PreparedStatements loaded from the cache.
     */
    private final int     cachedStatementCount = 0;

    private Connection connection;

    /**
     * For inserts, this is the list of keys generated by the DB.
     */
    private ArrayList<Object> generatedKeys;

    private final String configGroupName;
    private final AbstractOptionsConfiguration options;
    private JdbcDomainTranslator translator;
    private String dateFormat;
    private String drivers;

    /**
     * @param task
     * @param application
     * @param view
     * @param config
     */
    public JdbcHandler(Task task, AbstractOptionsConfiguration options )
    {
        super( task, options );
        this.options = options;
        configGroupName = options.getConfigValue( "_JDBC", "JdbcConfigGroupName" );
        task.log().debug( "JDBC config group = %s", configGroupName );

        if ( isBindAllValues() )
            cachedStatements = new HashMap<String, PreparedStatementCacheValue>();
        else
            cachedStatements = null; // Indicate that we aren't caching prepared statements.
    }

    @Override
    protected String getConfigValue( String key )
    {
        return options.getConfigValue( configGroupName, key );
    }

    private Connection getConnection(Application application)
    {
        if ( connection != null )
            return connection;

        String url = options.getOiSourceUrl();
        connection = getConnectionPool().getConnection( url, task, this, application );
        if ( connection != null )
            return connection;

        throw new ZeidonException("Error creating connection to %s", url );
    }

    /**
     * This doesn't need to be synchronized because the use of cacheMap does it for us.
     *
     * @return
     */
    private JdbcConnectionPool getConnectionPool()
    {
        if ( connectionPool == null )
        {
            Task systemTask = getTask().getSystemTask();
            connectionPool = systemTask.getCacheMap( JdbcConnectionPool.class );
            if ( connectionPool == null )
            {
                connectionPool = new JdbcConnectionPool();
                connectionPool = systemTask.putCacheMap( JdbcConnectionPool.class, connectionPool );
            }
        }

        return connectionPool;
    }

    @Override
    protected void getSqlValue(SqlStatement stmt, Domain domain, ViewAttribute viewAttribute, StringBuilder buffer, Object value)
    {
        try
        {
            if ( getTranslator().appendSqlValue( stmt, buffer, domain, viewAttribute, value ) )
                return;

            throw new ZeidonException("JdbcDomainTranslator did not correctly translate an attribute value" );
        }
        catch ( Exception e )
        {
            throw ZeidonException.wrapException( e ).prependViewAttribute( viewAttribute ).appendMessage( "Value = %s", value );
        }
    }

    @Override
    public void beginTransaction()
    {
        connection = getConnection( application );
        task.dblog().debug( "JDBC: got a connection to %s", options.getOiSourceUrl() );
    }

    /* (non-Javadoc)
     * @see com.quinsoft.zeidon.dbhandler.DbHandler#endTransaction(java.lang.Object, com.quinsoft.zeidon.Task, com.quinsoft.zeidon.View, boolean)
     */
    @Override
    public void endTransaction(boolean commit)
    {
        try
        {
            // Close any statements that were cached.
            if ( cachedStatements != null )
            {
                for ( PreparedStatementCacheValue v : cachedStatements.values() )
                    DbUtils.closeQuietly( v.ps );
                task.dblog().trace( "Loaded %d statements from cache\nTotal cache size = %d",
                                    cachedStatementCount, cachedStatements.size() );
                cachedStatements.clear();
            }

            if ( commit )
                connection.commit();
            else
                connection.rollback();

            connection.close();
            task.dblog().debug( "JDBC: closed connection" );
            connection = null;
        }
        catch ( Throwable e )
        {
            throw ZeidonException.prependMessage( e, "JDBC = %s", options.getOiSourceUrl() );
        }
    }

    /**
     * Returns the concatenated key values from the current row in the ResultSet.
     * This retrieves *ALL* keys, including parent keys.
     *
     * @param viewEntity
     * @param rs
     * @return
     */
    private String getFullKeyValuesForCurrentRow( ViewEntity viewEntity, ResultSet rs, SqlStatement stmt, Map<Integer,Object> loadedObjects )
    {
        List<String> values = new ArrayList<String>();

        // Add the entity name so we can keep track of the keys for multiple entities.
        values.add( viewEntity.getName() );

        // Get the values for the keys.  If we're doing a join then we may need to add parent
        // keys as well.  This is necessary if a child entity instance is the child of two
        // different parents.
        for ( ViewEntity ve = viewEntity; ve != null; ve = ve.getParent() )
        {
            DataRecord dataRecord = ve.getDataRecord();
            assert dataRecord != null;

            // If ve is not in the dataRecords map then it's not part of this select statement.
            if ( ! stmt.dataRecords.containsKey( dataRecord ) )
            {
                // The first viewEntity should *always* be in the dataRecords so if it's not
                // throw an assertion error.
                assert ve != viewEntity : "viewEntity is not in dataRecords map";
                break;
            }

            List<ViewAttribute> keys = ve.getKeys();
            for ( ViewAttribute key : keys )
            {
                DataField dataField = dataRecord.getDataField( key );
                Integer columnIdx = stmt.getColumns().get( dataField );
                assert columnIdx != null;
                try
                {
                    Object value = getSqlObject( rs, columnIdx, loadedObjects );
                    String str = value.toString();
                    values.add( str );
                }
                catch ( SQLException e )
                {
                    throw ZeidonException.wrapException( e ).appendMessage( "DataField: %s, column idx: %d", dataField, columnIdx );
                }
            }
        }

        return StringUtils.join( values, "|" );
    }

    /**
     * Returns the concatenated key values from the current entity in the ResultSet.
     *
     * Internal note: We want this to produce the same output as EntityInstance.getKeyString()
     *
     * @param viewEntity
     * @param rs
     * @return
     */
    private String getKeyValuesForCurrentEntity( ViewEntity viewEntity, ResultSet rs, SqlStatement stmt, Map<Integer,Object> loadedObjects )
    {
        StringBuilder builder = new StringBuilder();

        DataRecord dataRecord = viewEntity.getDataRecord();
        assert dataRecord != null;

        List<ViewAttribute> keys = viewEntity.getKeys();
        for ( ViewAttribute key : keys )
        {
            DataField dataField = dataRecord.getDataField( key );
            Integer columnIdx = stmt.getColumns().get( dataField );
            assert columnIdx != null;
            try
            {
                Object value = getSqlObject( rs, columnIdx, loadedObjects );
                String str = value.toString();
                builder.append( str ).append( "|" );
            }
            catch ( SQLException e )
            {
                throw ZeidonException.wrapException( e ).appendMessage( "DataField: %s, column idx: %d", dataField, columnIdx );
            }
        }

        return builder.toString();
    }

    @Override
    protected void addActivateLimit( ViewEntity viewEntity, SqlStatement stmt )
    {
        // Default is to do nothing.  Why?  Limits are DB-specific and anything we put here
        // could cause a generate problem.  We'll spit out a warning.
        getTask().log().warn( "Activate Limits are not supported by the generic JDBC DB Handler.  " +
                              "Please specify a specific DBHandler in Zeidon config." );
    }

    @Override
    protected int executeLoad(View view, ViewEntity viewEntity, SqlStatement stmt)
    {
        int rc = 0;
        String sql = stmt.getAssembledCommand();
        logSql( stmt );
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            Map<String,EntityInstance> loadedEntities = new HashMap<String, EntityInstance>();
            IntegerLinkedHashMap<ViewEntity> entityCounts = new IntegerLinkedHashMap<ViewEntity>();
            ps = prepareAndBind( stmt, sql, view, viewEntity, stmt.commandType );
            rs = ps.executeQuery();

            while ( rs.next() )
            {
                // TODO: implement Activate Limit constraint.
                loadAttributes( stmt, rs, view, loadedEntities, entityCounts );

                // Check to see if we've loaded 2 root entities.  If so, then see if we're only
                // activating a single root.  We have to check after we've called loadAttributes
                // because the root entity could be repeated in the result set if it is joined
                // with children.
                if ( entityCounts.get( viewEntity ) == 2  )
                {
                    rc = 1;  // Set return code to indicate we found multiple roots.

                    // If we're loading the root and we're only supposed to load a single root
                    // then stop loading.
                    if ( viewEntity.getParent() == null && control.contains( ActivateFlags.fSINGLE ) )
                    {
                        // We've loaded 2 root entities.  Delete the second (current) one.
                        view.cursor( viewEntity ).dropEntity();
                        break;
                    }
                }
            }

            for ( ViewEntity ve : entityCounts.keySet() )
            {
                ViewEntity parentEntity = ve.getParent();
                if ( parentEntity != null )
                {
                    EntityCursor parentCursor = view.cursor( parentEntity );
                    task.dblog().debug( "Activated %d %s entities for %s", entityCounts.get( ve ), ve, parentCursor );
                }
                else
                    task.dblog().debug( "Activated %d %s entities", entityCounts.get( ve ), ve );
            }
        }
        catch ( Exception e )
        {
            throw ZeidonException.prependMessage( e, "SQL => %s\nDB: %s", sql, options.getOiSourceUrl() )
                                 .prependViewEntity( viewEntity )
                                 .prependDataRecord( viewEntity.getDataRecord() );
        }
        finally
        {
            close( rs, ps );
        }

        return rc;
    }

    private Object getSqlObject( ResultSet rs, Integer idx, Map<Integer,Object> loadedObjects ) throws SQLException
    {
        Object o = loadedObjects.get( idx );
        if ( o == null )
        {
            o = rs.getObject( idx );
            loadedObjects.put( idx, o );
        }

        return o;
    }

    /**
     * Sets the attribute using the value retrieved from the DB.
     *
     * @param entityInstance
     * @param viewAttrib
     * @param value
     * @throws SQLException
     */
    protected void setAttribute( EntityInstance entityInstance, ViewAttribute viewAttrib, Object value ) throws SQLException
    {
        Object convertedValue = getTranslator().convertDbValue( viewAttrib.getDomain(), value );
        entityInstance.getAttribute( viewAttrib).setInternalValue( convertedValue, false );

        assert ! entityInstance.isAttributeUpdated( viewAttrib ) : "Attribute is updated " + viewAttrib.toString();
    }

    /**
     * This will load all the entities/attributes from the select statement.
     * @param loadedEntities
     * @param entityCount
     * @throws SQLException
     */
    private void loadAttributes( SqlStatement   stmt,
                                 ResultSet      rs,
                                 View           view,
                                 Map<String, EntityInstance> loadedEntities,
                                 IntegerLinkedHashMap<ViewEntity> entityCount ) throws SQLException
    {
        // Some JDBC drivers don't allow us to retrieve the same column twice from the same statement.
        // To get around this we'll store the values in an array in case we need to use them again.
        Map<Integer,Object> loadedObjects = new HashMap<Integer,Object>(10);

        // Loop through each of the DataRecords that are part of this statement.
        for ( DataRecord dataRecord : stmt.dataRecords.keySet() )
        {
            final ViewEntity viewEntity = dataRecord.getViewEntity();
            EntityInstance entityInstance = null;

            // For each DataRecord, load the attributes.
            for ( DataField dataField : stmt.dataRecords.get( dataRecord ) )
            {
                try
                {
                    Integer columnIdx = stmt.getColumns().get( dataField );
                    Object value;
                    try
                    {
                        value = getSqlObject( rs, columnIdx, loadedObjects );
                    }
                    catch ( SQLException e )
                    {
                        throw ZeidonException.wrapException( e ).appendMessage( "DataField: %s, column idx: %d", dataField, columnIdx );
                    }

                    if ( value == null )
                        continue;

                    // Create the new entity if we haven't already loaded this instance, otherwise set the cursor to it.
                    if ( entityInstance == null )
                    {
                        // It is possible for an entity instance to appear more than once in the result set.  This can
                        // happen if qualification returns it twice or if we're doing a join of small 1-to-many tables
                        // and the parent instance is returned for each child table.
                        //
                        // To handle this we'll grab the key values for the entity and see if we've already loaded
                        // an instance with those keys.  Get a key string of all keys of this entity instance and
                        // its parents.
                        String keyString = getFullKeyValuesForCurrentRow( viewEntity, rs, stmt, loadedObjects );
                        entityInstance = loadedEntities.get( keyString );
                        if ( entityInstance != null )
                        {
                            // We've already loaded this entity instance.  Set the cursor and stop
                            // loading the attributes for this instance.
                            view.cursor(  viewEntity ).setCursor( entityInstance );
                            break;
                        }

                        // Create the entity but tell the OE not to spawn because this will cause the OE
                        // to attempt to spawn entities that we've already loaded.
                        entityInstance = view.cursor( viewEntity ).createEntity( CursorPosition.LAST, CREATE_FLAGS );

                        loadedEntities.put( keyString, entityInstance );
                        entityCount.increment( viewEntity );

                        // We've created a new instance so now check to see if we link this instance with another.
                        String entityKeyString = getKeyValuesForCurrentEntity( viewEntity, rs, stmt, loadedObjects );

                        if ( viewEntity.isRecursive() )
                            checkForInfiniteRecursiveLoop( view, viewEntity, entityKeyString );

                        // TODO: We need to work out some problems if we're going to automatically relink.
                        // 1: Entities load different sets of attributes and if one entity doesn't load all the
                        //    necessary attributes then the linked instance appears to have missing attributes.
//                        if ( entityLinker.addEntity( entityInstance, entityKeyString ) )
//                            break;  // This entity was relinked with another entity so we can stop loading it.
                    }

                    ViewAttribute viewAttrib = dataField.getViewAttribute();
                    setAttribute( entityInstance, viewAttrib, value );
                }
                catch ( Exception e )
                {
                    throw ZeidonException.wrapException( e )
                                         .prependViewAttribute( dataField.getViewAttribute() )
                                         .prependMessage( "Column = %s.%s", dataRecord.getRecordName(), dataField.getName() );
                }
            } // for each DataField...

            assert assertNotNullKey( view, viewEntity ) : "Activated entity has null key";

        } // for each DataRecord...
    }

    /**
     * Make sure the key string doesn't exist in parent entities.  This indicates an infinite loop.
     *
     * @param view
     * @param viewEntity
     * @param entityKeyString
     */
    private void checkForInfiniteRecursiveLoop( View view, ViewEntity viewEntity, String entityKeyString )
    {
        ViewEntity parent = viewEntity.getParent();

        // Search through the parent chain of the current entity.
        for ( EntityInstance ei = view.cursor( parent ).getEntityInstance(); ei != null; ei = ei.getParent() )
        {
            // If the ei has a different relationship from viewEntity then it's not
            // part of a recursive loop.
            if ( ei.getViewEntity().getErRelToken() != viewEntity.getErRelToken() )
                continue;

            String parentKeyString = ei.getKeyString();
            if ( StringUtils.equals( entityKeyString, parentKeyString ) )
            {
                throw new ZeidonException( "Infinite recursive loop detected while activating OI" )
                                .appendMessage( "Child entity: %s", viewEntity.getName() )
                                .appendMessage( "Child key string: %s", entityKeyString )
                                .appendMessage( "Parent entity: %s", ei.getViewEntity().getName() )
                                .appendMessage( "Parent key string: %s", parentKeyString );
            }
        }
    }

    private void close( ResultSet rs, PreparedStatement ps )
    {
        try
        {
            close( rs );

            if ( cachedStatements == null ) // Are we caching PreparedStatements?
                DbUtils.closeQuietly( ps );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void close(ResultSet rs)
    {
        try
        {
            if (rs != null)
            {
                rs.close();
            }
        }
        catch (SQLException e)
        {
            task.dblog().error( e );
        }
    }

    @Override
    protected int executeSql(String sql)
    {
        beginTransaction();
        logSql( sql );

        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            ps = prepareAndBind( null, sql, null, null, null );
            ps.execute();
        }
        catch ( Exception e )
        {
            throw ZeidonException.prependMessage( e, "SQL => %s", sql );
        }
        finally
        {
            close( rs, ps );
            endTransaction( true );
        }

        return 0;
    }

    /**
     * Returns a PreparedStatement for the sql.  If we are caching PreparedStatements then this will
     * perform necessary logic for caching.  Will bind attributes if there are any.
     * @param stmt TODO
     * @param sql
     * @param view TODO
     * @param viewEntity TODO
     * @param commandType
     *
     * @return
     * @throws SQLException
     */
    private PreparedStatement prepareAndBind(SqlStatement stmt, String sql, View view, ViewEntity viewEntity, SqlCommand commandType) throws SQLException
    {
        PreparedStatement ps = null;

        // Not every statement can be cached.
        boolean cacheThisCommand = false;
        if ( commandType != null )
        {
            switch ( commandType )
            {
                case SELECT:
                case DELETE:
                    cacheThisCommand = true;
                    break;
            }
        }

        if ( cacheThisCommand && cachedStatements != null && viewEntity != null )  // Are we using cached PreparedStatements?
        {
            PreparedStatementCacheKey key = new PreparedStatementCacheKey( viewEntity, commandType, sql );
            PreparedStatementCacheValue value = cachedStatements.get( key.getKey() );
            if ( value == null )
            {
                ps = useDbGenerateKeys() ? connection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS ) :
                                           connection.prepareStatement( sql );
                value = new PreparedStatementCacheValue( ps, sql );
                cachedStatements.put( key.getKey(), value );
            }
            else
            {
                task.dblog().trace( "Using cached statement for Entity => %s \n=> %s", viewEntity, sql );
                ps = value.ps;
            }
        }
        else
        {
            // Some JDBC implementations don't support Statement.NO_GENERATED_KEYS (SQLDroid I'm looking
            // at you) so we have to use the single-argument prepareStatement if we aren't keeping the
            // generated keys.
            if ( useDbGenerateKeys() )
                ps = connection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            else
                ps = connection.prepareStatement( sql );
        }


        if ( stmt != null ) // When executing simple statements this will be null.
        {
            int idx = 0;
            for ( Object boundValue : stmt.getBoundValues() )
            {
                idx++;
                String valueAsString;
                if ( boundValue instanceof DataField )
                {
                    DataField dataField = (DataField) boundValue;
                    valueAsString = getTranslator().bindAttributeValue( ps, view, dataField, idx );
                }
                else
                {
                    valueAsString = getTranslator().bindAttributeValue( ps, boundValue, idx );
                }

                task.dblog().debug( "Bind idx %d = %s (attr value)", idx, valueAsString );
            }
        }

        return ps;
    }

    private String generateErrorMessageWithBoundAttributes( String sql, ViewEntity viewEntity, SqlStatement stmt )
    {
        StringBuilder sb = new StringBuilder( "SQL => " );
        sb.append( sql );

        int count = 0;
        for ( Object o : stmt.getBoundValues() )
        {
            count++;
            if ( o == null )
                sb.append( "\n   Value null" );
            else
                sb.append( "\n   Value " ).append( count ).append( " : " ).append( o )
                  .append( " [" ).append( o.getClass().getCanonicalName() ).append( "]" );
        }

        sb.append( "\n   ViewEntity : " ).append( viewEntity );
        return sb.toString();
    }

    @Override
    protected int executeStatement(View view, ViewEntity viewEntity, SqlStatement stmt)
    {
        String sql = stmt.getAssembledCommand();
        logSql( stmt );

        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            ps = prepareAndBind( stmt, sql, view, viewEntity, stmt.commandType );

            if ( stmt.commandType == SqlCommand.INSERT )
            {
                ps.executeUpdate();

                if ( useDbGenerateKeys() )
                {
                    generatedKeys = new ArrayList<Object>();
                    ResultSet rs2 = ps.getGeneratedKeys();
                    try
                    {
                        while ( rs2.next() )
                        {
                            Integer i = rs2.getInt( 1 );
                            generatedKeys.add( i );
                        }
                    }
                    finally
                    {
                        DbUtils.closeQuietly( rs2 );
                    }
                }
                else
                    generatedKeys = null;

            }
            else
            {
                ps.execute();
            }
        }
        catch ( Exception e )
        {
            throw ZeidonException.prependMessage( e, generateErrorMessageWithBoundAttributes( sql, viewEntity, stmt ) );
        }
        finally
        {
            close( rs, ps );
        }

        return 0;
    }

    /**
     * This is a temporary object that is used to create a key in the PreparedStatement cache.
     * Currently this uses the SQL string as part of the key but comparisons take longer than I'd
     * like.  It'd be nice to come up with a different solution.  The problem is that update statements
     * update only the attributes that were changed and this causes the SQL to look different for some
     * updates.
     *
     */
    static private class PreparedStatementCacheKey
    {
        private final ViewEntity viewEntity;
        private final SqlCommand commandType;
        private final String     sql;

        private PreparedStatementCacheKey(ViewEntity viewEntity, SqlCommand commandType, String sql)
        {
            assert viewEntity != null;
            assert commandType != null;
            assert sql != null;

            this.viewEntity = viewEntity;
            this.commandType = commandType;
            this.sql = sql;
        }

        private String getKey()
        {
            return sql;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + commandType.hashCode();
            result = prime * result + viewEntity.hashCode();
            result = prime * result + sql.hashCode();
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            if ( this == obj )
                return true;
            if ( obj == null )
                return false;
            if ( getClass() != obj.getClass() )
                return false;
            PreparedStatementCacheKey other = (PreparedStatementCacheKey) obj;
            if ( commandType != other.commandType )
                return false;
            if ( viewEntity != other.viewEntity )
                    return false;
            if ( ! sql.equals( other.sql ) )
                return false;

            return true;
        }

        @Override
        public String toString()
        {
            return viewEntity.toString() + " " + commandType.toString() + " " + StringUtils.substring( sql, 0, 50 );
        }
    }

    static private class PreparedStatementCacheValue
    {
        private final PreparedStatement ps;
        private final String            sql;

        private PreparedStatementCacheValue(PreparedStatement ps, String sql)
        {
            assert ps != null;
            assert sql != null;

            this.ps = ps;
            this.sql = sql;
        }

        @Override
        public String toString()
        {
            return sql;
        }
    }

    /**
     * Create our own private class that extends GenericObjectPool so we can insert it into
     * the task cache without worry of collision.
     */
    private static class JdbcConnectionPool
    {
        /**
         * We need to keep a separate pool for each type of connection string.
         */
        private final ConcurrentMap<String,BasicDataSource> poolMap;

        private JdbcConnectionPool()
        {
            poolMap = new MapMaker().concurrencyLevel( 4 ).makeMap();
        }

        private Connection getConnection( String url, Task task, JdbcHandler handler, Application application )
        {
            Connection connection;

            try
            {
                BasicDataSource pool = getPool( url, task, handler, application );
                connection = pool.getConnection();
                connection.setAutoCommit( false );
            }
            catch ( SQLException e )
            {
                throw ZeidonException.wrapException( e )
                                     .appendMessage( "Connection String = %s", url )
                                     .appendMessage( "Username: %s", handler.getUserName() );
            }

            return connection;
        }

        private String getDriver( String url, Task task, JdbcHandler handler )
        {
            String driver = handler.getDrivers();
            if ( StringUtils.isBlank( driver ) )
            {
                // Drivers wasn't specified in the config, so if possible we'll guess
                // by using the connection string.
                if ( url.startsWith( "jdbc:mysql:" ) )
                    driver = "com.mysql.jdbc.Driver";
                else
                if ( url.startsWith( "jdbc:sqlite:" ) )
                    driver = "org.sqlite.JDBC";
                else
                if ( url.startsWith( "jdbc:odbc:" ) )
                    driver = "sun.jdbc.odbc.JdbcOdbcDriver";
                else
                if ( url.startsWith( "jdbc:sqlserver:" ) )
                    driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                else
                    throw new ZeidonException( "JDBC Driver wasn't specified in config for %s", url );
            }

            return driver;
        }

        private BasicDataSource getPool( String url, Task task, JdbcHandler handler, Application application )
        {
            BasicDataSource pool = poolMap.get( url );
            if ( pool == null )
            {
                // We should get here only rarely.  Let's synchronize using the connStr.
                url = url.intern();
                synchronized ( url )
                {
                    pool = new BasicDataSource();

                    String driverClassName = getDriver( url, task, handler);
                    pool.setDriverClassName( driverClassName );

                    String username = handler.getUserName();
                    String password = handler.getPassword();

                    pool.setUsername( username );
                    pool.setPassword( password );
                    pool.setUrl( url );
                    pool.setTestOnBorrow( true );
                    pool.setValidationQuery( "select 1" );
                    poolMap.putIfAbsent( url, pool );

                    // It's even less likely that two threads created their own connection
                    // pool but it doesn't hurt handle it.
                    pool = poolMap.get( url );
                }
            }

            return pool;
        }
    }

    /**
     * Returns the keys generated by the last insert.
     */
    @Override
    public List<Object> getKeysGeneratedByDb()
    {
        return generatedKeys;
    }

    static private final Class<?>[] translatorConstructorArgs = new Class<?>[] { Task.class, JdbcHandler.class };
    public JdbcDomainTranslator getTranslator()
    {
        if ( translator == null )
        {
            String transName = getConfigValue( "Translator" );

            // If translator name isn't defined, use the standard one.
            if ( StringUtils.isBlank( transName ) )
            {
                // Translator isn't specified.  Let's try to be smart and determine the
                // correct translator from the connection string.
                String connStr = options.getOiSourceUrl();
                if ( ! StringUtils.isBlank( connStr ) )
                {
                    if ( connStr.contains( "sqlite" ) )
                        return new SqliteJdbcTranslator( task, this );
                }

                return new StandardJdbcTranslator( task, this );
            }

            try
            {
                ObjectEngine oe = task.getObjectEngine();
                ClassLoader classLoader = oe.getClassLoader( transName );
                Class<? extends JdbcDomainTranslator> translatorClass;
                translatorClass = (Class<? extends JdbcDomainTranslator>) classLoader.loadClass( transName );
                Constructor<? extends JdbcDomainTranslator> constructor = translatorClass.getConstructor( translatorConstructorArgs );
                translator = constructor.newInstance( task, this );
            }
            catch ( Throwable t )
            {
                throw ZeidonException.prependMessage( t, "Error trying to load translator class = '%s', DB=%s",
                                                      transName, options.getOiSourceUrl() );
            }
        }

        return translator;
    }

    public String getDateAsStringFormat()
    {
        if ( dateFormat == null )
        {
            dateFormat = getConfigValue( "DateFormat" );
            if ( StringUtils.isBlank( dateFormat ) )
                dateFormat = "yyyy-MM-dd";
        }

        return dateFormat;
   }

    public String getDrivers()
    {
        if ( drivers == null )
            drivers = getConfigValue( "Drivers" );

        return drivers;
    }
}