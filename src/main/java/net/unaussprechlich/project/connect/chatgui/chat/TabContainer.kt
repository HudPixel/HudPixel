/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.project.connect.chatgui.chat

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.container.register
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.project.connect.container.ChatTextFieldContainer

class TabContainer(width: Int, height: Int, val sizeCallback: (Int) -> Unit, sendCallback : (msg : String) -> Unit) : Container() {

    val scrollCon = newChatActualChatContainer()
    val chatInputField = ChatTextFieldContainer( width, {sizeCallback.invoke(it); update()}, sendCallback)

    init {
        this.width = width
        this.height = height

        this register scrollCon
        this register chatInputField

        update()
    }

    fun update(){
        chatInputField.width = width
        chatInputField.yOffset = height - chatInputField.height
        scrollCon.width = width
        scrollCon.height = height - chatInputField.height
    }

    override fun doClientTickLocal(): Boolean = true
    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean = true
    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean = true
    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean = true
    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean = true
    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean = true
    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean = true
    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean = true
    override fun doResizeLocal(width: Int, height: Int): Boolean = true

}
