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

import com.palechip.hudpixelmod.GameDetector
import com.palechip.hudpixelmod.HudPixelMod
import com.palechip.hudpixelmod.chat.WarlordsDamageChatFilter
import com.palechip.hudpixelmod.config.CCategory
import com.palechip.hudpixelmod.config.ConfigPropertyBoolean
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider
import com.palechip.hudpixelmod.util.GameType
import com.palechip.hudpixelmod.util.McColorHelperJava
import net.minecraft.util.text.TextFormatting
import java.util.regex.Pattern

class WarlordsDamageAndHealingCounterModularGuiProvider(private val type: WarlordsDamageAndHealingCounterModularGuiProvider.Type) : HudPixelModularGuiProvider(), McColorHelperJava {
    private var count: Int = 0

    override fun doesMatchForGame(): Boolean {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.WARLORDS) && !GameDetector.isLobby() && enabled
    }

    override fun setupNewGame() {
        this.count = 0
    }

    operator fun TextFormatting.plus(string: String) = "$this$string"
    override fun onGameStart() {
    }

    override fun onGameEnd() {
    }

    override fun onTickUpdate() {
    }

    override fun onChatMessage(textMessage: String, formattedMessage: String) {
        // incoming
        if (textMessage.startsWith(WarlordsDamageChatFilter.give)) {
            // healing
            if (this.type == Type.Healing && textMessage.contains(WarlordsDamageChatFilter.healing)) {
                this.count += getDamageOrHealthValue(textMessage)
            }
            // damage
            if (this.type == Type.Damage && !textMessage.contains(WarlordsDamageChatFilter.absorption) && !textMessage.contains(WarlordsDamageChatFilter.healing)) {
                this.count += getDamageOrHealthValue(textMessage)
            }
        }
    }

    // if the played class doens't have access to healing, they won't be bothered.
    // format into xxx.x
    //eladkay: yes, I know.
    val renderingString: String
        get() {
            if (this.count == 0 && this.type == Type.Healing) {
                return ""
            }
            val formatted = Math.round(this.count / 100.0) / 10.0
            when (this.type) {
                WarlordsDamageAndHealingCounterModularGuiProvider.Type.Healing -> return "${formatted}k"
                WarlordsDamageAndHealingCounterModularGuiProvider.Type.Damage -> return "${formatted}k"
            }
            return ""
        }

    override fun showElement(): Boolean {
        return doesMatchForGame()
    }

    override fun content(): String {
        return renderingString
    }

    override fun ignoreEmptyCheck(): Boolean {
        return false
    }

    override fun getAfterstats(): String {
        if (type == Type.Damage) {
            return TextFormatting.YELLOW + "You dealt a total of " + TextFormatting.RED + count + " damage.\n"
        } else {
            return TextFormatting.YELLOW + "You dealt a total of " + TextFormatting.GREEN + count + " healing."
        }
    }

    enum class Type {
        Damage, Healing
    }

    companion object {
        @ConfigPropertyBoolean(category = CCategory.WARLORDS, id = "warlordsDamageAndHealthCounter", comment = "The Warlords Damage And Health Tracker", def = true)
        @JvmStatic
        var enabled = false

        /**
         * @return The damage or health. 0 in case of failure.
         */
        fun getDamageOrHealthValue(message: String): Int {
            var message = message
            try {
                // filter !, which highlights critical damage/health
                message = message.replace("!", "")

                // do some regex magic
                val p = Pattern.compile("\\s[0-9]+\\s")
                val m = p.matcher(message)
                if (!m.find()) {
                    // We failed :(
                    return 0
                }
                // save the result
                var result = m.group()
                // if there is a second match, we'll use that because the first was an all number username in this case
                if (m.find()) {
                    result = m.group()
                }

                // and cast it into an integer (without whitespace)
                return Integer.valueOf(result.replace(" ", ""))!!
            } catch (e: Exception) {
                HudPixelMod.logDebug("Failed to extract damage from this message: " + message)
            }

            // We failed :(
            return 0
        }
    }
}
