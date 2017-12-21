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
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefCustomRenderContainer
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import net.unaussprechlich.project.connect.chatgui.ChatWrapper
import net.unaussprechlich.project.connect.container.ChatTextFieldContainer

class TabContainer(width: Int, height: Int, val sizeCallback: (Int) -> Unit, sendCallback : (msg : String) -> Unit) : Container() {

    val chatInputField = ChatTextFieldContainer( width, {sizeCallback.invoke(it); update()}, sendCallback)
    val scrollCon = ChatActualChatContainer(width, height - chatInputField.height)

    val resizeCon = DefCustomRenderContainer { xStart, yStart , _ , _, con, _ ->
        if(con.isHover || ChatWrapper.resize)   RenderUtils.iconRender_resize(xStart + this.width - 2, yStart + this.height - chatInputField.height - 2, RGBA.WHITE.get())
        else                        RenderUtils.iconRender_resize(xStart + this.width - 2, yStart + this.height - chatInputField.height - 2, RGBA.P1B1_596068.get())
    }

    init {
        this.width = width
        this.height = height

        resizeCon.width = 10
        resizeCon.height = 10

        this register scrollCon
        this register chatInputField
        this register resizeCon

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
