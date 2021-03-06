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
package com.quinsoft.zeidon.standardoe;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.quinsoft.zeidon.AbstractOptionsConfiguration;
import com.quinsoft.zeidon.StreamFormat;
import com.quinsoft.zeidon.View;
import com.quinsoft.zeidon.ZeidonException;
import com.quinsoft.zeidon.objectdefinition.AttributeDef;
import com.quinsoft.zeidon.objectdefinition.EntityDef;
import com.quinsoft.zeidon.objectdefinition.LodDef;

/**
 * Data and methods common to File DB activate and commit.
 * @author dgc
 *
 */
class FileDbUtils
{
    private final AbstractOptionsConfiguration options;
    private final String                       directoryName;
    private final StreamFormat                 streamFormat;

    /**
     * If true, the the ioServerUrl specifies a filename instead of a directory
     * name.  In that case all activates take place from the same file.
     */
    private final boolean isFile;

    FileDbUtils( AbstractOptionsConfiguration options )
    {
        this.options = options;
        String url = this.options.getOiSourceUrl();
        String workstring;

        if ( url.startsWith( "file:" ) )
            workstring = url.substring( 5 );
        else
        if ( url.startsWith( "resource:" ) )
            workstring = url.substring( 9 );
        else
            throw new ZeidonException("File DB Error: oiSourceUrl doesn't start with 'file:' or 'resource:'.  URL = %s", url );

        if ( workstring.startsWith( "xml:" ) )
        {
            streamFormat = StreamFormat.XML;
            directoryName = workstring.substring( 4 );
        }
        else
        if ( workstring.startsWith( "json:" ) )
        {
            streamFormat = StreamFormat.JSON;
            directoryName = workstring.substring( 5 );
        }
        else
        if ( workstring.toLowerCase().endsWith( ".xml" ) )
        {
            streamFormat = StreamFormat.XML;
            directoryName = workstring;
        }
        else
        if ( workstring.toLowerCase().endsWith( ".json" ) )
        {
            streamFormat = StreamFormat.JSON;
            directoryName = workstring;
        }
        else
        {
            streamFormat = StreamFormat.POR;
            directoryName = workstring;
        }

        if ( url.startsWith( "resource:" ) )
        {
            isFile = true;
        }
        else
        {
            File f = new File( directoryName );
            if ( ! f.exists() )
                throw new ZeidonException( "File DB directory does not exist: %s", directoryName );

            isFile = f.isFile();
        }
    }

    String genFilename( LodDef lodDef, String qualifier )
    {
        if ( isFile )
            return directoryName;

        return directoryName + File.separator + lodDef.getName() + "_" + qualifier + streamFormat.getExtension();
    }

    /**
     * Generates the appropriate filename using the key value of the root.
     *
     * @param view
     * @return
     */
    String genKeyFilename( View view )
    {
        LodDef lodDef = view.getLodDef();
        EntityDef root = lodDef.getRoot();
        List<AttributeDef> keys = root.getKeys();
        if ( keys.size() > 1 )
            throw new ZeidonException( "File DB only supports root entities with a single key." );

        AttributeDef key = keys.get( 0 );
        String value = view.cursor( root ).getAttribute( key ).getString();
        String qualifier = genKeyQualifier( key, value );
        return genFilename( lodDef, qualifier );
    }

    String genKeyQualifier( AttributeDef key, String value )
    {
        if ( StringUtils.isBlank( value ) )
            throw new ZeidonException( "Key value may not be null.  Key = %s.%s",
                                       key.getEntityDef().getName(), key.getName() );

        return key.getName() + "_" + value;
    }

    StreamFormat getStreamFormat()
    {
        return streamFormat;
    }

    String getDirectoryName()
    {
        return directoryName;
    }

    /**
     * Returns true if the oiSourceUrl specifies a filename instead of a directory.
     *
     * @return
     */
    boolean urlIsFile()
    {
        return isFile;
    }
}
