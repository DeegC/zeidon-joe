/**
 *
 */
package com.quinsoft.zeidon.test;

import com.quinsoft.zeidon.CursorPosition;
import junit.framework.Assert;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Before;
import org.junit.Test;

import com.quinsoft.zeidon.CursorResult;
import com.quinsoft.zeidon.EntityInstance;
import com.quinsoft.zeidon.EntityCursor;
import com.quinsoft.zeidon.ObjectEngine;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.View;
import com.quinsoft.zeidon.standardoe.JavaObjectEngine;
import com.quinsoft.zeidon.vml.VmlObjectOperations;
import com.quinsoft.zeidon.vml.zVIEW;

// Just for temporary testing...
//import com.jacob.com.*;
//import com.jacob.activeX.*;


/**
 * @author DG
 *
 */
public class TestEpamms
{
	Task         ePamms;
	Task         zeidonSystem;
	View         mFASrc;
	ObjectEngine oe;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
        oe = JavaObjectEngine.getInstance();
        ePamms = oe.createTask( "ePammsDon" );

		zeidonSystem = oe.getSystemTask();
	}

   @Test
	public void ExecuteJOE_Test000()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_Test000( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_Test000 ========");
	}

	@Test
	public void ExecuteJOE_Test0()
	{
	   View         mLLD;

		mLLD = ePamms.activateEmptyObjectInstance( "mLLD" );
		VmlTester tester = new VmlTester( mLLD );
		tester.ExecuteJOE_Test0( mLLD );
      System.out.println("===== Finished ExecuteJOE_Test0 ========");
	}

	@Test
	public void ExecuteJOE_Test00()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_Test00( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_Test00 ========");
	}

	@Test
	public void ExecuteJOE_Test1()
	{
	   View         testview;
		testview = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( testview );
		tester.ExecuteJOE_Test1( testview );
      System.out.println("===== Finished ExecuteJOE_Test1 ========");
	}

	@Test
	public void ExecuteJOE_Test2()
	{
	   View         testview;
		testview = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( testview );
		tester.ExecuteJOE_Test2( testview );
      System.out.println("===== Finished ExecuteJOE_Test2 ========");
	}

	@Test
	public void ExecuteJOE_TestSubobjectCreateView()
	{
	   View         testview;
		testview = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( testview );
		tester.ExecuteJOE_TestSubobjectCreateView( testview );
       System.out.println("===== Finished ExecuteJOE_TestSubobjectCreateView ========");
	}

	@Test
	public void ExecuteJOE_TestTemporalError()
	{
	   View         testview;
		testview = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( testview );
		tester.ExecuteJOE_TemporalDeleteError( testview );
       System.out.println("===== Finished ExecuteJOE_TemporalDeleteError ========");
	}

	@Test
	public void ExecuteJOE_TestSubobjectCheckExistence()
	{
	   View         testview;
		testview = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( testview );
		tester.ExecuteJOE_TestSubobjectCheckExistence( testview );
      System.out.println("===== Finished ExecuteJOE_TestSubobjectCheckExistence ========");
	}


	private class VmlTester extends VmlObjectOperations
	{
		public VmlTester( View view )
		{
			super( view );
		}

      private void displaySPLD( View vLLD, String entity, String msg ) {
         TraceLineS( "displaySPLD: ", msg );
         EntityCursor ec;
         if ( entity != null ) {
            ec = vLLD.getCursor( entity );
            ec.logEntity( false );
         }
         View t = vLLD.newView();
         t.resetSubobjectTop();
         ec = t.getCursor( "SPLD_LLD" );
         ec.logEntity( true );
         t.drop();
      }

      public int
		ExecuteJOE_Test000( View     view ) {
         View mSPLDef = ePamms.activateOiFromFile( "mSPLDef", zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/mSPLDefGood.json", null );
         displaySPLD( mSPLDef, "SPLD_LLD", "Triple Zero Activate" );

         EntityCursor ec = mSPLDef.getCursor( "LLD_Block" );
         ec.setFirst();
         View mSPLDef2 = mSPLDef.newView();

      // this works ...
         mSPLDef2.getCursor( "LLD_Block" ).setLast();
         mSPLDef2.getCursor( "LLD_SubBlock" ).setToSubobject();
         mSPLDef2.cursor( "LLD_Block" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
         mSPLDef2.resetSubobjectTop();
         displaySPLD( mSPLDef2, "SPLD_LLD", "After moveSubobject" );
      //
      /* also works
         EntityCursor ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.setLast();
         ec2 = mSPLDef2.getCursor( "LLD_SubBlock" );
         ec2.setToSubobject();
         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
         mSPLDef.logObjectInstance();
      */
         CommitOI_ToFile( mSPLDef2, zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/mSPLDef2.json", zASCII );
         mSPLDef = ePamms.activateOiFromFile( "mSPLDef", zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/mSPLDef2.json", null );
         displaySPLD( mSPLDef, "SPLD_LLD", "After Activate mSPLDef2" );

         // Try to move the SubBlock back to a panel.
         mSPLDef.resetSubobjectTop();

         mSPLDef2 = mSPLDef.newView();
         mSPLDef2.getCursor( "LLD_Panel" ).setFirst();

         ec = mSPLDef.getCursor( "LLD_SubBlock" );
         ec.setToSubobject();
         ec = mSPLDef.getCursor( "LLD_Block" );

         mSPLDef2.cursor( "LLD_Panel" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
         displaySPLD( mSPLDef2, "SPLD_LLD", "After second moveSubobject" );

         return 0;
      }
         
		public int
		ExecuteJOE_Test00( View     view )
		{
         zVIEW    mSPLDef = new zVIEW();
         
         ActivateOI_FromFile( mSPLDef, "mSPLDef", view, zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/mSPLDef.por", zSINGLE );
         displaySPLD( mSPLDef, "SPLD_LLD", "First Activate" );

         EntityCursor ec = mSPLDef.getCursor( "LLD_Block" );
         ec.setFirst();
         View mSPLDef2 = mSPLDef.newView();

      // this works ...
         mSPLDef2.getCursor( "LLD_Block" ).setLast();
         mSPLDef2.getCursor( "LLD_SubBlock" ).setToSubobject();
         mSPLDef2.cursor( "LLD_Block" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
         mSPLDef2.resetSubobjectTop();
         displaySPLD( mSPLDef2, "SPLD_LLD", "After moveSubobject" );
      //
      /* also works
         EntityCursor ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.setLast();
         ec2 = mSPLDef2.getCursor( "LLD_SubBlock" );
         ec2.setToSubobject();
         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
         mSPLDef.logObjectInstance();
      */
         CommitOI_ToFile( mSPLDef2, zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/mSPLDef2.por", zASCII );
         ActivateOI_FromFile( mSPLDef, "mSPLDef", view, zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/mSPLDef2.por", zSINGLE );
         displaySPLD( mSPLDef, "SPLD_LLD", "After Activate mSPLDef2" );

         // Try to move the SubBlock back to a panel.
         mSPLDef.resetSubobjectTop();

         mSPLDef2 = mSPLDef.newView();
         mSPLDef2.getCursor( "LLD_Panel" ).setFirst();

         ec = mSPLDef.getCursor( "LLD_SubBlock" );
         ec.setToSubobject();
         ec = mSPLDef.getCursor( "LLD_Block" );

         mSPLDef2.cursor( "LLD_Panel" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );

         displaySPLD( mSPLDef2, "SPLD_LLD", "After second moveSubobject" );

         return 0;
      }

		public int
		ExecuteJOE_Test0( View     mLLD )
		{
         EntityCursor ec = mLLD.getCursor( "LLD" );
         ec.createEntity( CursorPosition.FIRST );
         ec = mLLD.getCursor( "LLD_Page" );
         ec.createEntity( CursorPosition.FIRST );
         ec = mLLD.getCursor( "LLD_Panel" );
         ec.createEntity( CursorPosition.FIRST );
         ec = mLLD.getCursor( "LLD_Block" );
         ec.createEntity( CursorPosition.FIRST );
         ec.getAttribute( "Tag" ).setValue( "B1" );
         ec.createEntity( CursorPosition.NEXT );
         ec.getAttribute( "Tag" ).setValue( "B2" );
         mLLD.logObjectInstance();
         ec.setFirst();

         View mLLD2 = mLLD.newView();
         mLLD2.getCursor( "LLD_Block" ).setLast();
         mLLD2.getCursor( "LLD_SubBlock" ).setToSubobject();
         mLLD2.cursor( "LLD_Block" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
         mLLD.logObjectInstance();
         return 0;
      }

		public int
		ExecuteJOE_Test1( View     ViewToWindow )
		{
		   zVIEW    mSPLDef = new zVIEW( );
		   //:VIEW mSPLDef2 BASED ON LOD mSPLDef
		   zVIEW    mSPLDef2 = new zVIEW( );
		   int      RESULT = 0;
		   int      lTempInteger_0 = 0;


		   //:// Execute Tests to Check for JOE Bugs.

		   //:// TEST 1
		   //:// Recursive Subobject Test.
		   //:// The current error is that the basic SetViewToSubobject function did not change the view to the subobject.

		   //:// Create subobject with one level of recursive subobject.
		   //:ACTIVATE mSPLDef EMPTY
		   RESULT = ActivateEmptyObjectInstance( mSPLDef, "mSPLDef", ViewToWindow, zSINGLE );
		   //:NAME VIEW mSPLDef "mSPLDef"
		   SetNameForView( mSPLDef, "mSPLDef", null, zLEVEL_TASK );
		   //:CREATE ENTITY mSPLDef.SubregPhysicalLabelDef
		   RESULT = CreateEntity( mSPLDef, "SubregPhysicalLabelDef", zPOS_AFTER );
		   //:CREATE ENTITY mSPLDef.SPLD_LLD
		   RESULT = CreateEntity( mSPLDef, "SPLD_LLD", zPOS_AFTER );
		   //:mSPLDef.SPLD_LLD.Name = "Test"
		   SetAttributeFromString( mSPLDef, "SPLD_LLD", "Name", "Test" );
		   //:CREATE ENTITY mSPLDef.LLD_Page
		   RESULT = CreateEntity( mSPLDef, "LLD_Page", zPOS_AFTER );
		   //:mSPLDef.LLD_Page.Width = 10
		   SetAttributeFromInteger( mSPLDef, "LLD_Page", "Width", 10 );
		   //:CREATE ENTITY mSPLDef.LLD_Panel
		   RESULT = CreateEntity( mSPLDef, "LLD_Panel", zPOS_AFTER );
		   //:mSPLDef.LLD_Panel.Width = 11
		   SetAttributeFromInteger( mSPLDef, "LLD_Panel", "Width", 11 );

		   //:CREATE ENTITY mSPLDef.LLD_Block
		   RESULT = CreateEntity( mSPLDef, "LLD_Block", zPOS_AFTER );
		   //:mSPLDef.LLD_Block.Name = "Block Level 1"
		   SetAttributeFromString( mSPLDef, "LLD_Block", "Name", "Block Level 1" );
		   //:CREATE ENTITY mSPLDef.LLD_SpecialSectionAttribute
		   RESULT = CreateEntity( mSPLDef, "LLD_SpecialSectionAttribute", zPOS_AFTER );
		   //:mSPLDef.LLD_SpecialSectionAttribute.Name = "Spec Attribute 1"
		   SetAttributeFromString( mSPLDef, "LLD_SpecialSectionAttribute", "Name", "Spec Attribute 1" );

		   //:SetViewToSubobject( mSPLDef, "LLD_SubBlock" )
		   SetViewToSubobject( mSPLDef, "LLD_SubBlock" );

		   //:IF mSPLDef.LLD_Block EXISTS
           Assert.assertEquals( "SetViewToSubobject LLD_SubBlock didn't work.", -3, CheckExistenceOfEntity( mSPLDef, "LLD_Block" ) );
           /*
		   lTempInteger_0 = CheckExistenceOfEntity( mSPLDef, "LLD_Block" );
		   if ( lTempInteger_0 == 0 )
           */
		   //:END

		   //:CREATE ENTITY mSPLDef.LLD_Block
		   RESULT = CreateEntity( mSPLDef, "LLD_Block", zPOS_AFTER );
		   //:mSPLDef.LLD_Block.Name = "Block Level 2"
		   SetAttributeFromString( mSPLDef, "LLD_Block", "Name", "Block Level 2" );
		   //:CREATE ENTITY mSPLDef.LLD_SpecialSectionAttribute
		   RESULT = CreateEntity( mSPLDef, "LLD_SpecialSectionAttribute", zPOS_AFTER );
		   //:mSPLDef.LLD_SpecialSectionAttribute.Name = "Spec Attribute 2"
		   SetAttributeFromString( mSPLDef, "LLD_SpecialSectionAttribute", "Name", "Spec Attribute 2" );

		   //:ResetViewFromSubobject( mSPLDef )
		   ResetViewFromSubobject( mSPLDef );

           Assert.assertEquals( "No match on Block Level 1", 0, CompareAttributeToString( mSPLDef, "LLD_Block", "Name", "Block Level 1" ) );
		   //:IF mSPLDef.LLD_SpecialSectionAttribute.Name != "Spec Attribute 1"
           Assert.assertEquals( "No match on Spec Attribute 1", 0, CompareAttributeToString( mSPLDef, "LLD_SpecialSectionAttribute", "Name", "Spec Attribute 1" ) );

           //:SetViewToSubobject( mSPLDef, "LLD_SubBlock" )
		   SetViewToSubobject( mSPLDef, "LLD_SubBlock" );
		   //:IF mSPLDef.LLD_Block.Name != "Block Level 2"
           Assert.assertEquals( "No match on Block Level 2", 0, CompareAttributeToString( mSPLDef, "LLD_Block", "Name", "Block Level 2" ) );
		   //:IF mSPLDef.LLD_SpecialSectionAttribute.Name != "Spec Attribute 2"
           Assert.assertEquals( "No match on Spec Attribute 2", 0, CompareAttributeToString( mSPLDef, "LLD_SpecialSectionAttribute", "Name", "Spec Attribute 2" ) );

           //:ResetViewFromSubobject( mSPLDef )
		   ResetViewFromSubobject( mSPLDef );

		   //:// Now try the subobject from a different view.
		   //:CreateViewFromView( mSPLDef2, mSPLDef )
		   CreateViewFromView( mSPLDef2, mSPLDef );

		   //:SetViewToSubobject( mSPLDef2, "LLD_SubBlock" )
		   SetViewToSubobject( mSPLDef2, "LLD_SubBlock" );
           Assert.assertEquals( "No match on Block Level 2", 0, CompareAttributeToString( mSPLDef2, "LLD_Block", "Name", "Block Level 2" ) );
		   //:IF mSPLDef2.LLD_SpecialSectionAttribute.Name != "Spec Attribute 2"
           Assert.assertEquals( "No match on Spec Attribute 2", 0, CompareAttributeToString( mSPLDef2, "LLD_SpecialSectionAttribute", "Name", "Spec Attribute 2" ) );

		   //:ResetViewFromSubobject( mSPLDef2 )
		   ResetViewFromSubobject( mSPLDef2 );

		   //:DropView( mSPLDef2 )
		   DropView( mSPLDef2 );

		   //:TraceLineS( "*** JOE Test 1 successfully completed", "" )
		   TraceLineS( "*** JOE Test 1 successfully completed", "" );
		   return( 0 );
		//
//		    // Recursive code that didn't quite work.
//		    /*FOR EACH mSPLDef.BlockSubBlockComponent
//		       IF mSPLDef.BlockSubBlockComponent.Type = "Block"
//		          CREATE ENTITY mSPLDef.LLD_Block
//		          SetMatchingAttributesByName( mSPLDef, "LLD_Block", mSPLDef, "BlockSubBlockComponent", zSET_NULL )
//		       ELSE
//		          IF mSPLDef.BlockSubBlockComponent.Type = "SubBlock"
//		             CREATE ENTITY mSPLDef.LLD_SubBlock
//		             CreateViewFromView( mSPLDef2, mSPLDef )
//		             SetViewToSubobject( mSPLDef2, "LLD_SubBlock" )
//		             SetMatchingAttributesByName( mSPLDef2, "LLD_Block", mSPLDef, "BlockSubBlockComponent", zSET_NULL )
//		             ResetView( mSPLDef2 )
//		             DropView( mSPLDef2 )
//		          ELSE
//		             TraceLineS( "#### No Match on Block Type", "" )
//		          END
//		       END
//		    END*/
		// END
		}


		public int
		ExecuteJOE_TestSubobjectCheckExistence( View     ViewToWindow )
		{
			   zVIEW    mSPLDef = new zVIEW( );
			   int      RESULT = 0;
			   int      lTempInteger_0 = 0;


			   //://ACTIVATE mSPLDef SingleForUpdate WHERE mSPLDef.SubregPhysicalLabelDef.ID = 5
			   //://NAME VIEW mSPLDef "mSPLDef"
			   //://CommitOI_ToFile( mSPLDef, "c:\temp\SPLD.por", zASCII )
			   //:ActivateOI_FromFile( mSPLDef, "mSPLDef", ViewToWindow, "c:\temp\SPLD.por", zSINGLE )
			   // mSPLDefO is the new version of mSPLDef.
			   ActivateOI_FromFile( mSPLDef, "mSPLDefO", ViewToWindow, zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/JOE_Test3.por", zSINGLE );
			   //:NAME VIEW mSPLDef "mSPLDef"
			   SetNameForView( mSPLDef, "mSPLDef", null, zLEVEL_TASK );
			   //:SET CURSOR NEXT mSPLDef.LLD_Block
			   RESULT = SetCursorNextEntity( mSPLDef, "LLD_Block", "" );
			   //:SetViewToSubobject( mSPLDef, "LLD_SubBlock" )
			   SetViewToSubobject( mSPLDef, "LLD_SubBlock" );
			   mSPLDef.logObjectInstance();
			   //:IF mSPLDef.ContinuationStatement EXISTS
               lTempInteger_0 = CheckExistenceOfEntity( mSPLDef, "ContinuationStatement" );
               Assert.assertEquals( "Unexpected RC", CursorResult.UNDEFINED.toInt(),  lTempInteger_0 );
			   lTempInteger_0 = CheckExistenceOfEntity( mSPLDef, "LLD_SubBlock" );
               Assert.assertEquals( "Unexpected RC", CursorResult.NULL.toInt(),  lTempInteger_0 );

		   return 0;
		}


		public int
		ExecuteJOE_TestSubobjectCreateView( View     ViewToWindow )
		{
		   zVIEW    mSPLDef = new zVIEW( );
		   //:VIEW mSPLDef2 BASED ON LOD mSPLDef
		   zVIEW    mSPLDef2 = new zVIEW( );
		   int      RESULT = 0;
		   int      lTempInteger_0 = 0;


		   //:// Execute Tests to Check for JOE Bugs.

		   // Recursive Subobject Test.
		   // After creating the subobject entity, we get an error when calling the a newView() from the subobject view.
		   // The error occurs in EntityCursorImpl line 159 and I have to say I am confused as to why in EntityCursorImpl the
		   // retrieved parentInstance isn't the same in both the cases of newView() in this code.

		   //:// Create subobject with one level of recursive subobject.
		   //:ACTIVATE mSPLDef EMPTY
		   RESULT = ActivateEmptyObjectInstance( mSPLDef, "mSPLDef", ViewToWindow, zSINGLE );
		   //:NAME VIEW mSPLDef "mSPLDef"
		   SetNameForView( mSPLDef, "mSPLDef", null, zLEVEL_TASK );
		   //:CREATE ENTITY mSPLDef.SubregPhysicalLabelDef
		   RESULT = CreateEntity( mSPLDef, "SubregPhysicalLabelDef", zPOS_AFTER );
		   //:CREATE ENTITY mSPLDef.SPLD_LLD
		   RESULT = CreateEntity( mSPLDef, "SPLD_LLD", zPOS_AFTER );
		   //:mSPLDef.SPLD_LLD.Name = "Test"
		   SetAttributeFromString( mSPLDef, "SPLD_LLD", "Name", "Test" );
		   //:CREATE ENTITY mSPLDef.LLD_Page
		   RESULT = CreateEntity( mSPLDef, "LLD_Page", zPOS_AFTER );
		   //:mSPLDef.LLD_Page.Width = 10
		   SetAttributeFromInteger( mSPLDef, "LLD_Page", "Width", 10 );
		   //:CREATE ENTITY mSPLDef.LLD_Panel
		   RESULT = CreateEntity( mSPLDef, "LLD_Panel", zPOS_AFTER );
		   //:mSPLDef.LLD_Panel.Width = 11
		   SetAttributeFromInteger( mSPLDef, "LLD_Panel", "Width", 11 );

		   //:CREATE ENTITY mSPLDef.LLD_Block
		   RESULT = CreateEntity( mSPLDef, "LLD_Block", zPOS_AFTER );
		   //:mSPLDef.LLD_Block.Name = "Block Level 1"
		   SetAttributeFromString( mSPLDef, "LLD_Block", "Name", "Block Level 1" );
		   //:CREATE ENTITY mSPLDef.LLD_SpecialSectionAttribute
		   RESULT = CreateEntity( mSPLDef, "LLD_SpecialSectionAttribute", zPOS_AFTER );
		   //:mSPLDef.LLD_SpecialSectionAttribute.Name = "Spec Attribute 1"
		   SetAttributeFromString( mSPLDef, "LLD_SpecialSectionAttribute", "Name", "Spec Attribute 1" );

		   //:SetViewToSubobject( mSPLDef, "LLD_SubBlock" )
		   SetViewToSubobject( mSPLDef, "LLD_SubBlock" );

		   //zVIEW vGrid2 = new zVIEW( );
		   //CreateViewFromView( vGrid2, mSPLDef );
		   View vGrid2;
		   vGrid2 = mSPLDef.newView( );

		   DropView( vGrid2 );

		   RESULT = CreateEntity( mSPLDef, "LLD_Block", zPOS_AFTER );
		   //:mSPLDef.LLD_Block.Name = "Block Level 2"
		   SetAttributeFromString( mSPLDef, "LLD_Block", "Name", "Block Level 2" );
		   //:CREATE ENTITY mSPLDef.LLD_SpecialSectionAttribute
		   RESULT = CreateEntity( mSPLDef, "LLD_SpecialSectionAttribute", zPOS_AFTER );
		   //:mSPLDef.LLD_SpecialSectionAttribute.Name = "Spec Attribute 2"
		   SetAttributeFromString( mSPLDef, "LLD_SpecialSectionAttribute", "Name", "Spec Attribute 2" );

		   /*
		   ResetViewFromSubobject( mSPLDef );
		   SetViewToSubobject( mSPLDef, "LLD_SubBlock" );
		   ResetViewFromSubobject( mSPLDef );
		   SetViewToSubobject( mSPLDef, "LLD_SubBlock" );
		   */
		   vGrid2 = mSPLDef.newView( );
		   //CreateViewFromView( mSPLDef2, mSPLDef );
		   String str = GetStringFromAttribute( vGrid2, "LLD_SpecialSectionAttribute", "Name" );
		   DropView( vGrid2 );

		   return( 0 );
		}

		//:   VIEW mSPLDef  BASED ON LOD mSPLDef
		public int
		ExecuteJOE_Test2( View     ViewToWindow )
		{
		   zVIEW    mSPLDef = new zVIEW( );
		   //:VIEW mSPLDef2 BASED ON LOD mSPLDef
		   zVIEW    mSPLDef2 = new zVIEW( );
		   int      RESULT = 0;
		   int      lTempInteger_0 = 0;

         if ( RESULT == 0 )
            return 0;
		   //:// Execute Tests to Check for JOE Bugs.

		   //:// TEST 2
		   //:// Test of Entity created in one view is not showing second view.
		   //:// This only happens when the object is activated from the database and changed.

		   //:// Activate the basic object.
		   //:ActivateOI_FromFile( mSPLDef, "mSPLDef", ViewToWindow, "c:\temp\JOE_Test2.por", zSINGLE )
		   ActivateOI_FromFile( mSPLDef, "mSPLDef", ViewToWindow, zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/JOE_Test2.por", zSINGLE );
		   //:NAME VIEW mSPLDef "mSPLDef"
		   SetNameForView( mSPLDef, "mSPLDef", null, zLEVEL_TASK );

		   //:// First, do a simple create and see if entity is seen in other view.

		   //:SET CURSOR LAST mSPLDef.LLD_Panel
		   RESULT = SetCursorLastEntity( mSPLDef, "LLD_Panel", "" );
		   //:CreateViewFromView( mSPLDef2, mSPLDef )
		   CreateViewFromView( mSPLDef2, mSPLDef );
		   //:NAME VIEW mSPLDef2 "mSPLDef2"
		   SetNameForView( mSPLDef2, "mSPLDef2", null, zLEVEL_TASK );

		   //:CREATE ENTITY mSPLDef.ContinuationStatement
		   RESULT = CreateEntity( mSPLDef, "ContinuationStatement", zPOS_AFTER );
		   //:mSPLDef.ContinuationStatement.Title = "Title 1"
		   SetAttributeFromString( mSPLDef, "ContinuationStatement", "Title", "Title 1" );
		   //:mSPLDef.ContinuationStatement.Text  = "Text 1"
		   SetAttributeFromString( mSPLDef, "ContinuationStatement", "Text", "Text 1" );

		   //:IF mSPLDef2.ContinuationStatement DOES NOT EXIST
		   lTempInteger_0 = CheckExistenceOfEntity( mSPLDef2, "ContinuationStatement" );
           Assert.assertEquals( "ContinuationStatement doesn't exist 1", 0, CheckExistenceOfEntity( mSPLDef2, "ContinuationStatement" ) );

		   //:// Next, do the same test except that the entity is first deleted from the primary view.

		   //:DELETE ENTITY mSPLDef.ContinuationStatement NONE
		   RESULT = DeleteEntity( mSPLDef, "ContinuationStatement", zREPOS_NONE );

		   //:CREATE ENTITY mSPLDef.ContinuationStatement
		   RESULT = CreateEntity( mSPLDef, "ContinuationStatement", zPOS_AFTER );
		   //:mSPLDef.ContinuationStatement.Title = "Title 1"
		   SetAttributeFromString( mSPLDef, "ContinuationStatement", "Title", "Title 1" );
		   //:mSPLDef.ContinuationStatement.Text  = "Text 1"
		   SetAttributeFromString( mSPLDef, "ContinuationStatement", "Text", "Text 1" );

		   RESULT = SetCursorFirstEntity( mSPLDef2, "ContinuationStatement", "" );
           Assert.assertEquals( "ContinuationStatement doesn't exist 2", 0, RESULT );
           /*
		   if ( RESULT < zCURSOR_SET )
           */

		   //:TraceLineS( "*** JOE Test 2 successfully completed", "" )
		   TraceLineS( "*** JOE Test 2 successfully completed", "" );
		   return( 0 );
		// END
		}

		//:   VIEW mSPLDef  BASED ON LOD mSPLDef
		public int
		ExecuteJOE_TemporalDeleteError( View     ViewToWindow )
		{
		   zVIEW    mSPLDef = new zVIEW( );
		   //:VIEW mSPLDef2 BASED ON LOD mSPLDef
		   zVIEW    mSPLDef2 = new zVIEW( );
		   int      RESULT = 0;
		   int      lTempInteger_0 = 0;
         int      SaveID = 0;
         if ( RESULT == 0 )
            return 0;

           // A temporal subobject is created for LLD_Page. Then a subentity LLD_Panel is deleted.
           // After the CancelSubobject LLD_Page, the LLD_Panel should exist again, but it does not.

		   //:// Activate the basic object.
		   //:ActivateOI_FromFile( mSPLDef, "mSPLDef", ViewToWindow, "c:\temp\JOE_Test2.por", zSINGLE )
		   ActivateOI_FromFile( mSPLDef, "mSPLDef", ViewToWindow, zeidonSystem.getObjectEngine().getHomeDirectory() + "/ePammsDon/JOE_Test2.por", zSINGLE );
		   //:NAME VIEW mSPLDef "mSPLDef"
		   SetNameForView( mSPLDef, "mSPLDef", null, zLEVEL_TASK );

           // Dad's new test. After deleting the entity from a Temporal view, if the
           // parent TemporalSubobject gets cancelled, then the deleted entity should be back.
           //:// First CancelSubobject test after delete of a subentity.

           CreateTemporalSubobjectVersion( mSPLDef, "LLD_Page" );

           RESULT = SetCursorFirstEntity( mSPLDef, "LLD_Panel", "" );

           //:SaveID = mSPLDef.LLD_Panel.ID
           {MutableInt mi_SaveID = new MutableInt( SaveID );
               GetIntegerFromAttribute( mi_SaveID, mSPLDef, "LLD_Panel", "ID" );
           SaveID = mi_SaveID.intValue( );}

           //:DELETE ENTITY mSPLDef.LLD_Panel
           EntityInstance ei = mSPLDef.cursor( "LLD_Panel" ).getEntityInstance();
           EntityInstance page = mSPLDef.cursor( "LLD_Page" ).getEntityInstance();
           RESULT = DeleteEntity( mSPLDef, "LLD_Panel", zPOS_NEXT );

           //:CancelSubobject( mSPLDef, "LLD_Page" )
           CancelSubobject( mSPLDef, "LLD_Page" );

           //:SET CURSOR FIRST mSPLDef.LLD_Panel WHERE mSPLDef.LLD_Panel.ID = SaveID
           RESULT = SetCursorFirstEntityByInteger( mSPLDef, "LLD_Panel", "ID", SaveID, "" );
           Assert.assertEquals( "Can't find LLD_Panel but it should exist after CancelSubobject.", 0, RESULT );

           //:TraceLineS( "$$$$ Cancel of deleted Panel worked", "" )
           TraceLineS( "$$$$ Cancel of deleted Panel worked", "" );

           return( 0 );
 		}
   }
}
