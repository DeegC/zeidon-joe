/**
 *
 */
package com.quinsoft.zeidon.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import com.quinsoft.zeidon.DeserializeOi;
import com.quinsoft.zeidon.CursorResult;
import com.quinsoft.zeidon.EntityCursor;
import com.quinsoft.zeidon.ObjectEngine;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.View;
import com.quinsoft.zeidon.SerializeOi;
import com.quinsoft.zeidon.objectdefinition.ViewEntity;
import com.quinsoft.zeidon.objectdefinition.ViewOd;
import com.quinsoft.zeidon.standardoe.JavaObjectEngine;
import com.quinsoft.zeidon.utils.QualificationBuilder;

/**
 * @author DG
 *
 */
class SimpleTest
{
    static int createChildEntities( int entityCount, View view, ViewEntity viewEntity )
    {
        if ( !viewEntity.isCreate() )
            return entityCount;

        view.cursor( viewEntity.getName() ).createEntity();
        entityCount++;
        for ( ViewEntity child : viewEntity.getChildren() )
        {
            for ( int i = 0; i < 2; i++ )
            {
                entityCount = createChildEntities( entityCount, view, child );
            }
        }
        return entityCount;
    }

    public static String getStackTraceString( Throwable aThrowable )
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter( result );
        aThrowable.printStackTrace( printWriter );
        return result.toString();
    }

    static void stressTest()
    {
        ObjectEngine oe = JavaObjectEngine.getInstance();

        Task zeidonSystem = oe.getSystemTask();
        View view = zeidonSystem.activateEmptyObjectInstance( "kzwdlgxo" );
        ViewOd viewOd = view.getViewOd();
        ViewEntity rootEntity = viewOd.getRoot();
        int entityCount = 0;
        for ( int i = 0; i < 2000; i++ )
        {
            if ( i % 100 == 0 )
                view.log().debug( "Creating %s %d", rootEntity, i );

            entityCount = createChildEntities( entityCount, view, rootEntity );
            view.cursor( rootEntity.getName() ).setAttribute( "Tag", i );
        }

        view.log().debug( "Created %d entities", entityCount );
        zeidonSystem.log().debug( "Starting cursor test" );

        // Run the test without needing to convert the attribute value every time.
        String tag = "1500";
        for ( int i = 0; i < 1000; i++ )
        {
            for ( CursorResult rc = view.cursor( "Dlg" ).setFirst();
                  rc.isSet();
                  rc = view.cursor( "Dlg" ).setNext() )
            {
                if ( view.cursor( "Dlg" ).compareAttribute( "Tag", tag ) == 0 )
                    break;
            }
        }
        zeidonSystem.log().debug( "Done cursor test 1" );

        // Rerun the test using an int value that must be converted to a string.
        for ( int i = 0; i < 1000; i++ )
        {
            for ( CursorResult rc = view.cursor( "Dlg" ).setFirst();
                  rc.isSet();
                  rc = view.cursor( "Dlg" ).setNext() )
            {
                if ( view.cursor( "Dlg" ).compareAttribute( "Tag", 1500 ) == 0 )
                    break;
            }
        }
        zeidonSystem.log().debug( "Done cursor test 2" );

        // Rerun the test but use a cursor object instead of the view.
        EntityCursor cursor = view.cursor( "Dlg" );
        for ( int i = 0; i < 1000; i++ )
        {
            for ( CursorResult rc = cursor.setFirst();
                  rc.isSet();
                  rc = cursor.setNext() )
            {
                if ( cursor.compareAttribute( "Tag", 1500 ) == 0 )
                    break;
            }
        }
        zeidonSystem.log().debug( "Done cursor test 3" );

        // One last time without the attribute conversion.
        for ( int i = 0; i < 1000; i++ )
        {
            for ( CursorResult rc = cursor.setFirst();
                  rc.isSet();
                  rc = cursor.setNext() )
            {
                if ( cursor.compareAttribute( "Tag", tag ) == 0 )
                    break;
            }
        }
        zeidonSystem.log().debug( "Done cursor test 4" );

//        for ( EntityInstance dlg : view.getEntityListUnderParent( "Dlg" ) )
//        {
//            zeidonSystem.log().debug( "Dlg %s", dlg.getStringFromAttribute( "Tag" ) );
//            view.cursor( "Wnd" ).deleteEntity();
//        }
//        zeidonSystem.log().debug( "Done cursor test" );
    }

    private static void test( Task zencas )
    {
        View v = zencas.activateOiFromFile( "mStudent", "testdata/ZENCAs/mstudent_ac.por" );
        v.cursor("Student").setAttributeFromAttribute( "GeneralNote", v, "Student", "StudentLifeClearedDate" );
        System.out.println( "done " + v.cursor( "Student" ).getStringFromAttribute( "GeneralNote" ) );
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        System.out.println( sb.toString() );
        String properties = System.getProperties().toString();
        System.out.println( "CWD = " + System.getProperty("user.dir") );
        System.out.println( "CWD = " + System.getProperty("user.DIR") );
        System.out.println( "ZEIDON_HOME = " + System.getenv("ZEIDON_HOME") );

//        String fileDbUrl = "file:json:/tmp/filedb";
//        String fileDbUrl = "http://localhost:8080/test-restserver-1.0.6-SNAPSHOT/restserver";
        String fileDbUrl = "jdbc:sqlite:/home/dgc/zeidon/sqlite/zencasa.sqlite";
        ObjectEngine oe = JavaObjectEngine.getInstance();
        Task zencas = oe.createTask( "ZENCAs" );

        View stud = new QualificationBuilder( zencas )
                            .setViewOd( "lStudDpt" )
                            .setOiSourceUrl( fileDbUrl )
                            .addAttribQual( "Status", "A" )
                            .addAttribQual( "AND" )
                            .addAttribQual( "MajorDepartment", "ID", "=", 3 )
                            .activate();

//        JoeUtils.writeOiToJsonFile( stud, "/tmp/stud.json" );
        new SerializeOi().toFile( "/tmp/stud2.json" ).asJson().withoutHeaders().write( stud );

        View stud2 = new DeserializeOi( zencas )
                            .fromResource( "/tmp/stud2.json" )
                            .setViewOd( "lStudDpt" )
                            .asJson()
                            .activateFirst();
        stud2.logObjectInstance();

        List<View> stud3 = new DeserializeOi( zencas )
                            .fromResource( "/tmp/stud.json" )
                            .setViewOd( "lStudDpt" )
                            .asJson()
                            .activate();
        stud3.get( 0 ).logObjectInstance();

        //        stud.logObjectInstance();
/*
        CommitOptions options = new CommitOptions( zencas );
        options.setOiSourceUrl( fileDbUrl );
        stud.commit( options );

        stud = new QualificationBuilder( zencas )
                            .setViewOd( "lStudDpt" )
                            .addAttribQual( "eMailAddress", "kellysautter@comcast.net" )
                            .setOiSourceUrl( fileDbUrl )
                            .activate();
*/
    }
}