/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.gui

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedCodeEvent
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.gui.register
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.project.connect.Connect
import net.unaussprechlich.project.connect.chatgui.ChatSetup
import net.unaussprechlich.project.connect.chatgui.ChatWrapper
import org.lwjgl.input.Keyboard

object ChatGUI : GUI() {


    init {
        this register ChatWrapper
        ChatSetup
    }

    override fun <T : Event<*>> doEventBus(event: T): Boolean {
         if (event.id == EnumDefaultEvents.KEY_PRESSED.get()) {
             if (!ChatWrapper.isVisible && Connect.chatKey!!.isPressed) {
                 GuiManagerMG.bindScreen()
                 ChatWrapper.isVisible = true
                 Keyboard.enableRepeatEvents(true)
                 return false
             }
         } else if (event.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()) {
             if (ChatWrapper.isVisible && (event as KeyPressedCodeEvent).data == Keyboard.KEY_ESCAPE) {
                 ChatWrapper.isVisible = false
                 Keyboard.enableRepeatEvents(false)
             }
         }

        return ChatWrapper.isVisible
    }

    override fun doOpenGUI(e: GuiOpenEvent): Boolean { return true }
    override fun doResize(): Boolean { return true }
    override fun doClientTick(): Boolean { return true }
    override fun doRender(xStart: Int, yStart: Int): Boolean { return true }
    override fun doChatMessage(e: ClientChatReceivedEvent?): Boolean { return true }
    override fun doMouseMove(mX: Int, mY: Int): Boolean { return true }
    override fun doScroll(i: Int): Boolean { return true }
    override fun doClick(clickType: MouseHandler.ClickType?): Boolean { return true }

}