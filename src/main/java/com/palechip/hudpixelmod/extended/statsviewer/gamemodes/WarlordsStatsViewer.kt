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
class WarlordsStatsViewer
/**
 * constructor for the StatsViewerFactory
 */
(uuid: UUID, statsName: String) : AbstractStatsViewer(uuid, statsName), McColorHelperJava {
    private var kills: Int = 0
    private var assists: Int = 0
    private var deaths: Int = 0
    private var kd: Double = 0.toDouble()
    private var wl: Int = 0
    private var losses: Int = 0
    private var wins: Int = 0
    private var shamanLevel: Int = 0
    private var warriorLevel: Int = 0
    private var mageLevel: Int = 0
    private var paladinLevel: Int = 0

    /**
     * generates the renderList
     */
    private fun generateRenderList() {
        getRenderList()?.add(KD + McColorHelperJava.GOLD + kd + WL + McColorHelperJava.GOLD + wl + "%")
        getRenderList()?.add(LOS + McColorHelperJava.GOLD + losses + WIN + McColorHelperJava.GOLD + wins)
        getRenderList()?.add(KDA + McColorHelperJava.GOLD + kills + McColorHelperJava.D_GRAY + " | " + McColorHelperJava.GOLD + assists + McColorHelperJava.D_GRAY + " | " + McColorHelperJava.GOLD + deaths)
        getRenderList()?.add(SHA + McColorHelperJava.GOLD + shamanLevel + WAR + McColorHelperJava.GOLD + warriorLevel + PAL + McColorHelperJava.GOLD + paladinLevel + MAG + McColorHelperJava.GOLD + mageLevel)
    }

    /**
     * Function to compose the Jason object into teh right variables
     */
    override fun composeStats() {
        kills = getInt("kills")!!
        assists = getInt("assists")!!
        deaths = getInt("deaths")!!
        wins = getInt("wins")!!
        losses = getInt("losses")!!

        kd = calculateKD(kills, deaths)
        wl = calculateWL(wins, losses)

        shamanLevel = getInt("shaman_health")!! + getInt("shaman_energy")!! + getInt("shaman_cooldown")!!
        +getInt("shaman_critchance")!! + getInt("shaman_critmultiplier")!! + getInt("shaman_skill1")!!
        +getInt("shaman_skill2")!! + getInt("shaman_skill3")!! + getInt("shaman_skill4")!!
        +getInt("shaman_skill5")!!

        warriorLevel = getInt("warrior_health")!! + getInt("warrior_energy")!! + getInt("warrior_cooldown")!!
        +getInt("warrior_critchance")!! + getInt("warrior_critmultiplier")!! + getInt("warrior_skill1")!!
        +getInt("warrior_skill2")!! + getInt("warrior_skill3")!! + getInt("warrior_skill4")!!
        +getInt("warrior_skill5")!!

        mageLevel = getInt("mage_health")!! + getInt("mage_energy")!! + getInt("mage_cooldown")!!
        +getInt("mage_critchance")!! + getInt("mage_critmultiplier")!! + getInt("mage_skill1")!!
        +getInt("mage_skill2")!! + getInt("mage_skill3")!! + getInt("mage_skill4")!!
        +getInt("mage_skill5")!!

        paladinLevel = getInt("paladin_health")!! + getInt("paladin_energy")!! + getInt("paladin_cooldown")!!
        +getInt("paladin_critchance")!! + getInt("paladin_critmultiplier")!! + getInt("paladin_skill1")!!
        +getInt("paladin_skill2")!! + getInt("paladin_skill3")!! + getInt("paladin_skill4")!!
        +getInt("paladin_skill5")!!

        generateRenderList()
    }

    companion object {

        operator fun TextFormatting.plus(string: String) = "$this$string"
        operator fun String.plus(string: TextFormatting) = "$this$string"
        /**
         * Some static-final stuff to make the code cleaner ....
         */
        private val SHA = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "SHA" + McColorHelperJava.D_GRAY + "] "
        private val WAR = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "WAR" + McColorHelperJava.D_GRAY + "] "
        private val PAL = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "PAL" + McColorHelperJava.D_GRAY + "] "
        private val MAG = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "MAG" + McColorHelperJava.D_GRAY + "] "
        private val KDA = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "K" + McColorHelperJava.D_GRAY + "|"+ McColorHelperJava.WHITE + "A" + McColorHelperJava.D_GRAY + "|" + McColorHelperJava.WHITE + "D" + McColorHelperJava.D_GRAY + "] "
        private val LOS = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "LOS" + McColorHelperJava.D_GRAY + "] "
        private val WIN = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "WIN" + McColorHelperJava.D_GRAY + "] "
        private val KD = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "K/D" + McColorHelperJava.D_GRAY + "] "
        private val WL = McColorHelperJava.D_GRAY + " [" + McColorHelperJava.WHITE + "Wl" + McColorHelperJava.D_GRAY + "] "
    }

}
