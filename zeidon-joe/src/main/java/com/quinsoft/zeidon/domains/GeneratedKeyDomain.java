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
package com.quinsoft.zeidon.domains;

import java.util.Map;

import com.quinsoft.zeidon.Application;
import com.quinsoft.zeidon.AttributeInstance;
import com.quinsoft.zeidon.Blob;
import com.quinsoft.zeidon.GeneratedKey;
import com.quinsoft.zeidon.InvalidAttributeConversionException;
import com.quinsoft.zeidon.InvalidAttributeValueException;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.objectdefinition.AttributeDef;
import com.quinsoft.zeidon.standardoe.GeneratedKeyImpl;

/**
 * This is a domain for storing keys generated by the DB.  The domain is constructed
 * in a way that it can hold keys for any DB.  Internally the keys are stored as
 * strings and it is up to the DBHandler to convert it to the correct type.
 *
 */
public class GeneratedKeyDomain extends AbstractDomain
{

    /**
     * @param app
     * @param domainProperties
     * @param task
     */
    public GeneratedKeyDomain( Application app, Map<String, Object> domainProperties, Task task )
    {
        super( app, domainProperties, task );
    }

    /* (non-Javadoc)
     * @see com.quinsoft.zeidon.domains.Domain#convertExternalValue(com.quinsoft.zeidon.Task, com.quinsoft.zeidon.AttributeInstance, com.quinsoft.zeidon.objectdefinition.AttributeDef, java.lang.String, java.lang.Object)
     */
    @Override
    public Object convertExternalValue( Task task,
                                        AttributeInstance attributeInstance,
                                        AttributeDef attributeDef,
                                        String contextName,
                                        Object externalValue ) throws InvalidAttributeValueException
    {
        // If external value is an AttributeInstance then get *its* internal value.
        if ( externalValue instanceof AttributeInstance )
            externalValue = ((AttributeInstance) externalValue).getValue();

        if ( externalValue == null )
            return null;

        if ( externalValue instanceof GeneratedKey )
            return externalValue;
        
        return new GeneratedKeyImpl( externalValue.toString() );
    }

    @Override
    public void validateInternalValue( Task task, AttributeDef attributeDef, Object internalValue ) throws InvalidAttributeValueException
    {
        if ( internalValue instanceof GeneratedKey )
            return;

        throw new InvalidAttributeValueException( attributeDef, internalValue, "Attribute is expecting a GeneratedKey, got %s", internalValue.getClass() );
    }

    @Override
    public boolean isNull( Task task, AttributeDef attributeDef, Object value )
    {
        if ( value == null )
            return true;

        assert value instanceof GeneratedKey : "Unexpected class found: " + value.getClass().getCanonicalName();
        return ((GeneratedKey) value).isNull();
    }


    @Override
    public String convertToString( Task task, AttributeDef attributeDef, Object internalValue, String contextName )
    {
        if ( isNull( task, attributeDef, internalValue ) )
            return null;

        return ((GeneratedKey) internalValue).getString();
    }

    @Override
    public Blob convertToBlob( Task task, AttributeDef attributeDef, Object internalValue )
    {
        // We don't allow genkeys to be converted to boolean.
        throw new InvalidAttributeConversionException( attributeDef, "Cannot convert GeneratedKeyDomain to Blob", attributeDef.toString() );
    }

    @Override
    public Double convertToDouble( Task task, AttributeDef attributeDef, Object internalValue )
    {
        // We don't allow genkeys to be converted to Double.
        throw new InvalidAttributeConversionException( attributeDef, "Cannot convert GeneratedKeyDomain to Double", attributeDef.toString() );
    }

    /**
     * We don't allow GeneratedKeyDomains to be automatically converted into integers.
     * This prevents the accidental usage of keys as integers.
     */
    @Override
    public Integer convertToInteger( Task task, AttributeDef attributeDef, Object internalValue )
    {
        // We don't allow genkeys to be converted to integer.
        throw new InvalidAttributeConversionException( attributeDef, "Cannot convert GeneratedKeyDomain to Integer", attributeDef.toString() );
    }

    @Override
    public Boolean convertToBoolean(Task task, AttributeDef attributeDef, Object internalValue )
    {
        // We don't allow genkeys to be converted to boolean.
        throw new InvalidAttributeConversionException( attributeDef, "Cannot convert GeneratedKeyDomain to Boolean", attributeDef.toString() );
    }

    @Override
    public int compare( Task task,
                        AttributeInstance attributeInstance,
                        AttributeDef attributeDef,
                        Object internalValue,
                        Object externalValue )
    {
        Object value = convertExternalValue( task, attributeInstance, attributeDef, null, externalValue );
        Integer rc = compareNull( task, attributeDef, internalValue, value);
        if ( rc != null )
            return rc;

        return internalValue.toString().compareTo( externalValue.toString() );
    }
}
