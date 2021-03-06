/**
  * This file is part of the Zeidon Java Object Engine (Zeidon JOE).
  *
  * Zeidon JOE is free software: you can redistribute it and/or modify
  * it under the terms of the GNU Lesser General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * Zeidon JOE is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU Lesser General Public License for more details.
  *
  * You should have received a copy of the GNU Lesser General Public License
  * along with Zeidon JOE.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright 2009-2015 QuinSoft
  */
package com.quinsoft.zeidon.scala

import com.quinsoft.zeidon.ZeidonException

/**
 * Convenience class for asserting various things about a View.
 */
class AssertView( val view: View ) {
    /**
     * Throws an exception if the View does not have an instantiation associated with it.
     */
    def mustBeInstantiated = {
        if ( view.jview == null )
            throw new ZeidonException( "Calling an ObjectOperation with an uninstatiated view" )

        this
    }

    def lodName( lodName: String ) = {
        if ( view.lodName != lodName )
            throw new ZeidonException( "Unexpected LOD name for view.  Expected %s, found %s.",
                                       lodName, view.odName )

        this
    }

    def empty = {
        mustBeInstantiated
        if ( ! view.isEmpty )
            throw new ZeidonException( "View '%s' must be empty", view.odName )

        this
    }


    def notEmpty = {
        mustBeInstantiated
        if ( view.isEmpty )
            throw new ZeidonException( "View '%s' must not be empty", view.odName )

        this
    }
}