package net.unaussprechlich.managedgui.lib.helper

import net.unaussprechlich.managedgui.lib.ManagedGui
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiIngameMenu
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiInventory
import org.lwjgl.input.Mouse

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
object MouseHandler {

    var mcScale: Int = 0
        private set
    private var mX: Int = 0
    private var mY: Int = 0

    fun getmX(): Int {
        return mX
    }

    fun getmY(): Int {
        return mY
    }

    enum class ClickType {
        SINGLE, DOUBLE
    }

    fun onClientTick() {
        val mc = Minecraft.getMinecraft()
        if (mc.gameSettings.guiScale == 0) {
            mcScale = ScaledResolution(mc).scaleFactor
        } else {
            mcScale = mc.gameSettings.guiScale
        }
        val newmX = Mouse.getX() / mcScale
        val newmY = (mc.displayHeight - Mouse.getY()) / mcScale

        if (newmX != mX || newmY != mY) {
            mX = newmX
            mY = newmY
            ManagedGui.childRegistry.onMouseMove(mX, mY)
        }

        if (!(mc.currentScreen is GuiIngameMenu || mc.currentScreen is GuiChat || mc.currentScreen is GuiInventory))
            return
        handleMouseClick()
        handleMouseScroll()

    }

    private val clickDelay: Long = 1000
    private var lastTimeClicked: Long = 0
    private var doubleClick = false

    private fun handleMouseClick() {

        if (System.currentTimeMillis() > lastTimeClicked + clickDelay) {
            if (doubleClick) {
                ManagedGui.childRegistry.onClick(ClickType.SINGLE)
            }
            if (Mouse.isButtonDown(0))
                lastTimeClicked = System.currentTimeMillis()

            doubleClick = false

        } else if (System.currentTimeMillis() < lastTimeClicked + clickDelay) {
            if (!Mouse.isButtonDown(0) && !doubleClick) {
                doubleClick = true
                return
            }

            if (Mouse.isButtonDown(0) && doubleClick) {
                doubleClick = false
                ManagedGui.childRegistry.onClick(ClickType.DOUBLE)
            }
        }
    }

    private fun handleMouseScroll() {

        ManagedGui.childRegistry.onScroll(Mouse.getDWheel())
    }
}
