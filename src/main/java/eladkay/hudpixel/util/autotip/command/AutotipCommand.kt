/*******************************************************************************
 * HudPixelReloaded
 *
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses under forge-docs/. These parts can be downloaded at files.minecraftforge.net.
 *
 * This project contains a unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 *
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously intended for usage in this kind of application. By default, all rights are reserved.
 *
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license. The majority of code left from palechip's creations is the component implementation.
 *
 * The ported version to Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license (to be changed to the new license as detailed below in the next minor update).
 *
 * For the rest of the code and for the build the following license applies:
 *
 * alt-tag
 *
 * HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions. Based on a work at HudPixelExtended & HudPixel.
 *
 * Restrictions:
 *
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow the following license terms and the license terms given by the listed above Creative Commons License, however in extreme cases the authors reserve the right to revoke all rights for usage of the codebase.
 *
 * PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their code, but only when it is used separately from HudPixel and any license header must indicate that.
 * You shall not claim ownership over this project and repost it in any case, without written permission from at least two of the authors.
 * You shall not make money with the provided material. This project is 100% non commercial and will always stay that way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed code is merged to the release branch you cannot revoke the given freedoms by this license.
 * If your own project contains a part of the licensed material you have to give the authors full access to all project related files.
 * You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors reserve the right to take down any infringing project.
 ******************************************************************************/
package eladkay.hudpixel.util.autotip.command

import com.google.common.collect.Lists
import eladkay.hudpixel.command.HpCommandBase
import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.event.HypixelListener
import eladkay.hudpixel.util.autotip.event.Tipper
import eladkay.hudpixel.util.autotip.misc.StartLogin
import eladkay.hudpixel.util.autotip.misc.Stats
import eladkay.hudpixel.util.autotip.misc.TipTracker
import eladkay.hudpixel.util.autotip.util.ChatColor
import eladkay.hudpixel.util.autotip.util.ClientMessage
import eladkay.hudpixel.util.autotip.util.FileUtil
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
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
                                ChatColor.GREEN.toString() + "En"
                            else
                                ChatColor.RED.toString() + "Dis") + "abled")
                    ClientMessage.send("Tip Messages: " + Autotip.messageOption)
                    ClientMessage.send("Tips sent today: " + ChatColor.GOLD + TipTracker.tipsSent)
                    ClientMessage.send("Tips received today: " + ChatColor.GOLD
                            + TipTracker.tipsReceived)
                    ClientMessage
                            .send("Lifetime tips sent: " + ChatColor.GOLD + Autotip.totalTipsSent)
                    ClientMessage.send(ChatColor.GOLD.toString() + "Type /hudpixelautotip stats to see what has been earned.")
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
                            else -> ClientMessage.send(ChatColor.RED.toString() + "Usage: /hudpixelautotip stats <day, week, month, year, lifetime>")
                        }
                    } else {
                        Stats.printStats(FileUtil.date)
                    }
                }
                "t", "toggle" -> {
                    Autotip.toggle = !Autotip.toggle
                    ClientMessage.send(
                            "Autotipper: " + (if (Autotip.toggle)
                                ChatColor.GREEN.toString() + "En"
                            else
                                ChatColor.RED.toString() + "Dis") + "abled")
                }
                "wave", "time" -> if (Autotip.toggle) {
                    if (Autotip.onHypixel) {
                        ClientMessage.separator()
                        ClientMessage.send("Last wave: " +
                                ChatColor.GOLD + LocalTime.MIN.plusSeconds(Tipper.waveCounter.toLong())
                                .toString())
                        ClientMessage.send("Next wave: " +
                                ChatColor.GOLD + LocalTime.MIN.plusSeconds(
                                (Tipper.waveLength - Tipper.waveCounter).toLong()).toString())
                        ClientMessage.separator()
                    } else {
                        ClientMessage
                                .send("Autotip is disabled as you are not playing on Hypixel.")
                    }
                } else {
                    ClientMessage.send("Autotip is disabled. Use " + ChatColor.GOLD
                            + "/hudpixelautotip toggle"
                            + ChatColor.GRAY + " to enable it.")
                }
                "update" -> StartLogin.login()
                "info+" -> {
                    ClientMessage.separator()
                    ClientMessage.send("Last IP joined: " + HypixelListener.lastIp)
                    ClientMessage
                            .send("Current tipqueue: " + StringUtils.join(Tipper.tipQueue, ", "))
                    ClientMessage.separator()
                }
                else -> ClientMessage.send(ChatColor.RED.toString() + "Usage: " + getCommandUsage(sender))
            }
        } else {
            ClientMessage.send(ChatColor.RED.toString() + "Usage: " + getCommandUsage(sender))
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
