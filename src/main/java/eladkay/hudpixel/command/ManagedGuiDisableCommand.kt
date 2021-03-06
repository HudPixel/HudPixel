package eladkay.hudpixel.command

import eladkay.hudpixel.util.ChatMessageComposer
import eladkay.hudpixel.util.plus
import net.minecraft.command.ICommandSender
import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.managedgui.lib.ManagedGui

/**
 * Created by Elad on 3/2/2017.
 */
object ManagedGuiDisableCommand : HpCommandBase() {

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (ManagedGui.isIsDisabled) {
            ChatMessageComposer(EnumChatFormatting.GREEN + "Disabled managed GUI")
            ManagedGui.isIsDisabled = false
        } else {
            ChatMessageComposer(EnumChatFormatting.RED + "Disabled managed GUI")
            ManagedGui.isIsDisabled = true
        }
    }

    override fun getCommandName(): String {
        return "mgd"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/mgd"
    }
}