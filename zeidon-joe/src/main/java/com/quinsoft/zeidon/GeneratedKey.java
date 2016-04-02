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

/**
 * A generic value for keys generated by the DB.  The data type of the value in the DB
 * is DB-specific, e.g. integer for MySql, GUID for Sql Server, or Object ID for MongoDB.
 * The value can be extracted only as a string in application code.
 *
 * This is paired with GeneratedKeyDomain.
 *
 */
public interface GeneratedKey extends Comparable<Object>
{
    /**
     * @return the value of the key as a string.  This is different from toString() in
     * that it will throw an exception if the value is null.
     */
    String getString();

    /**
     * Determines if the value of the generated key is null.  Null keys are only valid
     * when an entity is first created but not saved.
     *
     * @return true if key is null.
     */
    boolean isNull();

    /**
     * @return the native value of the key; e.g. Integer for MySql.
     */
    Object getNativeValue();
}
