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

import com.quinsoft.zeidon.objectdefinition.EntityDef;

/**
 * An attempt was made to access a cursor that is null.
 *
 */
public class NullCursorException extends ZeidonException
{
    private static final long serialVersionUID = 1L;

    private final EntityDef entityDef;

    public NullCursorException(EntityCursor cursor)
    {
        super( "Cursor for entity is null", cursor.getEntityDef() );
        prependEntityDef( cursor.getEntityDef() );
        entityDef = cursor.getEntityDef();
    }

    public EntityDef getEntityDef()
    {
        return entityDef;
    }

}
