package eladkay.hudpixel.util.autotip.command

import eladkay.hudpixel.command.HpCommandBase
import eladkay.hudpixel.util.autotip.misc.TipTracker
import eladkay.hudpixel.util.autotip.util.ChatColor
import eladkay.hudpixel.util.autotip.util.ClientMessage
import eladkay.hudpixel.util.autotip.util.TimeUtil
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos

import java.util.Collections

class TipHistoryCommand : HpCommandBase() {

    override fun getCommandName(): String {
        return "tiphistory"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "/tiphistory [page]"
    }

    override fun getCommandAliases(): List<String> {
        return listOf("lasttip")
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (TipTracker.tipsSentHistory.size > 0) {
            var page = 1
            val pages = Math.ceil(TipTracker.tipsSentHistory.size.toDouble() / 7.0).toInt()

            if (args.size > 0) {
                try {
                    page = Integer.parseInt(args[0])
                } catch (ignored: NumberFormatException) {
                    page = -1
                }

            }

            if (page < 1 || page > pages) {
                ClientMessage.send(ChatColor.RED.toString() + "Invalid page number.")
            } else {
                ClientMessage.separator()
                ClientMessage.send(ChatColor.GOLD.toString() + "Tip History " + ChatColor.GRAY
                        + "[Page " + page + " of " + pages + "]" + ChatColor.GOLD + ":")

                TipTracker.tipsSentHistory.entries.stream()
                        .skip(((page - 1) * 7).toLong())
                        .limit(7)
                        .forEach { tip ->
                            ClientMessage.send(tip.value + ": " + ChatColor.GOLD
                                    + TimeUtil.formatMillis(
                                    System.currentTimeMillis() - tip.key) + ".")
                        }

                ClientMessage.separator()
            }
        } else {
            ClientMessage.send(ChatColor.RED.toString() + "You haven't tipped anyone yet!")
        }
    }

    override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String> {
        return emptyList()
    }
}
