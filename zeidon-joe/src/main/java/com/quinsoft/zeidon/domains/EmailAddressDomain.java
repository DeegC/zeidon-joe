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

    Copyright 2009-2014 QuinSoft
 */

package com.quinsoft.zeidon.domains;

import java.util.Map;

import org.apache.commons.validator.EmailValidator;

import com.quinsoft.zeidon.Application;
import com.quinsoft.zeidon.InvalidAttributeValueException;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.objectdefinition.AttributeDef;

/**
 * @author DG
 *
 */
public class EmailAddressDomain extends StringDomain
{

    public EmailAddressDomain(Application application, Map<String, Object> domainProperties, Task task )
    {
        super( application, domainProperties, task );
    }

    @Override
    public void validateInternalValue( Task task, AttributeDef attributeDef, Object internalValue ) throws InvalidAttributeValueException
    {
        String string = checkNullString( internalValue );
        
        String[] temp;
        String delims = "[;,]+";
        
        // Eliminate any blank spaces in email.
        string = string.replaceAll("\\s+", "");		   
        
        // It is valid for the email address to be more than one address separated by "," or ";" so account for that.
        temp = string.split(delims);
        for(int i =0; i < temp.length ; i++)
        {
            if ( ! EmailValidator.getInstance().isValid( temp[i] ) ) 
                throw new InvalidAttributeValueException( attributeDef, string, "Value must be a valid email address" );
        }
           
        super.validateInternalValue( task, attributeDef, internalValue );
    }

    @Override
    public String convertToString(Task task, AttributeDef attributeDef, Object internalValue )
    {
        return convertToString( task, attributeDef, internalValue, "" );
    }
    
    public String convertToString(Task task, AttributeDef attributeDef, Object internalValue, String contextName)
    {
        String string = checkNullString( internalValue );
        // Eliminate any blank spaces in email.
        string = string.replaceAll("\\s+", "");		   
    		// no context given.
        return( string );
    }
}
