/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util

import eladkay.hudpixel.util.autotip.Autotip.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.event.events.ScaleChangedEvent

object DisplayUtil {

    private var prevScale = 0

    fun onClientTick() {
        if (prevScale != mcScale) {
            prevScale = mcScale
            GuiManagerMG.postEvent(ScaleChangedEvent(mcScale))
        }
    }

    val mcScale: Int
        get() {
            val scale = Minecraft.getMinecraft().gameSettings.guiScale

            if (scale == 0) return ScaledResolution(mc).scaleFactor
            else            return scale
        }

    val scaledMcWidth: Int
        get() = Minecraft.getMinecraft().displayWidth / mcScale

    val scaledMcHeight: Int
        get() = Minecraft.getMinecraft().displayHeight / mcScale
}
