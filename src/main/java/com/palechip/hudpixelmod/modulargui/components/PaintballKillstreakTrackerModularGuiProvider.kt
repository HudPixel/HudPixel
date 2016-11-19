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
import com.palechip.hudpixelmod.modulargui.SimpleHudPixelModularGuiProvider
import com.palechip.hudpixelmod.util.GameType
import com.palechip.hudpixelmod.util.plus
import net.minecraftforge.fml.client.FMLClientHandler
import java.util.*

class PaintballKillstreakTrackerModularGuiProvider : SimpleHudPixelModularGuiProvider {
    private var renderedString: String? = null
    private var listenedKillstreak: String = ""
    private var isTimed: Boolean = false
    private var isActive: Boolean = false
    private var startTime: Long = 0
    private var duration: Long? = 0
    private var hasCooldown = false
    private var isCoolingDown: Boolean = false
    private var cooldownDependantKillstreak: String = ""

    /**
     * Setup a Timer for a specific killstreak

     * @param killstreak                  The listened killsteak
     * *
     * @param isTimed                     True if the killstreak has a duration, otherwise it'll enter cooldown state upon activation
     * *
     * @param hasCooldown                 True if there is a cooldown for the usage of the killstreak
     * *
     * @param cooldownDependantKillstreak Specifys that this killstreak also enters cooldown when the given killstreak is cooling down
     */
    constructor(killstreak: String, isTimed: Boolean, hasCooldown: Boolean, cooldownDependantKillstreak: String) {
        this.listenedKillstreak = killstreak
        this.isTimed = isTimed
        // look if we have saved a duration value from past instances
        if (this.isTimed && durationStorage.containsKey(this.listenedKillstreak)) {
            // set it so we don't have to measure again
            this.duration = durationStorage[this.listenedKillstreak]
        }
        this.hasCooldown = hasCooldown
        // add the string even when it is empty. It's better than null
        this.cooldownDependantKillstreak = cooldownDependantKillstreak
        if (!cooldownDependantKillstreak.isEmpty()) {
            // if there is a killstreak which this depends on, there also is one which depends on this.
            // for this reason we add this to the list of cooldownDependantKillstreas
            cooldownDependantKillstreaks.put(this.listenedKillstreak, this)
        }
    }

    constructor() {
        println("PB killstreak tracker not implemented!")
    }

    override fun doesMatchForGame(): Boolean {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.PAINTBALL)
    }

    override fun setupNewGame() {
        this.renderedString = ""
    }

    override fun onGameStart() {
    }

    override fun onGameEnd() {
        this.isActive = false
        this.isCoolingDown = false
    }

    override fun onTickUpdate() {
        if (this.isActive) {
            // only if we already measured the length
            if (this.duration != null && this.duration!! > 0L) {
                val remainingTime = this.duration!! - (System.currentTimeMillis() - this.startTime)
                this.renderedString = this.getColorForTime(remainingTime / 1000) + remainingTime / 1000 + "s"
            } else {
                val timePast = System.currentTimeMillis() - this.startTime
                this.renderedString = ChatFormatting.YELLOW + "" + timePast / 1000 + "s (m)"
            }
        }
    }

    override fun onChatMessage(textMessage: String, formattedMessage: String) {
        // test for starting
        if (textMessage.contains(FMLClientHandler.instance().client.session.username + " activated " + this.listenedKillstreak)) {
            this.startTime = System.currentTimeMillis()
            if (this.isTimed) {
                this.isActive = true
            } else if (this.hasCooldown) {
                this.isCoolingDown = true
            }
        }
        // test for expiring
        if (textMessage.contains("Your " + this.listenedKillstreak + " has expired!")) {
            this.isActive = false
            // update the duration
            this.duration = System.currentTimeMillis() - this.startTime
            // and save it for future instances
            durationStorage.put(this.listenedKillstreak, this.duration!!)
            this.renderedString = ""
            if (this.hasCooldown) {
                // Start the cooldown
                this.isCoolingDown = true
            }
        }
        // test if the cooldown is over
        if (textMessage.contains("You can now use " + this.listenedKillstreak + " again!")) {
            this.isCoolingDown = false
        }
    }

    /*
        if(this.isActive) {
            return ACTIVE_SIGN +  ChatFormatting.DARK_PURPLE + this.listenedKillstreak + ": " + renderedString;
        } else if(this.isCoolingDown ||
                ((!this.cooldownDependantKillstreak.isEmpty() && cooldownDependantKillstreaks.containsKey(this.cooldownDependantKillstreak)) && cooldownDependantKillstreaks.get(this.cooldownDependantKillstreak).isCoolingDown)) {
            // the listened killstreak will be RED because the color from COOLDOWN_SIGN isn't reset
            return COOLDOWN_SIGN + " " + this.listenedKillstreak;
        } else  {
            return "";
        }*/ val renderingString: String
        get() = ""

    private fun getColorForTime(time: Long): String {
        if (time >= 10) {
            // GREEN
            return ChatFormatting.GREEN.toString()
        } else if (time >= 5) {
            // orange
            return ChatFormatting.GOLD.toString()
        } else {
            // RED and bold
            return (ChatFormatting.RED + "" + ChatFormatting.BOLD).toString()
        }
    }


    override fun showElement(): Boolean {
        return doesMatchForGame() && !GameDetector.isLobby()
    }

    override fun content(): String {
        return renderingString
    }

    override fun getAfterstats(): String {
        return ""
    }

        private val COOLDOWN_SIGN = ChatFormatting.RED + "\u2717" // fancy x
        private val ACTIVE_SIGN = ChatFormatting.GREEN + "\u2713" // isHypixelNetwork mark
        private val cooldownDependantKillstreaks = HashMap<String, PaintballKillstreakTrackerModularGuiProvider>()
        private val durationStorage = HashMap<String, Long>()

}
