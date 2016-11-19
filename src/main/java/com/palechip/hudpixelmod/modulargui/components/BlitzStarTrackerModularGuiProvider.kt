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
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider
import com.palechip.hudpixelmod.util.GameType
import com.palechip.hudpixelmod.util.plus

object BlitzStarTrackerModularGuiProvider : HudPixelModularGuiProvider() {
    private val DURATION: Long = 60000 // = 60s The blitz star ability only lasts 30s. It's intentionally inaccurate.

    private var currentPhase: Phase? = null
    private var owner: String = ""
    private var activatedBlitz: String? = null
    private var startTime: Long = 0

    override fun doesMatchForGame(): Boolean {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.BLITZ)
    }

    override fun setupNewGame() {
        this.currentPhase = Phase.NOT_RELEASED
        this.activatedBlitz = ""
        this.owner = ""
        this.startTime = 0
    }

    override fun onGameStart() {
    }

    override fun onGameEnd() {

    }

    override fun onTickUpdate() {
        // update the time when active
        if (this.currentPhase == Phase.ACTIVE) {
            // expired?
            if (System.currentTimeMillis() - startTime >= DURATION) {
                this.currentPhase = Phase.USED
            }

        }
    }

    override fun onChatMessage(textMessage: String, formattedMessage: String) {
        var textMessage = textMessage
        // filter chat tag
        textMessage = textMessage.replace("[" + GameDetector.currentGameType.nm + "]: ", "")
        // hide message
        if (textMessage.contains("The Blitz Star has been hidden in a random chest!")) {
            this.currentPhase = Phase.HIDDEN
        } else if (textMessage.contains("found the Blitz Star!")) {
            this.owner = textMessage.substring(0, textMessage.indexOf(' ') + 1).replace(" ", "")
            this.currentPhase = Phase.FOUND
        } else if (textMessage.contains(this.owner + " was killed")) {
            this.owner = "" // we could find the killer but it isn't done intentionally.
        } else if (textMessage.contains(" BLITZ! ")) {
            // update the owner
            this.owner = textMessage.substring(0, textMessage.indexOf(' ')).replace(" ", "")
            this.startTime = System.currentTimeMillis()
            this.activatedBlitz = textMessage.substring(textMessage.indexOf('!') + 2).replace(" ", "")
            this.currentPhase = Phase.ACTIVE
        } else if (this.currentPhase != Phase.USED && this.currentPhase != Phase.ACTIVE && textMessage.contains("The Blitz Star has been disabled!")) {
            this.currentPhase = Phase.FORFEIT
        }// it's too close before deathmatch
        // somebody used it
        // the holder was killed
        // somebody found it.
    }

    // display nothing
    // it's hidden
    // tell the player who had it.
    // it's gone
    val renderingString: String
        get() {
            if (currentPhase == null)
                return ""
            when (this.currentPhase) {
                BlitzStarTrackerModularGuiProvider.Phase.NOT_RELEASED -> return "Not released"
                BlitzStarTrackerModularGuiProvider.Phase.HIDDEN -> return ChatFormatting.YELLOW + "Hidden"
                BlitzStarTrackerModularGuiProvider.Phase.FOUND -> return ChatFormatting.LIGHT_PURPLE + if (this.owner!!.isEmpty()) "Found" else this.owner ?: ""
                BlitzStarTrackerModularGuiProvider.Phase.ACTIVE -> return ChatFormatting.RED + (this.owner ?: "") + " -> " + this.activatedBlitz
                BlitzStarTrackerModularGuiProvider.Phase.FORFEIT, BlitzStarTrackerModularGuiProvider.Phase.USED -> return ChatFormatting.GREEN + "Gone"
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
        return ""
    }

    private enum class Phase {
        NOT_RELEASED, HIDDEN, FOUND, ACTIVE, USED, FORFEIT
    }


        val DISPLAY_MESSAGE = ChatFormatting.DARK_GREEN + "Blitz Star"
        @ConfigPropertyBoolean(category = CCategory.HUD, id = "blitzStarTracker", comment = "The Blitz Star Tracker", def = true)
        @JvmStatic
        var enabled = false

}
