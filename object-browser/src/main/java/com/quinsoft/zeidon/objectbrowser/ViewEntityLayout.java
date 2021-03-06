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

package com.quinsoft.zeidon.objectbrowser;

import com.quinsoft.zeidon.objectdefinition.EntityDef;

/**
 * @author DG
 *
 */
class EntityDefLayout
{
    private final LodDefLayout lodDefLayout;
    private final EntityDef   entityDef;
    private final int width;
    
    /**
     * @param lodDefLayout
     * @param entityDef
     */
    EntityDefLayout(LodDefLayout lodDefLayout, EntityDef entityDef)
    {
        this.lodDefLayout = lodDefLayout;
        this.entityDef = entityDef;
        
        if ( entityDef.getChildCount() == 0 )
        {
            width = 1;
        }
        else
        {
            int w = 0;
            for ( EntityDef child : entityDef.getChildren() )
            {
                EntityDefLayout layout = new EntityDefLayout( lodDefLayout, child );
                w += layout.getWidth();
            }
            width = w;
        }
        
        this.lodDefLayout.addEntityDefLayout( this.entityDef, this );
    }

    public int getWidth()
    {
        return width;
    }

    public LodDefLayout getLodDefLayout()
    {
        return lodDefLayout;
    }

    public EntityDef getEntityDef()
    {
        return entityDef;
    }
}
