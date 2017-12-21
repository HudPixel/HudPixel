/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.ConstantsMG
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.container.register
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedCodeEvent
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedEvent
import net.unaussprechlich.managedgui.lib.event.events.TimeEvent
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.FontUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils

class DefTextFieldContainer(width: Int, var hint : String = "", val sizeCallback : (height : Int) -> Unit) : Container() {

    override fun doResizeLocal(width: Int, height: Int): Boolean { return true }

    val textCon =  DefTextAutoLineBreakContainer("", width - 20, {update()}).apply {
        yOffset = 5
        xOffset = 5
    }

    var text
        get() = textCon.text
        set(value) {
            textCon.text = value
            updateCursor()
        }

    fun clear(){
        cursorPos = 0
        text = ""
    }

    var hasFocus = false
    var cursorBlink = false
    var cursorPos = 0
    var cursorX = 0
    var cursorY = 0

    init {
        this register textCon

        this.width = width

        textCon.clickedCallback.registerListener { clickType, _ ->
            if(clickType == MouseHandler.ClickType.SINGLE) hasFocus = !hasFocus
        }

        cursorPos = text.length
        backgroundRGBA = RGBA.P1B1_DEF.get()
    }

    fun update(){
        height = textCon.height + 10
        textCon.width = width - 27
        sizeCallback.invoke(height)
    }

    fun updateCursor(){
        var index = 0
        var row = 0
        for(s in textCon.renderList){
            if((index + s.length) >= cursorPos - row){
                cursorX = FontUtil.getStringWidth(s.substring(0, cursorPos - index - row) )
                cursorY = row * ConstantsMG.TEXT_Y_OFFSET
            } else {
                index += s.length
                row++
            }
        }
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if(ees == EnumEventState.PRE) return true

        RenderUtils.drawBorderInlineShadowBox(xStart + 2 , yStart + 2, width - 4, height - 4, RGBA.P1B1_596068.get(), RGBA.P1B1_DEF.get())

        if(text == "" && hint != "") FontUtil.draw("" + EnumChatFormatting.GRAY + EnumChatFormatting.ITALIC + hint, xStart+5, yStart+5)
        if(cursorBlink)                      RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + 5 + cursorX, yStart + 5 + cursorY, 1, 9, RGBA.P1B1_596068.get())

        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        if(!isVisible) return false

        if (iEvent.id == EnumDefaultEvents.TIME.get())
            if((iEvent as TimeEvent).data == EnumTime.TICK_15){
                cursorBlink = !cursorBlink
                return true
            }

        if(iEvent.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()){
            when((iEvent as KeyPressedCodeEvent).data){
                14 -> {
                    if(text.isNotEmpty())
                        if(cursorPos == text.length){
                           text = text.substring(0, text.length -1)
                            cursorPos--
                        } else{
                            text = text.substring(0, cursorPos - 1) + text.substring(cursorPos, text.length)
                            if(cursorPos > 0) cursorPos--
                        }
                }
                205 -> {
                    if(cursorPos + 1 <= text.length) cursorPos++
                }
                203 -> {
                    if(cursorPos > 0) cursorPos--
                }
                //else -> println("data: ${iEvent.data.toInt()}")
            }
            updateCursor()

            return true
        }

        if (iEvent.id != EnumDefaultEvents.KEY_PRESSED.get()) return true
        val c = (iEvent as KeyPressedEvent).data.toCharArray()[0]

        if(cursorPos == text.length){
            cursorPos++
            text += c
        } else {
            cursorPos++
            text = text.substring(0, --cursorPos) + c + text.substring(--cursorPos, text.length)
        }

        return true
    }

    override fun doClientTickLocal(): Boolean { return true }
    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean { return true }
    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean { return true }
    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean { return true }
    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean { return true }
    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean { return true }

}