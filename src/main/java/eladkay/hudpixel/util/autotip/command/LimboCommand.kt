package eladkay.hudpixel.util.autotip.command

import eladkay.hudpixel.command.HpCommandBase
import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.util.ChatColor
import eladkay.hudpixel.util.autotip.util.ClientMessage
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos

import java.util.Collections

class LimboCommand : HpCommandBase() {

    override fun getCommandName(): String {
        return "limbo"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "/limbo"
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (Autotip.onHypixel) {
            executed = true
            Autotip.mc.thePlayer.sendChatMessage(ChatColor.RED.toString())
        } else {
            ClientMessage.send(ChatColor.RED.toString() + "You must be on Hypixel to use this command!")
        }
    }

    override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String> {
        return emptyList()
    }

    companion object {

        var executed: Boolean = false
    }
}
