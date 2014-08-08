/**
 *
 */
package com.quinsoft.zeidon.scala

import com.quinsoft.zeidon._
import com.quinsoft.zeidon.objectdefinition._
import com.quinsoft.zeidon.scala.EntityCursor

/**
 * A Scala wrapper for the JOE View.  This object uses dynamic methods that allows
 * users to write code using VML-like view.entity.attribute syntax.
 *
 * @author dgc
 *
 */
class View( val task: Task ) extends Task(task) {

    var jviewOd: ViewOd = null
    var jview:   com.quinsoft.zeidon.View = null

    def this( jv: com.quinsoft.zeidon.View ) = {
      this( new Task( jv.getTask() ) )
      jviewOd = jv.getViewOd()
      jview = jv
    }

    def this( jtask: com.quinsoft.zeidon.Task ) = {
      this( new Task( jtask ) )
    }

    def BASEDONLOD( lodName: String ): View = basedOnLod( lodName )
	def basedOnLod( lodName: String ): View = {
		jviewOd = task.jtask.getApplication().getViewOd( task.jtask, lodName )
		if ( jview != null && jview.getViewOd() != jviewOd )
		  throw new ZeidonException( "ViewOD set by basedOnLod doesn't match view." )

		return this
	}

    def from( view: View ) {
        jview = view.jview.newView()
        jviewOd = jview.getViewOd()
    }

    def copyCursors( view: View ) {
        if ( jview == null )
            throw new ZeidonException( "View has no OI" )

        jview.copyCursors(view.jview)
    }

    def activateEmpty(): Unit = {
        validateViewOd
        jview = jtask.activateEmptyObjectInstance( jviewOd )
    }

    def activateWhere( addQual: (QualBuilder) => QualBuilder ): QualBuilder = {
        validateViewOd
        val builder = new QualBuilder( this, jviewOd )
        addQual( builder )
    }

    def buildQual( addQual: (QualBuilder) => QualBuilder ): QualBuilder = {
        val builder = buildQual()
        addQual( builder )
    }

    def buildQual(): QualBuilder = {
        validateViewOd
        val builder = new QualBuilder( this, jviewOd )
        return builder
    }

    def logObjectInstance = jview.logObjectInstance()
    def activateOptions = jview.getActivateOptions()

    /**
     * This is called when the compiler doesn't recognize a method name.  This
     * is used to find the entity cursor for a view.
     */
    def selectDynamic(entityName: String): EntityCursor = {
        validateViewOd

        // Following will throw an exception if the entityName is not valid.
        val jviewEntity = jviewOd.getViewEntity(entityName)
        val jcur = jview.cursor(jviewEntity)
        new EntityCursor( this, jcur )
    }

    /**
     * Validates that the View OD is specified.
     */
    private def validateViewOd: Unit = {
        if ( jviewOd == null )
            throw new ZeidonException( "LOD name not established for this View" )
    }
}

object View {
    /**
     * Convert a Java View to a Scala View
     */
    implicit def jview2view( jview: com.quinsoft.zeidon.View ) = new com.quinsoft.zeidon.scala.View( jview )
    
    /**
     * Convert Scala View to Java View.
     */
    implicit def view2jview( view: com.quinsoft.zeidon.scala.View ) = view.jview

}