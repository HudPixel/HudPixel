package com.palechip.hudpixelmod.extended.statsviewer.gamemodes

import com.palechip.hudpixelmod.extended.statsviewer.msc.AbstractStatsViewer
import com.palechip.hudpixelmod.util.McColorHelperJava
import net.minecraft.util.text.TextFormatting

import java.util.UUID

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
class MegaWallsStatsViewer(uuid: UUID, statsName: String) : AbstractStatsViewer(uuid, statsName), McColorHelperJava {
    private var coins: Int = 0
    private var kills: Int = 0
    private var deaths: Int = 0
    private var wins: Int = 0
    private var losses: Int = 0
    private var kd: Double = 0.toDouble()
    private var wl: Double = 0.toDouble()

    private fun generateRenderList() {

        getRenderList()?.add(COINS + McColorHelperJava.GOLD + this.coins + WL + McColorHelperJava.GOLD + this.wl + "%")
        getRenderList()?.add(WINS + McColorHelperJava.GOLD + this.wins + LOSSES + McColorHelperJava.GOLD + this.losses)
        getRenderList()?.add(KILLS + McColorHelperJava.GOLD + this.kills + DEATHS + McColorHelperJava.GOLD + this.deaths + KD + McColorHelperJava.GOLD + this.kd)
    }

    override fun composeStats() {
        this.coins = getInt("coins")!!
        this.wins = getInt("wins")!!
        this.losses = getInt("losses")!!
        this.kills = getInt("kills")!!
        this.deaths = getInt("deaths")!!

        kd = calculateKD(kills, deaths)
        wl = calculateWL(wins, losses).toDouble()

        generateRenderList()
    }

    companion object {

        operator fun TextFormatting.plus(string: String) = "$this$string"
        operator fun String.plus(string: TextFormatting) = "$this$string"

        /*
    *Lets add some static finals. Players love static finals.
    */
        private val COINS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Coins" + McColorHelperJava.D_GRAY + "] "
        private val WINS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Wins" + McColorHelperJava.D_GRAY + "] "
        private val LOSSES = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Losses" + McColorHelperJava.D_GRAY + "] "
        private val KILLS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Kills" + McColorHelperJava.D_GRAY + "] "
        private val DEATHS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Deaths" + McColorHelperJava.D_GRAY + "] "
        private val KD = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "K/D" + McColorHelperJava.D_GRAY + "] "
        private val WL = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "W/L" + McColorHelperJava.D_GRAY + "] "
    }
}
