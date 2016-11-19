/* **********************************************************************************************************************
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 **********************************************************************************************************************/
package com.palechip.hudpixelmod.modulargui.components

import com.mojang.realmsclient.gui.ChatFormatting
import com.palechip.hudpixelmod.GameDetector
import com.palechip.hudpixelmod.config.CCategory
import com.palechip.hudpixelmod.config.ConfigPropertyBoolean
import com.palechip.hudpixelmod.modulargui.SimpleHudPixelModularGuiProvider
import com.palechip.hudpixelmod.util.GameType
import com.palechip.hudpixelmod.util.McColorHelperJava
import com.palechip.hudpixelmod.util.plus
import net.minecraftforge.fml.client.FMLClientHandler

object MWKillCounterModularGuiProvider : SimpleHudPixelModularGuiProvider(), McColorHelperJava {
    private val trackedType = KillType.Normal
    private var kills: Int = 0

    override fun doesMatchForGame(): Boolean {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.MEGA_WALLS)
    }

    override fun setupNewGame() {
        this.kills = 0
    }

    override fun onGameStart() {
    }

    override fun onGameEnd() {
    }

    override fun onTickUpdate() {
    }

    override fun onChatMessage(textMessage: String, formattedMessage: String) {
        // coin message?, not from tipping
        if (textMessage.startsWith("+") && textMessage.toLowerCase().contains("coins") && !textMessage.toLowerCase().contains("for being generous :)")) {
            when (this.trackedType) {
                MWKillCounterModularGuiProvider.KillType.Normal -> {
                    // exclude wither rushing reward
                    if (!textMessage.contains("ASSIST") && !textMessage.contains("FINAL KILL") && !textMessage.contains("Wither Damage")) {
                        this.kills++
                    }
                    // some ninja detection for kills over 18
                    if (this.kills >= 18 && textMessage.contains("was killed by " + FMLClientHandler.instance().client.session.username)) {
                        this.kills++
                    }
                }
                MWKillCounterModularGuiProvider.KillType.Final -> if (!textMessage.contains("ASSIST") && textMessage.contains("FINAL KILL")) {
                    this.kills++
                }
                MWKillCounterModularGuiProvider.KillType.Assists -> if (textMessage.contains("ASSIST") && !textMessage.contains("FINAL KILL")) {
                    this.kills++
                }
                MWKillCounterModularGuiProvider.KillType.Final_Assists -> if (textMessage.contains("ASSIST") && textMessage.contains("FINAL KILL")) {
                    this.kills++
                }
                MWKillCounterModularGuiProvider.KillType.Wither_Coins -> if (textMessage.contains("Wither Damage")) {
                    this.kills += CoinCounterModularGuiProvider.getCoinsFromMessage(textMessage)
                }
            }
        }
    }

    val renderingString: String
        get() {
            when (this.trackedType) {
                MWKillCounterModularGuiProvider.KillType.Normal -> return KILL_DISPLAY + this.kills
                MWKillCounterModularGuiProvider.KillType.Final -> return FINAL_KILL_DISPLAY + this.kills
                MWKillCounterModularGuiProvider.KillType.Assists -> return ASSISTS_DISPLAY + this.kills
                MWKillCounterModularGuiProvider.KillType.Final_Assists -> return FINAL_ASSISTS_DISPLAY + this.kills
                MWKillCounterModularGuiProvider.KillType.Wither_Coins -> return if (this.kills > 0) WITHER_COINS_DISPLAY + this.kills else ""
            }
            return ""
        }

    override fun showElement(): Boolean {
        return doesMatchForGame() && !GameDetector.isLobby() && enabled
    }

    override fun content(): String {
        return renderingString
    }

    override fun ignoreEmptyCheck(): Boolean {
        return false
    }

    override fun getAfterstats(): String {
        return McColorHelperJava.YELLOW + "You got a total of " + McColorHelperJava.GREEN + kills + McColorHelperJava.YELLOW + " Kills."
    }

    enum class KillType {
        Normal, Final, Assists, Final_Assists, Wither_Coins
    }

        private val KILL_DISPLAY = ChatFormatting.AQUA + "Kills: " + ChatFormatting.RED
        private val FINAL_KILL_DISPLAY = ChatFormatting.BLUE + "Final Kills: " + ChatFormatting.RED
        private val ASSISTS_DISPLAY = ChatFormatting.AQUA + "" + ChatFormatting.ITALIC + "Assists: " + ChatFormatting.DARK_GRAY
        private val FINAL_ASSISTS_DISPLAY = ChatFormatting.BLUE + "" + ChatFormatting.ITALIC + "Final Assists: " + ChatFormatting.DARK_GRAY
        private val WITHER_COINS_DISPLAY = ChatFormatting.GOLD + "Wither Coins: "
        @ConfigPropertyBoolean(category = CCategory.HUD, id = "megaWallsKillCounter", comment = "The MW Kill Tracker", def = true)
        @JvmStatic
        var enabled = false

}
