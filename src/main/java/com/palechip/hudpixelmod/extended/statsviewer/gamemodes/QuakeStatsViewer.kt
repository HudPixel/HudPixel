package com.palechip.hudpixelmod.extended.statsviewer.gamemodes

import com.palechip.hudpixelmod.extended.statsviewer.msc.AbstractStatsViewer
import com.palechip.hudpixelmod.util.McColorHelperJava
import net.minecraft.util.text.TextFormatting
import java.util.*

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
class QuakeStatsViewer(uuid: UUID, statsName: String) : AbstractStatsViewer(uuid, statsName), McColorHelperJava {

    private var solo_kills: Int = 0
    private var team_kills: Int = 0
    private var solo_deaths: Int = 0
    private var team_deaths: Int = 0
    private var solo_wins: Int = 0
    private var team_wins: Int = 0
    private var trigger: Float = 0.toFloat()
    private var team_kd: Double = 0.toDouble()
    private var solo_kd: Double = 0.toDouble()


    private fun generateRenderList() {
        getRenderList()?.add(SOLO_WINS + McColorHelperJava.GOLD + this.solo_wins + TEAM_WINS + McColorHelperJava.GOLD + this.team_wins)
        getRenderList()?.add(TEAM_KILLS + McColorHelperJava.GOLD + this.team_kills + TEAM_DEATHS + McColorHelperJava.GOLD + this.team_deaths + TEAM_KD + McColorHelperJava.GOLD + this.team_kd)
        getRenderList()?.add(SOLO_KILLS + McColorHelperJava.GOLD + this.solo_kills + SOLO_DEATHS + McColorHelperJava.GOLD + this.solo_deaths + SOLO_KD + McColorHelperJava.GOLD + this.solo_kd)
        getRenderList()?.add(TRIGGER + McColorHelperJava.GOLD + this.trigger)
    }

    override fun composeStats() {
        this.solo_kills = getInt("kills")!!
        this.team_kills = getInt("kills_teams")!!
        this.solo_deaths = getInt("deaths")!!
        this.team_deaths = getInt("deaths_teams")!!
        this.solo_wins = getInt("wins")!!
        this.team_wins = getInt("wins_teams")!!
        this.trigger = getTrigger(getString("trigger"))

        solo_kd = calculateKD(solo_kills, solo_deaths)
        team_kd = calculateKD(team_kills, team_deaths)

        generateRenderList()

    }

    private fun getTrigger(trigger: String?): Float {

        /**
         * Welcome to language Level 6 .... switching strings is not working here
         */
        if (trigger == "NINE_POINT_ZERO") {
            return 9.0f
        } else if (trigger == "FIVE_POINT_ZERO") {
            return 5.0f
        } else if (trigger == "TWO_POINT_FIVE") {
            return 2.5f
        } else if (trigger == "ONE_POINT_FIVE") {
            return 1.5f
        } else if (trigger == "ONE_POINT_FOUR") {
            return 1.4f
        } else if (trigger == "ONE_POINT_THREE") {
            return 1.3f
        } else if (trigger == "ONE_POINT_TWO") {
            return 1.2f
        } else if (trigger == "ONE_POINT_ONE") {
            return 1.1f
        } else if (trigger == "ONE_POINT_ZERO") {
            return 1.0f
        } else if (trigger == "ZERO_POINT_NINE") {
            return 0.9f
        } else {
            return 666f
        }
    }

    companion object {


        operator fun TextFormatting.plus(string: String) = "$this$string"
        operator fun String.plus(string: TextFormatting) = "$this$string"
        /*
     *Lets add some static finals. Players love static finals.
     */
        private val TRIGGER = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Trigger speed" + McColorHelperJava.D_GRAY + "] "
        private val SOLO_KD = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Solo K/D" + McColorHelperJava.D_GRAY + "] "
        private val TEAM_KD = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Team K/D" + McColorHelperJava.D_GRAY + "] "
        private val SOLO_KILLS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Solo kills" + McColorHelperJava.D_GRAY + "] "
        private val SOLO_DEATHS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Solo deaths" + McColorHelperJava.D_GRAY + "] "
        private val TEAM_KILLS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Team kills" + McColorHelperJava.D_GRAY + "] "
        private val TEAM_DEATHS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Team deaths" + McColorHelperJava.D_GRAY + "] "
        private val SOLO_WINS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Solo wins" + McColorHelperJava.D_GRAY + "] "
        private val TEAM_WINS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.GRAY + "Team wins" + McColorHelperJava.D_GRAY + "] "
    }
}
