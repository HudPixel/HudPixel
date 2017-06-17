package eladkay.hudpixel.util.autotip.command

import com.google.common.collect.Lists
import eladkay.hudpixel.command.HpCommandBase
import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.event.Tipper
import eladkay.hudpixel.util.autotip.misc.StartLogin
import eladkay.hudpixel.util.autotip.misc.Stats
import eladkay.hudpixel.util.autotip.misc.TipTracker
import eladkay.hudpixel.util.autotip.util.ClientMessage
import eladkay.hudpixel.util.autotip.util.FileUtil
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumChatFormatting
import org.apache.commons.lang3.StringUtils
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Year
import java.time.format.DateTimeFormatter

class AutotipCommand : HpCommandBase() {

    override fun getCommandName(): String {
        return "hudpixelautotip"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "/hudpixelautotip <stats, info, messages, toggle, time>"
    }

    override fun getCommandAliases(): List<String> {
        return Lists.newArrayList("hudat")
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.size > 0) {
            when (args[0].toLowerCase()) {
                "m", "messages" -> {
                    Autotip.messageOption = Autotip.messageOption.next()
                    ClientMessage.send("Tip Messages: " + Autotip.messageOption)
                }
                "?", "info" -> {
                    ClientMessage.separator()
                    ClientMessage.send(
                            "Autotipper: " + (if (Autotip.toggle)
                                EnumChatFormatting.GREEN.toString() + "En"
                            else
                                EnumChatFormatting.RED.toString() + "Dis") + "abled")
                    ClientMessage.send("Tip Messages: " + Autotip.messageOption)
                    ClientMessage.send("Tips sent today: " + EnumChatFormatting.GOLD + TipTracker.tipsSent)
                    ClientMessage.send("Tips received today: " + EnumChatFormatting.GOLD
                            + TipTracker.tipsReceived)
                    ClientMessage
                            .send("Lifetime tips sent: " + EnumChatFormatting.GOLD + Autotip.totalTipsSent)
                    ClientMessage.send(EnumChatFormatting.GOLD.toString() + "Type /hudpixelautotip stats to see what has been earned.")
                    ClientMessage.separator()
                }
                "s", "stats" -> {
                    val now = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

                    if (args.size == 2) {
                        when (args[1].toLowerCase()) {
                            "day", "daily", "today" -> Stats.printStats(FileUtil.date)
                            "yesterday" -> Stats.printStats(LocalDate.now().minusDays(1).format(formatter))
                            "week", "weekly" -> Stats.printBetween(now.with(DayOfWeek.MONDAY).format(formatter),
                                    now.with(DayOfWeek.SUNDAY).format(formatter))
                            "month", "monthly" -> Stats.printBetween(now.withDayOfMonth(1).format(formatter),
                                    now.withDayOfMonth(now.lengthOfMonth()).format(formatter))
                            "year", "yearly" -> Stats.printBetween("01-01-" + Year.now().value,
                                    "31-12-" + Year.now().value)
                            "all", "total", "life", "lifetime" -> Stats.printBetween("25-06-2016", FileUtil.date)
                            else -> ClientMessage.send(EnumChatFormatting.RED.toString() + "Usage: /hudpixelautotip stats <day, week, month, year, lifetime>")
                        }
                    } else {
                        Stats.printStats(FileUtil.date)
                    }
                }
                "t", "toggle" -> {
                    Autotip.toggle = !Autotip.toggle
                    ClientMessage.send(
                            "Autotipper: " + (if (Autotip.toggle)
                                EnumChatFormatting.GREEN.toString() + "En"
                            else
                                EnumChatFormatting.RED.toString() + "Dis") + "abled")
                }
                "wave", "time" -> if (Autotip.toggle) {
                    if (Autotip.onHypixel) {
                        ClientMessage.separator()
                        ClientMessage.send("Last wave: " +
                                EnumChatFormatting.GOLD + LocalTime.MIN.plusSeconds(Tipper.waveCounter.toLong())
                                .toString())
                        ClientMessage.send("Next wave: " +
                                EnumChatFormatting.GOLD + LocalTime.MIN.plusSeconds(
                                (Tipper.waveLength - Tipper.waveCounter).toLong()).toString())
                        ClientMessage.separator()
                    } else {
                        ClientMessage
                                .send("Autotip is disabled as you are not playing on Hypixel.")
                    }
                } else {
                    ClientMessage.send("Autotip is disabled. Use " + EnumChatFormatting.GOLD
                            + "/hudpixelautotip toggle"
                            + EnumChatFormatting.GRAY + " to enable it.")
                }
                "update" -> StartLogin.login()
                "info+" -> {
                    ClientMessage.separator()
                    ClientMessage
                            .send("Current tipqueue: " + StringUtils.join(Tipper.tipQueue, ", "))
                    ClientMessage.separator()
                }
                else -> ClientMessage.send(EnumChatFormatting.RED.toString() + "Usage: " + getCommandUsage(sender))
            }
        } else {
            ClientMessage.send(EnumChatFormatting.RED.toString() + "Usage: " + getCommandUsage(sender))
        }
    }

    override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String> {
        when (args!!.size) {
            1 -> return CommandBase.getListOfStringsMatchingLastWord(args, "stats", "info", "messages", "toggle",
                    "time")
            2 -> if (args[0].equals("stats", ignoreCase = true) || args[0].equals("s", ignoreCase = true)) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "day", "yesterday", "week",
                        "month", "year",
                        "lifetime")
            }
        }
        return emptyList()
    }
}
