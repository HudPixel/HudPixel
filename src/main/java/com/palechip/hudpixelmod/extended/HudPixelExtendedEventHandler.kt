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

package com.palechip.hudpixelmod.extended

import com.palechip.hudpixelmod.GameDetector
import com.palechip.hudpixelmod.HudPixelMod
import com.palechip.hudpixelmod.HudPixelMod.MODID
import com.palechip.hudpixelmod.config.EasyConfigHandler
import com.palechip.hudpixelmod.extended.fancychat.FancyChat
import com.palechip.hudpixelmod.extended.onlinefriends.OnlineFriendManager
import com.palechip.hudpixelmod.extended.statsviewer.StatsViewerManager
import com.palechip.hudpixelmod.extended.util.IEventHandler
import com.palechip.hudpixelmod.extended.util.gui.FancyListManager
import com.palechip.hudpixelmod.modulargui.ModularGuiHelper
import com.palechip.hudpixelmod.util.DisplayUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiIngameMenu
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.client.event.*
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Mouse
import java.util.*
import java.util.function.Consumer

@SideOnly(Side.CLIENT)
object HudPixelExtendedEventHandler {
    private var lastSystemTime = System.currentTimeMillis()
    private val delay = 20 * 1000 //20s

    @SubscribeEvent
    fun onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent) {
        try {
            // This event isn't bound to the Hypixel Network
            if (eventArgs.modID == MODID) {
                EasyConfigHandler.synchronize()
                ieventBuffer.forEach(Consumer<IEventHandler> { it.onConfigChanged() })
            }
        } catch (e: Exception) {
            HudPixelMod.logWarn("[Extended] An exception occurred in onConfigChanged(). Stacktrace below.")
            e.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onPlayerName(e: PlayerEvent.NameFormat) {
        //StaffManager.onPlayerName(e);
    }

    @SubscribeEvent
    fun onRenderWorldLast(e: RenderWorldLastEvent) {
        try {
            if (!HudPixelMod.isHypixelNetwork) return
        } catch (ex: Exception) {
            HudPixelMod.logWarn("[Extended] An exception occurred in onRenderWorldLast(). Stacktrace below.")
            ex.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onRenderPlayer(e: RenderPlayerEvent.Pre) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.isHypixelNetwork) {
                //just triggeres the statsrenderer if the player is waiting for the game to start
                if (GameDetector.isLobby() && StatsViewerManager.enabled)
                    StatsViewerManager.onRenderPlayer(e)
            }
        } catch (ex: Exception) {
            HudPixelMod.logWarn("[Extended] An exception occurred in onRenderPlayer(). Stacktrace below.")
            ex.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onOpenGui(e: GuiOpenEvent) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.isHypixelNetwork) {
                for (i in ieventBuffer)
                    i.openGUI(Minecraft.getMinecraft().currentScreen)
                if (Minecraft.getMinecraft().thePlayer != null)
                    OnlineFriendManager
                FancyChat.openGui()

            }
        } catch (ex: Exception) {
            HudPixelMod.logWarn("[Extended] An exception occurred in onOpenGui(). Stacktrace below.")
            ex.printStackTrace()
        }

    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChatMessage(e: ClientChatReceivedEvent) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.isHypixelNetwork) {

                for (i in ieventBuffer)
                    i.onChatReceived(e)
                FancyChat.onChat(e)
            }
        } catch (ex: Exception) {
            HudPixelMod.logWarn("[Extended]An exception occurred in onChatMessage(). Stacktrace below.")
            ex.printStackTrace()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onClientTick(e: TickEvent.ClientTickEvent) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.isHypixelNetwork) {

                processTickTimes()

                ieventBuffer.forEach(Consumer<IEventHandler> { it.onClientTick() })

                FancyListManager.processLoadingBar()
                handleMouseScroll()
                //Tick for FancyChat
                FancyChat.onClientTick()
                //Tick for the statsViewerManager
                if (GameDetector.isLobby()) {
                    //System.out.print(GameDetector.getCurrentGameType().getName());
                    StatsViewerManager.onClientTick()
                }

                if (lastSystemTime + delay < System.currentTimeMillis()) {
                    lastSystemTime = System.currentTimeMillis()
                }

            } else if (HudPixelMod.IS_DEBUGGING) {
                FancyChat.onClientTick()
            }
        } catch (ex: Exception) {
            HudPixelMod.logWarn("[Extended]An exception occurred in onClientTick(). Stacktrace below.")
            ex.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onRenderTick(e: RenderGameOverlayEvent.Post) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.isHypixelNetwork && e.type == RenderGameOverlayEvent.ElementType.ALL && !e.isCancelable) {
                ieventBuffer.forEach(Consumer<IEventHandler> { it.onRender() })
                if (FancyChat.enabled) FancyChat.onRenderTick()
            }
        } catch (ex: Exception) {
            HudPixelMod.logWarn("[Extended]An exception occurred in omRenderTick). Stacktrace below.")
            ex.printStackTrace()
        }

    }


    private val clickDelay: Long = 1000

    private var lastTimeClicked: Long = 0
    private var doubleClick = false

    private val iEventArrayList = ArrayList<IEventHandler>()

    @JvmStatic
    fun registerIEvent(iEventHandler: IEventHandler) {
        iEventArrayList.add(iEventHandler)
    }

    @JvmStatic
    fun unregisterIEvent(iEventHandler: IEventHandler) {
        iEventArrayList.remove(iEventHandler)
    }

    @JvmStatic
    val ieventBuffer: ArrayList<IEventHandler>
        get() = ArrayList(iEventArrayList)


    private fun mouseClickEvent() {
        val mc = Minecraft.getMinecraft()
        if (!(mc.currentScreen is GuiIngameMenu || mc.currentScreen is GuiChat)) return

        if (System.currentTimeMillis() > lastTimeClicked + clickDelay && Mouse.isButtonDown(0)) {
            doubleClick = false
            lastTimeClicked = System.currentTimeMillis()

        } else if (System.currentTimeMillis() < lastTimeClicked + clickDelay) {
            if (!Mouse.isButtonDown(0) && !doubleClick) {
                doubleClick = true
                return
            }

            if (Mouse.isButtonDown(0) && doubleClick) {
                doubleClick = false
                val scale = DisplayUtil.mcScale

                val mX = Mouse.getX() / scale
                val mY = (mc.displayHeight - Mouse.getY()) / scale
                for (iE in ieventBuffer) {
                    iE.onMouseClick(mX, mY)
                }
            }
        }
    }

    private fun handleMouseScroll() {
        mouseClickEvent()

        val mc = Minecraft.getMinecraft()
        if (!(mc.currentScreen is GuiIngameMenu || mc.currentScreen is GuiChat)) return

        val scale = DisplayUtil.mcScale

        val mX = Mouse.getX() / scale
        val mY = (mc.displayHeight - Mouse.getY()) / scale
        val i = Mouse.getDWheel()
        for (iE in ieventBuffer) {
            iE.handleMouseInput(i, mX, mY)
        }
        FancyChat.handleMouseInput(i)
    }

    fun onGameStart() {

    }

    fun onGameEnd() {
        ModularGuiHelper.onGameEnd()
    }

    /**
     * prints the message to the clientchat

     * @param message the message
     */
    private fun printMessage(message: String) {
        Minecraft.getMinecraft().ingameGUI.chatGUI.printChatMessage(
                TextComponentString(message))
    }

    private var tick: Short = 0
    private var sec: Short = 0
    private var min: Short = 0

    private fun processTickTimes() {
        tick++
        if (tick >= 20) {
            tick = 0
            sec++
            ieventBuffer.forEach(Consumer<IEventHandler> { it.everySEC() })
            if (sec >= 60) {
                sec = 0
                min++
                ieventBuffer.forEach(Consumer<IEventHandler> { it.everyMIN() })
                if (min >= 60)
                    min = 0
            } else if (sec.toInt() == 5) {
                ieventBuffer.forEach(Consumer<IEventHandler> { it.everyFiveSEC() })
            }
        } else if (tick.toInt() == 10) {
            ieventBuffer.forEach(Consumer<IEventHandler> { it.everyTenTICKS() })
        }

    }

}
