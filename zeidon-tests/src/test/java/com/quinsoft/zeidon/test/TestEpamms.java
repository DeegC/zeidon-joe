/**
 *
 */
package com.quinsoft.zeidon.test;

import junit.framework.Assert;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Before;
import org.junit.Test;

import com.quinsoft.zeidon.ActivateFlags;
import com.quinsoft.zeidon.CursorPosition;
import com.quinsoft.zeidon.CursorResult;
import com.quinsoft.zeidon.EntityCursor;
import com.quinsoft.zeidon.EntityInstance;
import com.quinsoft.zeidon.ObjectEngine;
import com.quinsoft.zeidon.Task;
import com.quinsoft.zeidon.View;
import com.quinsoft.zeidon.ZeidonException;
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
	public void ExecuteJOE_TestGetRecursiveSubEntities()
	{
	   View      mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestGetRecursiveSubEntities( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestGetRecursiveSubEntities ========");
	}

   @Test
	public void ExecuteJOE_TestMismatch()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestMismatch( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestMismatch ========");
	}

   @Test
	public void ExecuteJOE_TestGetRecursiveDelete()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestGetRecursiveDelete( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestGetRecursiveDelete ========");
	}

   @Test
	public void ExecuteJOE_TestCreateChildOnSubobject()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestCreateChildOnSubobject( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestCreateChildOnSubobject ========");
	}

   @Test
	public void ExecuteJOE_TestBB()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestBB( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestBB ========");
	}

   @Test
	public void ExecuteJOE_TestSetCursorWithAttributeContainingBlank()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestSetCursorWithAttributeContainingBlank( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestSetCursorWithAttributeContainingBlank ========");
	}

   @Test
	public void ExecuteJOE_TestBBB()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestBBB( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestBBB ========");
	}

	public void ExecuteJOE_TestB()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestB( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestB ========");
	}

   @Test
	public void ExecuteJOE_TestAA()
	{
	   View         mSPLDef;

		mSPLDef = ePamms.activateEmptyObjectInstance( "mSPLDef" );
		VmlTester tester = new VmlTester( mSPLDef );
		tester.ExecuteJOE_TestAA( mSPLDef );
      System.out.println("===== Finished ExecuteJOE_TestAA ========");
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


      private int
      o_fnLocalBuildQual_12( View     vSubtask,
                             zVIEW    vQualObject,
                             int      lTempInteger_0 )
      {
         int      RESULT = 0;

      // RESULT = SfActivateSysEmptyOI( vQualObject, "KZDBHQUA", vSubtask, 256 /*zMULTIPLE*/ );
         View view = vSubtask.activateEmptyObjectInstance( "KZDBHQUA", task.getSystemTask().getApplication() );
      // CreateEntity( vQualObject, "EntitySpec", zPOS_AFTER );
         view.cursor( "EntitySpec" ).createEntity( CursorPosition.NEXT );
      // SetAttributeFromString( vQualObject, "EntitySpec", "EntityName", "SubregPhysicalLabelDef" );
         view.cursor( "EntitySpec" ).getAttribute( "EntityName" ).setValue( "SubregPhysicalLabelDef" );
      // CreateEntity( vQualObject, "QualAttrib", zPOS_AFTER );
         view.cursor( "QualAttrib" ).createEntity( CursorPosition.NEXT );
      // SetAttributeFromString( vQualObject, "QualAttrib", "EntityName", "SubregPhysicalLabelDef" );
         view.cursor( "QualAttrib" ).getAttribute( "EntityName" ).setValue( "SubregPhysicalLabelDef" );
      // SetAttributeFromString( vQualObject, "QualAttrib", "AttributeName", "ID" );
         view.cursor( "QualAttrib" ).getAttribute( "AttributeName" ).setValue( "ID" );
      // SetAttributeFromInteger( vQualObject, "QualAttrib", "Value", lTempInteger_0 );
         view.cursor( "QualAttrib" ).getAttribute( "Value" ).setValue( lTempInteger_0 );
      // SetAttributeFromString( vQualObject, "QualAttrib", "Oper", "=" );
         view.cursor( "QualAttrib" ).getAttribute( "Oper" ).setValue( "=" );
         vQualObject.setView( view );
         return( 0 );
      }

      public void ExecuteJOE_TestGetRecursiveSubEntities( View view ) {

         //:VIEW mSPLDef  BASED ON LOD  mSPLDef
      // zVIEW    mSPLDef = new zVIEW( );
         int      lTempInteger_0 = 0;
         zVIEW    vTempViewVar_0 = new zVIEW( );

         //:// Activate the mSPLDef object selected in mSubProd.
         //:ACTIVATE mSPLDef WHERE mSPLDef.SubregPhysicalLabelDef.ID = lSPLDLST.SubregPhysicalLabelDef.ID
      // {MutableInt mi_lTempInteger_0 = new MutableInt( lTempInteger_0 );
      //     GetIntegerFromAttribute( mi_lTempInteger_0, lSPLDLST, "SubregPhysicalLabelDef", "ID" );
      // lTempInteger_0 = mi_lTempInteger_0.intValue( );}
         lTempInteger_0 = 5;
         o_fnLocalBuildQual_12( view, vTempViewVar_0, lTempInteger_0 );
      // ActivateObjectInstance( mSPLDef, "mSPLDef", view, vTempViewVar_0, zSINGLE );
         View mSPLDef = view.activateObjectInstance( "mSPLDef", vTempViewVar_0.getView(), ActivateFlags.SINGLE );
      // DropView( vTempViewVar_0 );
         //:NAME VIEW mSPLDef "mSPLDef"
         SetNameForView( mSPLDef, "mSPLDef", null, zLEVEL_TASK );
         mSPLDef.logObjectInstance();
         EntityCursor ec = mSPLDef.getCursor( "LLD_Block" );
         CursorResult cr = ec.setFirstWithinOi( "ID", "622" );
         Assert.assertEquals( "LLD_Block 622 exists but is not found: ", CursorResult.SET, cr );
         ec = mSPLDef.getCursor( "LLD_SpecialSectionAttrBlock" );
         cr = ec.setFirst();
         Assert.assertEquals( "LLD_SpecialSectionAttrBlock exists but is not found: ", CursorResult.SET, cr );
     }

      public void ExecuteJOE_TestGetRecursiveDelete( View view ) {

         //:VIEW mSPLDef  BASED ON LOD  mSPLDef
      // zVIEW    mSPLDef = new zVIEW( );
         int      lTempInteger_0 = 0;
         zVIEW    vTempViewVar_0 = new zVIEW( );

         //:// Activate the mSPLDef object selected in mSubProd.
         //:ACTIVATE mSPLDef WHERE mSPLDef.SubregPhysicalLabelDef.ID = lSPLDLST.SubregPhysicalLabelDef.ID
      // {MutableInt mi_lTempInteger_0 = new MutableInt( lTempInteger_0 );
      //     GetIntegerFromAttribute( mi_lTempInteger_0, lSPLDLST, "SubregPhysicalLabelDef", "ID" );
      // lTempInteger_0 = mi_lTempInteger_0.intValue( );}
         lTempInteger_0 = 7;
         o_fnLocalBuildQual_12( view, vTempViewVar_0, lTempInteger_0 );
      // ActivateObjectInstance( mSPLDef, "mSPLDef", view, vTempViewVar_0, zSINGLE );
         View mSPLDef = view.activateObjectInstance( "mSPLDef", vTempViewVar_0.getView(), ActivateFlags.SINGLE );
      // DropView( vTempViewVar_0 );
         //:NAME VIEW mSPLDef "mSPLDef"
         SetNameForView( mSPLDef, "mSPLDef", null, zLEVEL_TASK );
         EntityCursor ec = mSPLDef.getCursor( "SubregPhysicalLabelDef" );
         ec.deleteEntity();
         mSPLDef.commit();
     }

      private void CheckAddKeywordEntry( View  mSPLDefBlock, String   szKeywordName )
      {
         EntityCursor ec = mSPLDefBlock.getCursor( "LLD_SpecialSectionAttribute");
         CursorResult cr = ec.setFirst( "Name", szKeywordName );
         if ( cr != CursorResult.SET ) {
            ec.createEntity( CursorPosition.LAST );
            ec.getAttribute( "Name" ).setValue( szKeywordName );
            ec = mSPLDefBlock.getCursor( "LLD_SpecialSectionAttrBlock" );
            ec.createEntity( CursorPosition.LAST );
         }
      }

		public int
		ExecuteJOE_TestCreateChildOnSubobject( View view )
		{
         CursorResult cr;
      // View    mSPLDefBlock = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDefBlock.json" ).activateFirst();
         View    mSPLDefBlock = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDef3.json" ).activateFirst();
      // mSPLDef.logObjectInstance();
         displaySPLD( mSPLDefBlock, "LLD_Page", "First Activate" );

      // EntityCursor ec1 = mSPLDef.getCursor( "LLD_SubBlock" );
         EntityCursor ec = mSPLDefBlock.getCursor( "LLD_SubBlock" );
         ec.setFirstWithinOi( "ID", "622" );
         ec.setToSubobject();
         ec = mSPLDefBlock.getCursor( "LLD_Block" );
         TraceLineS( "Logging LLD_Block: ", "622" );
         ec.logEntity( true );
         ec = mSPLDefBlock.getCursor( "LLD_SpecialSectionAttribute" );
      // ec.deleteAll();
         ec = mSPLDefBlock.getCursor( "LLD_Block" );
         ec.logEntity( true );

         CheckAddKeywordEntry( mSPLDefBlock, "Title" );
         CheckAddKeywordEntry( mSPLDefBlock, "Text" );
         CheckAddKeywordEntry( mSPLDefBlock, "Column List" );
         CheckAddKeywordEntry( mSPLDefBlock, "MARKETING Header" );
         ec.logEntity( true );

         cr = mSPLDefBlock.cursor( "LLD_SpecialSectionAttrBlock" ).checkExistenceOfEntity();
         Assert.assertEquals( "LLD_SpecialSectionAttrBlock exists but is not found: ", CursorResult.SET, cr );

         return 0;
      }

		public int
		ExecuteJOE_TestB( View     view )
		{
      // View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDefFull.json" ).activateFirst();
         View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDef3.json" ).activateFirst();
      // mSPLDef.logObjectInstance();
         displaySPLD( mSPLDef, "LLD_Page", "First Activate" );

         EntityCursor ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", "600" );
         View mSPLDef2 = mSPLDef.newView();
         EntityCursor ec2 = mSPLDef2.getCursor( "LLD_Panel" );
         ec2.setFirstWithinOi( "ID", "52" );
         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.moveSubobject( CursorPosition.LAST, ec1, CursorPosition.FIRST );
         displaySPLD( mSPLDef, "LLD_Page", "After moveSubobject" );

         ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", "600" );
         EntityInstance ei = ec1.getEntityInstance();
         EntityInstance eip = ei.getParent();
         String ID = eip.getAttribute( "ID" ).getString();
         TraceLineS( "Block 600's parent ID: ", ID );
         Assert.assertEquals( "Block 600's parent ID: ", "52", ID );

         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.setFirstWithinOi( "ID", "598" );
         ec2 = mSPLDef2.getCursor( "LLD_SubBlock" );
         ec2.setToSubobject();
         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.moveSubobject( CursorPosition.LAST, ec1, CursorPosition.FIRST );
         displaySPLD( mSPLDef, "LLD_Page", "After 2nd moveSubobject" );

         ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", "600" );
         ei = ec1.getEntityInstance();
         eip = ei.getParent();
         ID = eip.getAttribute( "ID" ).getString();
         TraceLineS( "Block 600's original parent ID: ", ID );
         Assert.assertEquals( "Block 600's parent ID: ", "598", ID );

         return 0;
      }

		public int
		ExecuteJOE_TestBB( View     view )
		{
      // View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDefFull.json" ).activateFirst();
         View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDef3.json" ).activateFirst();
      // mSPLDef.logObjectInstance();
         displaySPLD( mSPLDef, "LLD_Page", "First Activate" );

      //EntityCursor ec1 = mSPLDef.getCursor( "LLD_SubBlock" );
         EntityCursor ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", "600" );
      // ec1.setToSubobject();
      // ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.logEntity( false );

         View mSPLDef2 = mSPLDef.newView();
         EntityCursor ec2 = mSPLDef2.getCursor( "LLD_Panel" );
         ec2.setFirstWithinOi( "ID", "52" );
         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.moveSubobject( CursorPosition.FIRST, ec1, CursorPosition.FIRST );

         ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", "600" );
         EntityInstance ei = ec1.getEntityInstance();
         EntityInstance eip = ei.getParent();
         String ID = eip.getAttribute( "ID" ).getString();
         TraceLineS( "Block 600's parent ID: ", ID );
         Assert.assertEquals( "Block 600's parent ID: ", "52", ID );

         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.setFirstWithinOi( "ID", "598" );
         ec2 = mSPLDef2.getCursor( "LLD_SubBlock" );
         ec2.setToSubobject();
         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.moveSubobject( CursorPosition.FIRST, ec1, CursorPosition.FIRST );
         displaySPLD( mSPLDef, "LLD_Page", "After 2nd moveSubobject" );

         ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", "600" );
         ei = ec1.getEntityInstance();
         eip = ei.getParent();
         ID = eip.getAttribute( "ID" ).getString();
         TraceLineS( "Block 600's original parent ID: ", ID );
         Assert.assertEquals( "Block 600's parent ID: ", "598", ID );

         return 0;
      }

		public int
		ExecuteJOE_TestSetCursorWithAttributeContainingBlank( View     view )
		{
         String ID = "813";
         View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDefMason.json" ).activateFirst();
      // mSPLDef.logObjectInstance();
         displaySPLD( mSPLDef, null, "First Activate" );

      //EntityCursor ec1 = mSPLDef.getCursor( "LLD_SubBlock" );
         EntityCursor ec = mSPLDef.getCursor( "LLD_Block" );
         ec.setFirstWithinOi( "ID", ID );
         displaySPLD( mSPLDef, "LLD_Block", "After setFirstWithinOi for LLD_Block: " + ID );

         EntityInstance ei = ec.getEntityInstance();
         TraceLineS( "Logging LLD_Block: ", ID );
         ei.logEntity();

         ec = mSPLDef.getCursor( "LLD_SpecialSectionAttribute" );
         CursorResult cr = ec.setFirst();
         Assert.assertEquals( "Cursor Result setFirst", CursorResult.SET, cr );

      // SetCursorFirstEntityByString( mSPLDef, "LLD_SpecialSectionAttribute", "Name", szKeywordName, "" );
         cr = ec.setFirst( "Name", "Hazards Warning", "" );
         Assert.assertEquals( "Cursor Result looking for Name: Hazards Warning", CursorResult.SET, cr );
         cr = ec.setFirst( "Name", "Hazards Signal Word", "" );
         Assert.assertEquals( "Cursor Result looking for Name: Hazards Signal Word", CursorResult.SET, cr );
         cr = ec.setFirst( "Name", "Hazards Precautionary", "" );
         Assert.assertEquals( "Cursor Result looking for Name: Hazards Precautionary", CursorResult.SET, cr );

         return 0;
      }

		public int
		ExecuteJOE_TestBBB( View     view )
		{
         String ID = "812";
         View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir( "mSPLDefMason.json" ).activateFirst();
      // mSPLDef.logObjectInstance();
         displaySPLD( mSPLDef, "LLD_Page", "First Activate" );

      //EntityCursor ec1 = mSPLDef.getCursor( "LLD_SubBlock" );
         EntityCursor ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", ID );
         EntityInstance ei = ec1.getEntityInstance();
         ei.getAttribute( "Top" ).setValue( "777.0" );
         ei.getAttribute( "wPE" ).setValue( "panel" );
         ei.getAttribute( "wPID" ).setValue( "64" );
         ei.getAttribute( "wE" ).setValue( "block" );
         ei.getAttribute( "wID" ).setValue( ID );
         EntityCursor ec2 = mSPLDef.getCursor( "LLD_SubBlock" );
         ec2.setFirst( "ID", "813" );
         ei = ec2.getEntityInstance();
         ei.getAttribute( "Top" ).setValue( "888.0" );
         ei.getAttribute( "wPE" ).setValue( "block" );
         ei.getAttribute( "wPID" ).setValue( "812" );
         ei.getAttribute( "wE" ).setValue( "block" );
         ei.getAttribute( "wID" ).setValue( "813" );

         ec1.logEntity( true );

         View mSPLDef2 = mSPLDef.newView();
         ec2 = mSPLDef2.getCursor( "LLD_Panel" );
         ec2.setFirstWithinOi( "ID", "64" );
         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         displaySPLD( mSPLDef, null, "Before moveSubobject" );
         ec2.moveSubobject( CursorPosition.FIRST, ec1, CursorPosition.FIRST );
         mSPLDef2.drop();
         displaySPLD( mSPLDef, null, "After moveSubobject" );

         // Now check for the attributs that were set on the OI.
         mSPLDef2 = mSPLDef.newView();
         ec1 = mSPLDef.getCursor( "LLD_Block" );
         CursorResult cr = ec1.setFirstWithinOi( "ID", ID );
         Assert.assertEquals( "Cursor Result looking for ID: " + ID, cr, CursorResult.SET );

         ei = ec1.getEntityInstance();  // this is the entity that we are moving (812 in test)
         String wPID = ec1.getAttribute( "wPID" ).getString();
         String top = ec1.getAttribute( "Top" ).getString();
         EntityInstance eip = null;
         eip = ei.getParent();
         String IDP = eip.getAttribute( "ID" ).getString();  // get ID of parent
         Assert.assertEquals( "Permanent attribute 'Top' are not the same", "777.0", top );
         Assert.assertEquals( "Work attribute Parent ID's are not the same", IDP, wPID );

         return 0;
      }

		public int
		ExecuteJOE_TestAA( View     view )
		{
         View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDefAA.json" ).activateFirst();
      // mSPLDef.logObjectInstance();
         displaySPLD( mSPLDef, "LLD_Page", "First Activate" );

         EntityCursor ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", "805" );
         View mSPLDef2 = mSPLDef.newView();
         EntityCursor ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.setFirstWithinOi( "ID", "808" );
         ec2 = mSPLDef2.getCursor( "LLD_SubBlock" );
         ec2.setToSubobject();
         ec2 = mSPLDef2.getCursor( "LLD_Block" );
         ec2.moveSubobject( CursorPosition.FIRST, ec1, CursorPosition.FIRST );
      // mSPLDef.logObjectInstance();
         ec1 = mSPLDef.getCursor( "LLD_Block" );
         ec1.setFirstWithinOi( "ID", "805" );
         EntityInstance eip = ec1.getParent();
         Assert.assertEquals( "Parent ID's do not match up", "808", eip.getAttribute( "ID" ).getString() );

         return 0;
      }

      public int
		ExecuteJOE_Test000( View     view ) {
         View mSPLDef = view.activateOiFromFile( "mSPLDef", "target/test-classes/testdata//ePammsDon/mSPLDefGood.json", null );
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
//       CommitOI_ToFile( mSPLDef2, "target/test-classes/testdata//ePammsDon/mSPLDef2.json", zASCII );
//       mSPLDef = ePamms.activateOiFromFile( "mSPLDef", "target/test-classes/testdata//ePammsDon/mSPLDef2.json", null );
//       displaySPLD( mSPLDef, "SPLD_LLD", "After Activate mSPLDef2" );

         // Try to move the SubBlock back to a panel.
         mSPLDef2.resetSubobjectTop();
         displaySPLD( mSPLDef2, "SPLD_LLD", "After Activate mSPLDef2" );
         CursorResult cr = mSPLDef2.getCursor( "LLD_Panel" ).setFirst();

         mSPLDef = mSPLDef2.newView();
         mSPLDef.resetSubobjectTop();
         displaySPLD( mSPLDef, "SPLD_LLD", "After Activate/Reset New mSPLDef" );
         cr = mSPLDef.cursor( "LLD_Page" ).setFirst();
         displaySPLD( mSPLDef, "LLD_Page", "After LLD_Page setFirst" );
         cr = mSPLDef.cursor( "LLD_Panel" ).setFirst();
         displaySPLD( mSPLDef, "LLD_Panel", "After LLD_Panel setFirst" );
         cr = mSPLDef.cursor( "LLD_Block" ).setFirst();
         displaySPLD( mSPLDef, "LLD_Block", "After LLD_Block setFirst" );
         cr = mSPLDef.cursor( "LLD_SubBlock" ).setFirst();
         displaySPLD( mSPLDef, "LLD_SubBlock", "After LLD_SubBlock setFirst" );

         ec = mSPLDef.getCursor( "LLD_SubBlock" );
         TraceLineS( "Logging LLD_SubBlock", "" );
         ec.logEntity();
         ec.setToSubobject();
         ec = mSPLDef.getCursor( "LLD_Block" );
         if ( ec.isNull() )
             throw new ZeidonException( "This shouldn't be null" );

         mSPLDef2.cursor( "LLD_Block" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
         displaySPLD( mSPLDef2, "SPLD_LLD", "After second moveSubobject" );

         return 0;
      }

		public int
		ExecuteJOE_Test00( View     view )
		{
      // View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDefGood.json" ).activateFirst();
         View    mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDef3.json" ).activateFirst();
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
         CommitOI_ToFile( mSPLDef2, "target/test-classes/testdata//ePammsDon/mSPLDefX.json", zASCII );
      // mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDef2.json" ).activateFirst();
         mSPLDef = view.deserializeOi().setLodDef( "mSPLDef" ).fromApplicationDir(  "mSPLDef3.json" ).activateFirst();
         displaySPLD( mSPLDef, "SPLD_LLD", "After Activate mSPLDef2" );

         // Try to move the SubBlock back to a panel.
         mSPLDef.resetSubobjectTop();

         mSPLDef2 = mSPLDef.newView();
         mSPLDef2.getCursor( "LLD_Panel" ).setFirst();

         ec = mSPLDef.getCursor( "LLD_SubBlock" );
         ec.setToSubobject();
         ec = mSPLDef.getCursor( "LLD_Block" );

         mSPLDef2.cursor( "LLD_Block" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );

         displaySPLD( mSPLDef2, "SPLD_LLD", "After second moveSubobject" );

         return 0;
      }

		public int
		ExecuteJOE_Test0( View     mLLD )
		{

         //    A             A               A
         //   / \    ==>     |      ==>     / \
         //  B1 B2           B2            B2 B1
         //                  |
         //                B1Sub

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
      // mLLD.logObjectInstance();
         ec.setFirst();

         View mLLD2 = mLLD.newView();
         mLLD2.getCursor( "LLD_Block" ).setLast();
         mLLD2.getCursor( "LLD_SubBlock" ).setToSubobject();
         mLLD2.cursor( "LLD_Block" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
      // mLLD.logObjectInstance();

         mLLD.resetSubobjectTop();
         mLLD2.resetSubobjectTop();

         ec = mLLD2.getCursor( "LLD_SubBlock" );
         ec.logEntity();
         ec.setToSubobject();
         ec = mLLD2.getCursor( "LLD_Block" );
         ec.logEntity();
         mLLD.cursor( "LLD_Panel" ).logEntity();
         mLLD.cursor( "LLD_Block" ).moveSubobject( CursorPosition.FIRST, ec, CursorPosition.FIRST );
      // mLLD.logObjectInstance();
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
			   ActivateOI_FromFile( mSPLDef, "mSPLDefO", ViewToWindow, "target/test-classes/testdata//ePammsDon/JOE_Test3.por", zSINGLE );
			   //:NAME VIEW mSPLDef "mSPLDef"
			   SetNameForView( mSPLDef, "mSPLDef", null, zLEVEL_TASK );
			   //:SET CURSOR NEXT mSPLDef.LLD_Block
			   RESULT = SetCursorNextEntity( mSPLDef, "LLD_Block", "" );
			   //:SetViewToSubobject( mSPLDef, "LLD_SubBlock" )
			   SetViewToSubobject( mSPLDef, "LLD_SubBlock" );
			// mSPLDef.logObjectInstance();
			   //:IF mSPLDef.ContinuationStatement EXISTS
               lTempInteger_0 = CheckExistenceOfEntity( mSPLDef, "ContinuationStatement" );
               Assert.assertEquals( "Unexpected RC", CursorResult.UNDEFINED.toInt(),  lTempInteger_0 );
			   lTempInteger_0 = CheckExistenceOfEntity( mSPLDef, "LLD_SubBlock" );
               Assert.assertEquals( "Unexpected RC", CursorResult.NULL.toInt(),  lTempInteger_0 );
               mSPLDef.cursor( "LLD_Block" ).setNextWithinOi();
               mSPLDef.cursor( "LLD_Block" ).setPrevWithinOi();

               Integer id = mSPLDef.cursor( "LLD_Block" ).getAttribute( "ID" ).getInteger();
               mSPLDef.cursor( "LLD_SubBlock" ).setToSubobject();
               mSPLDef.cursor( "LLD_Block" ).resetSubobjectToParent();
               Assert.assertEquals( "IDs don't match after resetToParent", mSPLDef.cursor( "LLD_Block" ).getAttribute( "ID" ).getInteger(),  id );

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
		   ActivateOI_FromFile( mSPLDef, "mSPLDef", ViewToWindow, "target/test-classes/testdata//ePammsDon/JOE_Test2.por", zSINGLE );
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
		   ActivateOI_FromFile( mSPLDef, "mSPLDef", ViewToWindow, "target/test-classes/testdata//ePammsDon/JOE_Test2.por", zSINGLE );
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

      private int
      o_fnLocalBuildQual_6( View     vSubtask,
                            zVIEW    vQualObject,
                            int      lTempInteger_0 )
      {

      // RESULT = SfActivateSysEmptyOI( vQualObject, "KZDBHQUA", vSubtask, 256 /*zMULTIPLE*/ );
         View view = vSubtask.activateEmptyObjectInstance( "KZDBHQUA", task.getSystemTask().getApplication() );
      // CreateEntity( vQualObject, "EntitySpec", zPOS_AFTER );
         view.cursor( "EntitySpec" ).createEntity( CursorPosition.NEXT );
      // SetAttributeFromString( vQualObject, "EntitySpec", "EntityName", "Subregistrant" );
         view.cursor( "EntitySpec" ).getAttribute( "EntityName" ).setValue( "Subregistrant" );
      // CreateEntity( vQualObject, "QualAttrib", zPOS_AFTER );
         view.cursor( "QualAttrib" ).createEntity( CursorPosition.NEXT );
      // SetAttributeFromString( vQualObject, "QualAttrib", "EntityName", "Subregistrant" );
         view.cursor( "QualAttrib" ).getAttribute( "EntityName" ).setValue( "Subregistrant" );
      // SetAttributeFromString( vQualObject, "QualAttrib", "AttributeName", "ID" );
         view.cursor( "QualAttrib" ).getAttribute( "AttributeName" ).setValue( "ID" );
      // SetAttributeFromInteger( vQualObject, "QualAttrib", "Value", lTempInteger_0 );
         view.cursor( "QualAttrib" ).getAttribute( "Value" ).setValue( lTempInteger_0 );
      // SetAttributeFromString( vQualObject, "QualAttrib", "Oper", "=" );
         view.cursor( "QualAttrib" ).getAttribute( "Oper" ).setValue( "=" );
         vQualObject.setView( view );
         return( 0 );
      }

      private int
      o_fnLocalBuildQual_1( View     vSubtask,
                            zVIEW    vQualObject,
                            int      lTempInteger_0 )
      {

      // RESULT = SfActivateSysEmptyOI( vQualObject, "KZDBHQUA", vSubtask, 256 /*zMULTIPLE*/ );
         View view = vSubtask.activateEmptyObjectInstance( "KZDBHQUA", task.getSystemTask().getApplication() );
      // CreateEntity( vQualObject, "EntitySpec", zPOS_AFTER );
         view.cursor( "EntitySpec" ).createEntity( CursorPosition.NEXT );
      // SetAttributeFromString( vQualObject, "EntitySpec", "EntityName", "SubregProduct" );
         view.cursor( "EntitySpec" ).getAttribute( "EntityName" ).setValue( "SubregProduct" );
      // CreateEntity( vQualObject, "QualAttrib", zPOS_AFTER );
         view.cursor( "QualAttrib" ).createEntity( CursorPosition.NEXT );
      // SetAttributeFromString( vQualObject, "QualAttrib", "EntityName", "SubregProduct" );
         view.cursor( "QualAttrib" ).getAttribute( "EntityName" ).setValue( "SubregProduct" );
      // SetAttributeFromString( vQualObject, "QualAttrib", "AttributeName", "ID" );
         view.cursor( "QualAttrib" ).getAttribute( "AttributeName" ).setValue( "ID" );
      // SetAttributeFromInteger( vQualObject, "QualAttrib", "Value", lTempInteger_0 );
         view.cursor( "QualAttrib" ).getAttribute( "Value" ).setValue( lTempInteger_0 );
      // SetAttributeFromString( vQualObject, "QualAttrib", "Oper", "=" );
         view.cursor( "QualAttrib" ).getAttribute( "Oper" ).setValue( "=" );
         vQualObject.setView( view );
         return( 0 );
      }


      private int
      o_fnLocalBuildQual_8( View     vSubtask,
                            zVIEW    vQualObject,
                            int      lTempInteger_0 )
      {

      // RESULT = SfActivateSysEmptyOI( vQualObject, "KZDBHQUA", vSubtask, 256 /*zMULTIPLE*/ );
         View view = vSubtask.activateEmptyObjectInstance( "KZDBHQUA", task.getSystemTask().getApplication() );
      // CreateEntity( vQualObject, "EntitySpec", zPOS_AFTER );
         view.cursor( "EntitySpec" ).createEntity( CursorPosition.NEXT );
      // SetAttributeFromString( vQualObject, "EntitySpec", "EntityName", "MasterLabelContent" );
         view.cursor( "EntitySpec" ).getAttribute( "EntityName" ).setValue( "MasterLabelContent" );
      // CreateEntity( vQualObject, "QualAttrib", zPOS_AFTER );
         view.cursor( "QualAttrib" ).createEntity( CursorPosition.NEXT );
      // SetAttributeFromString( vQualObject, "QualAttrib", "EntityName", "MasterLabelContent" );
         view.cursor( "QualAttrib" ).getAttribute( "EntityName" ).setValue( "MasterLabelContent" );
      // SetAttributeFromString( vQualObject, "QualAttrib", "AttributeName", "ID" );
         view.cursor( "QualAttrib" ).getAttribute( "AttributeName" ).setValue( "ID" );
      // SetAttributeFromInteger( vQualObject, "QualAttrib", "Value", lTempInteger_0 );
         view.cursor( "QualAttrib" ).getAttribute( "Value" ).setValue( lTempInteger_0 );
      // SetAttributeFromString( vQualObject, "QualAttrib", "Oper", "=" );
         view.cursor( "QualAttrib" ).getAttribute( "Oper" ).setValue( "=" );
         vQualObject.setView( view );
         return( 0 );
      }

      public void ExecuteJOE_TestMismatch( View view ) {
         zVIEW    mSubreg = new zVIEW( );
         zVIEW    mSubProd = new zVIEW( );
         zVIEW    mSubLC = new zVIEW( );
         zVIEW    mMasLC = new zVIEW( );
         zVIEW    vTempViewVar_0 = new zVIEW( );

         o_fnLocalBuildQual_6( view, vTempViewVar_0, 1 /*lTempInteger_2*/ );
         ActivateObjectInstance( mSubreg, "mSubreg", view, vTempViewVar_0, zSINGLE );
         DropView( vTempViewVar_0 );
         SetNameForView( mSubreg, "mSubreg", null, zLEVEL_TASK );

         o_fnLocalBuildQual_1( view, vTempViewVar_0, 10 /*lTempInteger_0*/ );
         ActivateObjectInstance( mSubProd, "mSubProd", view, vTempViewVar_0, zSINGLE );
         DropView( vTempViewVar_0 );
         SetNameForView( mSubProd, "mSubProd", null, zLEVEL_TASK );

         ActivateEmptyObjectInstance( mSubLC, "mSubLC", view, zSINGLE );
         SetNameForView( mSubLC, "mSubLC", null, zLEVEL_TASK );
         CreateEntity( mSubLC, "SubregLabelContent", zPOS_AFTER );
         IncludeSubobjectFromSubobject( mSubLC, "SubregProduct", mSubProd, "SubregProduct", zPOS_AFTER );

         o_fnLocalBuildQual_8( view, vTempViewVar_0, 10 /*lTempInteger_0*/ );
         ActivateObjectInstance( mMasLC, "mMasLC", view, vTempViewVar_0, zSINGLE );
         DropView( vTempViewVar_0 );
         SetNameForView( mMasLC, "mMasLC", null, zLEVEL_TASK );
         IncludeSubobjectFromSubobject( mSubLC, "MasterLabelContent", mMasLC, "MasterLabelContent", zPOS_AFTER );

         {
            mMasLC_Object m_mMasLC_Object = new mMasLC_Object( mMasLC );
            m_mMasLC_Object.omMasLC_BuildCompositeEntries( mMasLC );
         }

         //:// Initialize the data in the SLC from the MLC.
         {
            mSubLC_Object m_mSubLC_Object = new mSubLC_Object( mSubLC );
            m_mSubLC_Object.omSubLC_BuildSLC_FromMLC( mSubLC, mMasLC );
         }

         //:// Build SLC Components subobject.
         {
            mSubLC_Object m_mSubLC_Object = new mSubLC_Object( mSubLC );
            m_mSubLC_Object.omSubLC_BuildCompositeEntries( mSubLC );
         }

         int RESULT = SetCursorFirstEntity( mSubLC, "S_Usage", "" );
         while ( RESULT > zCURSOR_UNCHANGED )
         { 
            //:IF mSubLC.M_Usage DOES NOT EXIST
            RESULT = CheckExistenceOfEntity( mSubLC, "M_Usage" );
            if ( RESULT != 0 )
            { 
               //:DELETE ENTITY mSubLC.S_Usage NONE 
               DeleteEntity( mSubLC, "S_Usage", zREPOS_NONE );
            } 

            RESULT = SetCursorNextEntity( mSubLC, "S_Usage", "" );
         } 

         //:// Go to select any mMLC entries that are already in the mSLC.
         //:SetMLC_SelectedFlags( mMasLC, mSubLC )
         {
            mMasLC_Object m_mMasLC_Object = new mMasLC_Object( mMasLC );
            m_mMasLC_Object.omMasLC_SetMLC_SelectedFlags( mMasLC, mSubLC );
          // m_mMasLC_Object = null;  // permit gc  (unnecessary)
         }


      // if ( CompareAttributeToString( mSubLC, "SubregLabelContent", "Description", "" ) == 0 || CompareAttributeToString( mSubLC, "SubregLabelContent", "Version", "" ) == 0 )
         SetAttributeFromString( mSubLC, "SubregLabelContent", "Description", "DeleteThisOne" );
         SetAttributeFromString( mSubLC, "SubregLabelContent", "Version", "0.1" );

         CommitObjectInstance( mSubLC );   

         View vmSubProd = task.getViewByName( "mSubProd" );
         if ( getView( vmSubProd ) != null )
         {
            View vGridSubregProducts2 = vmSubProd.newView( );
            CursorResult csrRC = vGridSubregProducts2.cursor( "SubregLabelContent" ).setFirst(  );
            Assert.assertEquals( "Unexpected RC", CursorResult.SET,  csrRC );
         }

      }
   }
}
