/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect

import eladkay.hudpixel.HudPixelMod
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.unaussprechlich.hudpixelextended.util.LoggerHelper
import net.unaussprechlich.managedgui.lib.ManagedGui
import net.unaussprechlich.managedgui.lib.ManagedGuiLib
import net.unaussprechlich.managedgui.lib.helper.SetupHelper
import net.unaussprechlich.project.connect.gui.ChatGUI
import net.unaussprechlich.project.connect.gui.NotificationGUI
import net.unaussprechlich.project.connect.gui.TestGUI
import net.unaussprechlich.project.connect.socket.io.SocketConnection
import org.lwjgl.input.Keyboard

/**
 * Connect Created by unaussprechlich on 20.12.2016.
 * Description:
 */
object Connect {

    var chatKey: KeyBinding? = null

    fun setup() {
        LoggerHelper.logInfo("Setting up Connect!")
        ManagedGuiLib.setup(object: SetupHelper {
            override fun getMODID(): String {
                return HudPixelMod.MODID
            }
        })
        //TODO set to false if you want the ManagedGUI lib
        ManagedGuiLib.isIsDisabled = false

        MinecraftForge.EVENT_BUS.register(this)

        this.chatKey = KeyBinding("Press to open the HudChat", Keyboard.KEY_K, "HudPixel Mod")
        ClientRegistry.registerKeyBinding(chatKey)

        ManagedGui.addGUI(NotificationGUI)
        ManagedGui.addGUI(TestGUI)
        ManagedGui.addGUI(ChatGUI)

        SocketConnection
    }

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        try {
            if (HudPixelMod.isHypixelNetwork) {
                if (this.chatKey!!.isPressed) {
                    ChatGUI.openChat()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }




}
