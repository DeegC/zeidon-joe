/**
 *
 */
package com.quinsoft.zencas;

import java.sql.SQLException;

import com.quinsoft.zeidon.AbstractOptionsConfiguration;
import com.quinsoft.zeidon.EntityInstance;
import com.quinsoft.zeidon.InvalidAttributeValueException;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.dbhandler.SqliteJdbcHandler;
import com.quinsoft.zeidon.objectdefinition.AttributeDef;

/**
 * @author dgc
 *
 */
public class ZencasSqliteDbHandler extends SqliteJdbcHandler
{
    private final boolean productionMode = true;

    public ZencasSqliteDbHandler(Task task, AbstractOptionsConfiguration options )
    {
        super( task, options );
    }

    @Override
    protected void setAttribute( EntityInstance entityInstance, AttributeDef attributeDef, Object value ) throws SQLException
    {
        try
        {
            super.setAttribute( entityInstance, attributeDef, value ) ;
        }
        catch ( InvalidAttributeValueException e )
        {
            if ( ! productionMode )
                throw e;
        }
    }
}
