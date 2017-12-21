/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.managedgui.lib.gui

import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.managedgui.lib.ManagedGui
import net.unaussprechlich.managedgui.lib.child.ChildRegistry
import net.unaussprechlich.managedgui.lib.child.IChild
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler

infix fun GUI.register(con : Container) = this.registerChild(con)
infix fun GUI.unregister(con : Container) = this.unregisterChild(con)



abstract class GUI : ChildRegistry(), IChild {

    private var xStart = 0
    private var yStart = 0
    var isVisible = false
    var keepInCache = false
        protected set

    enum class Mode{
        ALWAYS,
        WHILEBINDED,
        NULL
    }

    abstract fun getMode(): Mode

    fun onCloseGUI(){
        if(getMode() == Mode.WHILEBINDED) isVisible = false
    }

    fun onOpenGUI(gui: GuiScreen?){
        if(gui is ManagedGui && getMode() == Mode.WHILEBINDED) isVisible = true
    }

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

    override fun doRender(xStart: Int, yStart: Int): Boolean {
        return isVisible
    }

    override fun doClientTick(): Boolean { return isVisible }

    override fun doChatMessage(e: ClientChatReceivedEvent?): Boolean { return isVisible }

    override fun doMouseMove(mX: Int, mY: Int): Boolean { return isVisible }

    override fun doScroll(i: Int): Boolean { return isVisible }

    override fun doClick(clickType: MouseHandler.ClickType?): Boolean { return isVisible }

    override fun <T : Event<*>> doEventBus(event: T): Boolean { return isVisible }

    override fun doResize(): Boolean { return isVisible }

}
