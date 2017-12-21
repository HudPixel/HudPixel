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