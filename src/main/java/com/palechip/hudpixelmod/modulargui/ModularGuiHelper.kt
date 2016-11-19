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
package com.palechip.hudpixelmod.modulargui

import com.google.common.collect.Lists
import com.mojang.realmsclient.gui.ChatFormatting
import com.palechip.hudpixelmod.GameDetector
import com.palechip.hudpixelmod.extended.fancychat.FancyChat
import com.palechip.hudpixelmod.extended.update.UpdateNotifier
import com.palechip.hudpixelmod.extended.util.McColorHelper
import com.palechip.hudpixelmod.modulargui.components.*
import com.palechip.hudpixelmod.modulargui.modules.PlayGameModularGuiProvider
import com.palechip.hudpixelmod.util.plus
import eladkay.modulargui.lib.ModularGuiRegistry
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

@SideOnly(Side.CLIENT)
class ModularGuiHelper : McColorHelper {

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent) {
        for (provider in providers)
            provider.onChatMessage(e.message.unformattedText, e.message.formattedText)
    }

    @SubscribeEvent
    fun onClientTick(e: TickEvent.RenderTickEvent) {
        for (provider in ModularGuiHelper.providers)
            provider.onTickUpdate()
    }

    companion object {
        fun register(vararg providers0: ModularGuiRegistry.Element) {
            for(provider in providers0) {
                ModularGuiRegistry.registerElement(provider)
                providers.add(provider.provider as IHudPixelModularGuiProviderBase)
            }
        }
        val TITLE = ModularGuiRegistry.Element("simp0", SimpleTitleModularGuiProvider)
        //ik i have these backwards
        val FPS = ModularGuiRegistry.Element("Ping", PingAndFpsModularGuiProvider(PingAndFpsModularGuiProvider.PingOrFps.FPS))
        val PING = ModularGuiRegistry.Element("FPS", PingAndFpsModularGuiProvider(PingAndFpsModularGuiProvider.PingOrFps.PING))
        val AVGPROTECTION = ModularGuiRegistry.Element("Average Protection", ArmorProtectionModularGuiProvider)
        val COORDS = ModularGuiRegistry.Element("possimp", CoordsDisplayModularGuiProvider)
        val COIN_COUNTER = ModularGuiRegistry.Element(CoinCounterModularGuiProvider.COINS_DISPLAY_TEXT, CoinCounterModularGuiProvider())
        val TIMER = ModularGuiRegistry.Element(TimerModularGuiProvider.TIME_DISPLAY_MESSAGE, TimerModularGuiProvider)
        val BLITZ_STAR_TRACKER = ModularGuiRegistry.Element(BlitzStarTrackerModularGuiProvider.DISPLAY_MESSAGE, BlitzStarTrackerModularGuiProvider)
        val DEATHMATCH_TRACKER = ModularGuiRegistry.Element("simp1", BlitzDeathmatchNotifierModularGuiProvider)
        val KILLSTREAK_TRACKER = ModularGuiRegistry.Element("simp2", KillstreakTrackerModularGuiProvider)
        val TKR_TIMER = ModularGuiRegistry.Element("simp3", TkrTimerModularGuiProvider)
        val VZ_BALANCE = ModularGuiRegistry.Element(VZBalanceModularGuiProvider.TOTAL_COINS_DISPLAY_TEXT, VZBalanceModularGuiProvider)
        val WALLS2_KILLCOUNTER = ModularGuiRegistry.Element("simp4", WallsKillCounterModularGuiProvider)
        val WALLS3_KILLCOUNTER = ModularGuiRegistry.Element("simp5", MWKillCounterModularGuiProvider)
        val PB_KILLSTREAK_TRACKER = ModularGuiRegistry.Element("simp6", PaintballKillstreakTrackerModularGuiProvider())
        val WARLORDS_DAMAGE_TRACKER = ModularGuiRegistry.Element(ChatFormatting.RED + "Damage", WarlordsDamageAndHealingCounterModularGuiProvider(WarlordsDamageAndHealingCounterModularGuiProvider.Type.Damage))
        val WARLORDS_HEALING_TRACKER = ModularGuiRegistry.Element(ChatFormatting.GREEN + "Healing", WarlordsDamageAndHealingCounterModularGuiProvider(WarlordsDamageAndHealingCounterModularGuiProvider.Type.Healing))
        val PLAY_GAME_MODULE = ModularGuiRegistry.Element(ChatFormatting.DARK_RED + "Game", PlayGameModularGuiProvider)
        var providers: MutableList<IHudPixelModularGuiProviderBase> = Lists.newArrayList<IHudPixelModularGuiProviderBase>()

        init {
            register(TITLE, FPS, PING, AVGPROTECTION, COORDS, COIN_COUNTER, TIMER, BLITZ_STAR_TRACKER, DEATHMATCH_TRACKER, KILLSTREAK_TRACKER, TKR_TIMER, VZ_BALANCE, WALLS2_KILLCOUNTER, WALLS3_KILLCOUNTER, PB_KILLSTREAK_TRACKER, WARLORDS_DAMAGE_TRACKER, WARLORDS_HEALING_TRACKER)
        }



        private fun processAfterstats(): ArrayList<String> {
            val renderList = ArrayList<String>()

            if (!GameDetector.shouldProcessAfterstats()) return renderList
            println("Afterstats!")
            /**
             * bitte was schönes hinmachen :D
             */

            renderList.add(" ")
            renderList.add(McColorHelper.BLUE + UpdateNotifier.SEPARATION_MESSAGE)


            //collects all data
            for (element in ModularGuiRegistry.allElements) {
                if (!element.provider.showElement() || element.provider.getAfterstats() == null || element.provider.getAfterstats().isEmpty())
                    continue //if you shouldn't show it, skip it.
                renderList.add(element.provider.getAfterstats())
            }

            /**
             * bitte was schönes hinmachen :D
             */

            renderList.add(McColorHelper.BLUE + UpdateNotifier.SEPARATION_MESSAGE)
            renderList.add(" ")

            return renderList
        }

        fun onGameEnd() {
            if (!GameDetector.isLobby()) return
            val renderList = processAfterstats()
            Collections.reverse(renderList)
            for (s in renderList) {
                FancyChat.addMessage(s)
                printMessage(s)
            }
        }

        /**
         * prints the message to the clientchat

         * @param message the message
         */
        private fun printMessage(message: String) {
            Minecraft.getMinecraft().ingameGUI.chatGUI.printChatMessage(
                    TextComponentString(message))
        }
    }

}
