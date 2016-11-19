package eladkay.modulargui.lib.base

import eladkay.modulargui.lib.IModularGuiProvider
import net.minecraft.client.Minecraft

/**
 * This class is meant to show a correct implementation of IModularGuiProvider.
 * This is a static implementation: all elements using it will always have the same value.

 * @author Eladkay
 * *
 * @since 1.6
 */
class NameModularGuiProvider : IModularGuiProvider {
    override fun getAfterstats(): String {
        throw UnsupportedOperationException()
    }

    override fun showElement(): Boolean {
        return true
    } //elements using this provider will always be shown.

    override fun content(): String {
        if (Minecraft.getMinecraft().thePlayer == null)
            return "" //if the player object is null (in the title screen etc) don't show anything
        return Minecraft.getMinecraft().thePlayer.name //else just return the player's name
    }

    override fun ignoreEmptyCheck(): Boolean {
        return false
    }
}
