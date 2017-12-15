/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.EnumEventState

/**
 * DefCustomRenderContainer Created by Alexander on 07.03.2017.
 * Description:
 */
class DefCustomRenderContainer() : Container() {

    var customRenderer : ICustomRenderer? = null
    var renderer : ((xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState) -> Unit)? = null


    constructor(customRenderer: ICustomRenderer) : this(){
        this.customRenderer = customRenderer
    }

    constructor(renderer : (xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState) -> Unit ) : this() {
        this.renderer = renderer
    }

    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if(renderer != null) renderer!!.invoke(xStart, yStart, width, height, this, ees)
        if(customRenderer != null) customRenderer!!.onRender(xStart, yStart, width, height, this, ees)
       return true
    }

    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean {
        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean {
        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }
}
