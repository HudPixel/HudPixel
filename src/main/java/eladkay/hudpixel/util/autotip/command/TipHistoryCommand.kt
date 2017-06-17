package eladkay.hudpixel.util.autotip.command

import eladkay.hudpixel.command.HpCommandBase
import eladkay.hudpixel.util.autotip.misc.TipTracker
import eladkay.hudpixel.util.autotip.util.ClientMessage
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumChatFormatting

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
                ClientMessage.send(EnumChatFormatting.RED.toString() + "Invalid page number.")
            } else {
                ClientMessage.separator()
                ClientMessage.send(EnumChatFormatting.GOLD.toString() + "Tip History " + EnumChatFormatting.GRAY
                        + "[Page " + page + " of " + pages + "]" + EnumChatFormatting.GOLD + ":")

                TipTracker.tipsSentHistory.entries.stream()
                        .skip(((page - 1) * 7).toLong())
                        .limit(7)
                        .forEach { tip ->
                            ClientMessage.send(tip.value + ": " + EnumChatFormatting.GOLD
                                    + formatMillis(
                                    System.currentTimeMillis() - tip.key) + ".")
                        }

                ClientMessage.separator()
            }
        } else {
            ClientMessage.send(EnumChatFormatting.RED.toString() + "You haven't tipped anyone yet!")
        }
    }

    override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String> {
        return emptyList()
    }
    fun formatMillis(duration: Long): String {
        val sb = StringBuilder()
        var temp: Long
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY
            if (temp > 0) {
                sb.append(temp).append(" day").append(if (temp > 1) "s" else "")
                return sb.toString() + " ago"
            }

            temp = duration / ONE_HOUR
            if (temp > 0) {
                sb.append(temp).append(" hour").append(if (temp > 1) "s" else "")
                return sb.toString() + " ago"
            }

            temp = duration / ONE_MINUTE
            if (temp > 0) {
                sb.append(temp).append(" minute").append(if (temp > 1) "s" else "")
                return sb.toString() + " ago"
            }

            temp = duration / ONE_SECOND
            if (temp > 0) {
                sb.append(temp).append(" second").append(if (temp > 1) "s" else "")
            }
            return sb.toString() + " ago"
        } else {
            return "just now"
        }
    }
    private val ONE_SECOND: Long = 1000
    private val ONE_MINUTE = ONE_SECOND * 60
    private val ONE_HOUR = ONE_MINUTE * 60
    private val ONE_DAY = ONE_HOUR * 24
}
