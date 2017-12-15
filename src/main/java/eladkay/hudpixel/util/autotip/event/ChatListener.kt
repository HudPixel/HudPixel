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
package eladkay.hudpixel.util.autotip.event

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.command.LimboCommand
import eladkay.hudpixel.util.autotip.misc.TipTracker
import eladkay.hudpixel.util.autotip.misc.Writer
import eladkay.hudpixel.util.autotip.util.ChatColor
import eladkay.hudpixel.util.autotip.util.ClientMessage
import eladkay.hudpixel.util.autotip.util.MessageOption
import eladkay.hudpixel.util.autotip.util.UniversalUtil
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import java.util.regex.Matcher
import java.util.regex.Pattern

class ChatListener {

    private val xpPattern = Pattern.compile("\\+50 experience \\(Gave a player a /tip\\)")
    private val playerPattern = Pattern.compile("You tipped (?<player>\\w+) in .*")
    private val coinPattern = Pattern.compile(
            "\\+(?<coins>\\d+) coins for you in (?<game>.+) for being generous :\\)")
    private val earnedPattern = Pattern.compile(
            "You earned (?<coins>\\d+) coins and (?<xp>\\d+) experience from (?<game>.+) tips in the last minute!")

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {

        if (!Autotip.onHypixel) {
            return
        }

        val msg = UniversalUtil.getUnformattedText(event)
        val mOption = Autotip.messageOption

        if (Autotip.toggle) {
            if (msg == "Slow down! You can only use /tip every few seconds."
                    || msg == "Still processing your most recent request!"
                    || msg == "You are not allowed to use commands as a spectator!"
                    || msg == "You cannot tip yourself!"
                    || msg.startsWith("You can only use the /tip command")
                    || msg.startsWith("You can't tip the same person")
                    || msg.startsWith("You've already tipped someone in the past hour in ")
                    || msg.startsWith("You've already tipped that person")) {
                event.isCanceled = true
            }

            if (xpPattern.matcher(msg).matches()) {
                event.isCanceled = mOption == MessageOption.COMPACT || mOption == MessageOption.HIDDEN
                return
            }

            val playerMatcher = playerPattern.matcher(msg)
            if (playerMatcher.matches()) {
                TipTracker.addTip(playerMatcher.group("player"))
                event.isCanceled = mOption == MessageOption.HIDDEN
                return
            }

            val coinMatcher = coinPattern.matcher(msg)
            if (coinMatcher.matches()) {
                val coins = Integer.parseInt(coinMatcher.group("coins"))
                val game = coinMatcher.group("game")

                (TipTracker.tipsSentEarnings as java.util.Map<String, Int>).merge(game, coins) { a, b -> a + b }
                event.isCanceled = mOption == MessageOption.COMPACT || mOption == MessageOption.HIDDEN

                println("Earned $coins coins in $game")
                return
            }

            val earnedMatcher = earnedPattern.matcher(msg)
            if (earnedMatcher.matches()) {
                val coins = Integer.parseInt(earnedMatcher.group("coins"))
                val xp = Integer.parseInt(earnedMatcher.group("xp"))
                val game = earnedMatcher.group("game")

                (TipTracker.tipsReceivedEarnings as java.util.Map<String, Int>).merge(game, coins) { a, b -> a + b }
                TipTracker.tipsReceived += xp / 60
                Writer.execute()

                if (mOption == MessageOption.COMPACT) {
                    ClientMessage.sendRaw(
                            String.format("%sEarned %s%d coins%s and %s%d experience%s in %s.",
                                    ChatColor.GREEN, ChatColor.YELLOW, coins,
                                    ChatColor.GREEN, ChatColor.BLUE, xp,
                                    ChatColor.GREEN, game
                            ))
                }
                event.isCanceled = mOption == MessageOption.COMPACT || mOption == MessageOption.HIDDEN
                println("Earned $coins coins and $xp experience in $game")
                return
            }
        }

        if (LimboCommand.executed && msg.startsWith("A kick occurred in your connection") &&
                msg.contains("Illegal characters")) {
            event.isCanceled = true
            LimboCommand.executed = false
        }

    }
}
