package com.quinsoft.northwind.scala

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Before
import org.junit.Test
import com.quinsoft.zeidon.scala.ZeidonOperations
import com.quinsoft.zeidon.scala.View
import com.quinsoft.zeidon.scala.Implicits._
import com.quinsoft.zeidon.standardoe.JavaObjectEngine

class TestNorthwind extends AssertionsForJUnit {

    val oe = JavaObjectEngine.getInstance()
    var task = oe.createTask("northwind")

    @Before
    def initialize() {
//        oe.startBrowser
    }

    @Test
    def testLoadFromJson() {
        val order = task.deserializeOi().fromFile("testdata/northwind/data/order1.json").unpickle
        assertTrue( order.Employee.exists )
        assertTrue( order.Customer.exists )
        assertTrue( order.Shipper.exists )
        order.logObjectInstance
    }

    @Test
    def testSerializingHiddenEntities() {
        val order = new View( task ) basedOn "Order"
        order.activateWhere( _.Order.OrderId = 10250 )
        assertTrue( order.Order.exists )
        order.OrderDetail.deleteEntity()
        assertEquals( 2, order.OrderDetail.count( false ) )
        assertEquals( 3, order.OrderDetail.count( true ) )

        order.serializeOi.asJson().withIncremental().toTempDir( "testDelete.json" )
        val order2 = task.deserializeOi().fromTempDir("testDelete.json").unpickle
        assertEquals( 2, order2.OrderDetail.count( false ) )
        assertEquals( 3, order2.OrderDetail.count( true ) )

        order.serializeOi.asXml().withIncremental().toTempDir( "testDelete.xml" )
        val order3 = task.deserializeOi().fromTempDir("testDelete.xml").unpickle
        assertEquals( 2, order3.OrderDetail.count( false ) )
        assertEquals( 3, order3.OrderDetail.count( true ) )

/* TODO: We don't currently handle hidden entities in .por files.
        order.serializeOi.withIncremental().toTempDir( "testDelete.por" )
        val order4 = task.deserializeOi().fromTempDir("testDelete.por").unpickle
        assertEquals( 2, order4.OrderDetail.count( false ) )
        assertEquals( 3, order4.OrderDetail.count( true ) )
*/
    }
}
