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

package com.quinsoft.zeidon;

import com.quinsoft.zeidon.objectdefinition.AttributeDef;

/**
 * @author DG
 *
 */
public class InvalidAttributeValueException extends ZeidonException
{
    private static final long serialVersionUID = 1L;
    
    private final AttributeDef attributeDef;
    private final Object        inputValue;
    private final String        reason;
    
    public InvalidAttributeValueException( AttributeDef attributeDef, Object inputValue, String reason, Object...args )
    {
        super( "Invalid value for attribute" );
        if ( args.length > 0 )
           reason = String.format( reason, args );  // we want reason to be formatted for setting this.reason below
        
        // KJS 01/16/13 - I am having a problem where "reason" is null and causing errors with DoInputMapping in jsp pages.
        // I am going to set reason if it's null.
        if ( reason == null )
        	reason = "Invalid value for attribute";

        prependMessage( reason );
        prependMessage( "Value = %s", inputValue );
        prependAttributeDef( attributeDef );
        
        this.attributeDef = attributeDef;
        this.inputValue = inputValue;
        this.reason = reason;
    }

    public AttributeDef getAttributeDef()
    {
        return attributeDef;
    }

    public Object getInputValue()
    {
        return inputValue;
    }

    public String getReason()
    {
        return reason;
    }
}
