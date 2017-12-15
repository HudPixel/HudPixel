/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.container

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.container.register
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedCodeEvent
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefCustomRenderContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefTextFieldContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.ICustomRenderer
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils

class ChatTextFieldContainer( width: Int = 400, val sizeCallback : (height : Int) -> Unit, val sendCallback: (msg : String) -> Unit) : Container(){

    private val sendIconRenderer = object: ICustomRenderer{
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true

            val offY = (height - 7) / 2
            var color = RGBA.P1B1_596068.get()
            if(con.isHover) color =  RGBA.WHITE.get()

            RenderUtils.drawBorderInlineShadowBox(xStart , yStart ,  width , height  , color, RGBA.P1B1_DEF.get())

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + 4, yStart + offY     , buttonWidth - 8, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + 4, yStart + offY + 3 , buttonWidth - 8, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + 4, yStart + offY + 6 , buttonWidth - 8, 1, color)

            return true
        }
    }

    val textFieldCon = DefTextFieldContainer(width - 19, "Enter your message ...", { height ->
        update()
        sizeCallback.invoke(height)
    })

    /**
     * The Container for the send button
     */
    val sendButton = DefCustomRenderContainer(sendIconRenderer)
    val buttonWidth = 20

    init {
        this register textFieldCon
        this register sendButton

        this.width = width
        sendButton.yOffset = 2
        sendButton.width = buttonWidth

        backgroundRGBA = RGBA.P1B1_DEF.get()

        sendButton.clickedCallback.registerListener { clickType, _ ->
            if(clickType == MouseHandler.ClickType.SINGLE) send()
        }

        this.height = textFieldCon.height
        sendButton.height = height - 4
        sendButton.xOffset = width - buttonWidth - 2
    }

    /**
     * Updates the dimensions for all registered childs
     */
    fun update(){
        textFieldCon.width = width - 19
        this.height = textFieldCon.height
        sendButton.height = height - 4
        sendButton.xOffset = width - buttonWidth - 2
    }

    /**
     * Invokes the sendCallback and clears the text field
     */
    fun send(){
        sendCallback.invoke(textFieldCon.text)
        textFieldCon.clear()
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        if(iEvent.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()){
            when((iEvent as KeyPressedCodeEvent).data){
                28 -> send()
            }
        }
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean = true
    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean = true
    override fun doClientTickLocal(): Boolean {return true }
    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean { return true }
    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean { return true }
    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean { return true }
    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean { return true }
    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean { return true }


}


