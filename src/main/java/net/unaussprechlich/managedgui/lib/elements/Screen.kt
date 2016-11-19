package net.unaussprechlich.managedgui.lib.elements

import net.unaussprechlich.managedgui.lib.helper.Child
import net.unaussprechlich.managedgui.lib.helper.ChildRegistry
import net.unaussprechlich.managedgui.lib.helper.MouseHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.event.ClientChatReceivedEvent

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
class Screen(private val id: String, private val guiScreen: GuiScreen) : Child {

    private val childRegistry = ChildRegistry()

    private fun checkDisplayType(): Boolean {
        return Minecraft.getMinecraft().currentScreen!!.javaClass == guiScreen.javaClass
    }

    override fun onClientTick() {
        if (!checkDisplayType()) return
        childRegistry.onClientTick()
    }

    override fun onRenderTick() {
        if (!checkDisplayType()) return
        childRegistry.onRender()
    }

    override fun onChatMessage(e: ClientChatReceivedEvent) {
        if (!checkDisplayType()) return
        childRegistry.onChatMessage(e)
    }

    override fun onClick(clickType: MouseHandler.ClickType) {
        if (!checkDisplayType()) return
        childRegistry.onClick(clickType)
    }

    override fun onScroll(i: Int) {
        if (!checkDisplayType()) return
        childRegistry.onScroll(i)
    }

    override fun onMouseMove(mX: Int, mY: Int) {
        if (!checkDisplayType()) return
        childRegistry.onMouseMove(mX, mY)
    }
}
