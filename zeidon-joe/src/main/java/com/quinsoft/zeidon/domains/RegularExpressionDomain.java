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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.quinsoft.zeidon.Application;
import com.quinsoft.zeidon.InvalidAttributeValueException;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.objectdefinition.AttributeDef;

/**
 * @author DG
 *
 */
public class RegularExpressionDomain extends StringDomain
{
    private final Pattern domainPattern;

    public RegularExpressionDomain(Application application, Map<String, Object> domainProperties, Task task )
    {
        super( application, domainProperties, task );
        if ( StringUtils.isBlank( getConstraintRule() ) )
            domainPattern = null;
        else
            domainPattern = Pattern.compile( getConstraintRule() );
    }

    @Override
    public void validateInternalValue( Task task, AttributeDef attributeDef, Object internalValue ) throws InvalidAttributeValueException
    {
        String string = checkNullString( internalValue );

        // Validate string length
        super.validateInternalValue( task, attributeDef, string );

        if ( domainPattern != null )
        {
            Matcher m = domainPattern.matcher( string );
            if ( ! m.matches() )
                throw new InvalidAttributeValueException( attributeDef, string, "Input value does not match regular expression %s", getConstraintRule() );
        }
        else
        {
            DomainContext context = getContext( task, null );
            context.validateInternalValue( task, attributeDef, string ); // Validate the regex.
        }
    }

    @Override
    public DomainContext newContext(Task task)
    {
        return new RegexContext( this );
    }

    @Override
    public String convertToString(Task task, AttributeDef attributeDef, Object internalValue, String contextName)
    {
        DomainContext context = getContext( task, contextName );
        return context.convertToString( task, attributeDef, internalValue );
    }

    private class RegexContext extends BaseDomainContext
    {
        private Pattern pattern;

		public RegexContext(Domain domain)
		{
			super(domain);
		}

	    @Override
	    public void validateInternalValue( Task task, AttributeDef attributeDef, Object value ) throws InvalidAttributeValueException
	    {
	        String string = checkNullString( value );
	        Matcher m = pattern.matcher( string );
	        if ( ! m.matches() )
	            throw new InvalidAttributeValueException( attributeDef, value, "Input value does not match regular expression %s", getEditString() );
	    }

        @Override
        protected void setEditString( String editString )
        {
            super.setEditString( editString );
            pattern = Pattern.compile( editString );
        }
    }
}
