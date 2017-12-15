/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.managedgui.lib.gui

import net.unaussprechlich.managedgui.lib.child.ChildRegistry
import net.unaussprechlich.managedgui.lib.child.IChild
import net.unaussprechlich.managedgui.lib.container.Container

infix fun GUI.register(con : Container) = this.registerChild(con)
infix fun GUI.unregister(con : Container) = this.unregisterChild(con)

abstract class GUI : ChildRegistry(), IChild {

    private var xStart = 0
    private var yStart = 0

    fun setXStart(xStart: Int) {
        this.xStart = xStart
    }

    fun setYStart(yStart: Int) {
        this.yStart = yStart
    }

    override fun getXStart(): Int {
        return xStart
    }

    override fun getYStart(): Int {
        return yStart
    }

}
