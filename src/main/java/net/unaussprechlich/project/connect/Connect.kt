/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect

import eladkay.hudpixel.HudPixelMod
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.unaussprechlich.hudpixelextended.util.LoggerHelper
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.ManagedGui
import net.unaussprechlich.managedgui.lib.helper.SetupHelper
import net.unaussprechlich.project.connect.gui.ConnectGUI
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
        ManagedGui.setup(object: SetupHelper {
            override fun getMODID(): String {
                return HudPixelMod.MODID
            }
        })
        //TODO set to false if you want the ManagedGUI lib
        ManagedGui.isIsDisabled = false

        this.chatKey = KeyBinding("Press to open the HudChat", Keyboard.KEY_K, "HudPixel Mod")
        ClientRegistry.registerKeyBinding(chatKey)

        GuiManagerMG.addGUI("ConnectGUI", ConnectGUI)

        SocketConnection
    }




}
