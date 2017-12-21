/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.chatgui

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.ManagedGui
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.container.register
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefCustomRenderContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefTextContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.ICustomRenderer
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import net.unaussprechlich.project.connect.chatgui.ChatWrapper.isMax
import net.unaussprechlich.project.connect.chatgui.ChatWrapper.move
import net.unaussprechlich.project.connect.chatgui.ChatWrapper.prevX
import net.unaussprechlich.project.connect.chatgui.ChatWrapper.prevY
import net.unaussprechlich.project.connect.chatgui.ChatWrapper.stdHeight
import net.unaussprechlich.project.connect.chatgui.ChatWrapper.stdWidth


object ChatWindowControllerContainer : Container(){

    private val BS = 17

    //RENDERER##########################################################################################################

    private val minIconRenderer = object: ICustomRenderer {
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true
            val s = 2
            val s2 = s*2

            var color = RGBA.P1B1_596068.get()
            if(con.isHover) color = RGBA.WHITE.get()

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + s +1 , yStart + s +1, width - s2 - 2, height - s2 -2, RGBA.BLACK_LIGHT.get(), RGBA.C_161b21.get(), 2)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s, width - s2, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s + 1, 1, height - 2 - s2, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s -1, yStart + s + 1, 1, height - 2 - s2, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + height - s - 1, width - s2, 1, color)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s - 4, width - s2*2 - 2, 1, color)

            return true
        }
    }

    private val maxIconRenderer = object: ICustomRenderer {
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true
            val s = 2
            val s2 = s*2

            var color = RGBA.P1B1_596068.get()
            if(con.isHover) color = RGBA.WHITE.get()

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + s +1 , yStart + s +1, width - s2 - 2, height - s2 -2, RGBA.BLACK_LIGHT.get(), RGBA.C_161b21.get(), 2)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s, width - s2, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s + 1, 1, height - 2 - s2, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s -1, yStart + s + 1, 1, height - 2 - s2, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + height - s - 1, width - s2, 1, color)

            if(isMax){
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 0, yStart + height / 2  +1, 4, 1, color)
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 3  ,yStart + height - s2 - 3, 1, 3, color)

                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 4, yStart + s2 + 3, 4, 1, color)
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 4, yStart + s2 + 0, 1, 3, color)
            } else {
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 2, 4, 1, color)
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 5, 1, 3, color)

                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 5, yStart + s2 + 1, 4, 1, color)
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 2, yStart + s2 + 2, 1, 3, color)
            }

            return true
        }
    }

    fun getMoveConXoffset() : Int{ return moveCon.xOffset }
    fun getMoveConYoffset() : Int{ return moveCon.yOffset }

    private val moveIconRenderer = object: ICustomRenderer {
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true
            val s = 2
            val s2 = s*2

            var color = RGBA.P1B1_596068.get()
            if(con.isHover) color = RGBA.WHITE.get()

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + s +1 , yStart + s +1, width - s2 - 2, height - s2 -2, RGBA.BLACK_LIGHT.get(), RGBA.C_161b21.get(), 2)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s, width - s2, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s + 1, 1, height - 2 - s2, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s -1, yStart + s + 1, 1, height - 2 - s2, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + height - s - 1, width - s2, 1, color)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 2, 3, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 4, 1, 2, color)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 4, yStart + s2 + 1, 3, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 2, yStart + s2 + 2, 1, 2, color)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 4, yStart + height - s2 - 2, 3, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 2, yStart + height - s2 - 4, 1, 2, color)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + s2 + 1, 3, 1, color)
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + s2 + 2, 1, 2, color)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart +  width / 2, yStart + height /2 , 1, 1, color)

            return true
        }
    }

    private val maxCon = DefCustomRenderContainer(maxIconRenderer).apply {
        xOffset = width - BS * 3
        height = BS
    }

    private val moveCon = DefCustomRenderContainer(moveIconRenderer).apply {
        xOffset = width - BS
        height = BS
    }

    private val minCon = DefCustomRenderContainer(minIconRenderer).apply {
        xOffset = width - BS * 2
        height = BS
    }

    private val logoCon = DefTextContainer("[Hud" + EnumChatFormatting.GOLD + "Pixel" + EnumChatFormatting.WHITE + "]").apply {
        yOffset = 3
        xOffset = 4
    }

    init {
        this.width = ChatWrapper.stdWidth

        maxCon.width = BS
        minCon.width = BS
        moveCon.width = BS

        this register moveCon
        this register maxCon
        this register minCon
        this register logoCon

        height = BS
        backgroundRGBA = RGBA.C_161b21.get()

        moveCon.clickedCallback.registerListener{ clickType, _ ->
            if (clickType == MouseHandler.ClickType.DRAG && !isMax)
                move = true
        }

        minCon.clickedCallback.registerListener { clickType, _ ->
            if (clickType == MouseHandler.ClickType.SINGLE){
                ChatWrapper.isVisible = false
                ManagedGui.closeCurrentGUI()
            }
        }

        maxCon.clickedCallback.registerListener { clickType, _ ->
            if(clickType == MouseHandler.ClickType.SINGLE){
                if(isMax){
                    isMax = false
                    ChatWrapper.xOffset = prevX
                    ChatWrapper.yOffset = prevY
                    ChatWrapper.resizeThatThing(stdWidth, stdHeight)
                } else {
                    isMax = true
                    prevX = ChatWrapper.xOffset
                    prevY = ChatWrapper.yOffset
                    ChatWrapper.setXYOffset(5, 5)
                    ChatWrapper.resizeThatThing(DisplayUtil.scaledMcWidth - 10, DisplayUtil.scaledMcHeight - 10)
                }
            }
        }
    }

    override fun doClientTickLocal(): Boolean { return true }
    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean { return true }
    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean { return true }
    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean { return true }
    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean { return true }
    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean { return true }
    override fun <T : Event<*>> doEventBusLocal(iEvent : T): Boolean { return true }
    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean { return true }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        maxCon.xOffset  = width - BS * 3 + 2
        moveCon.xOffset = width - BS
        minCon.xOffset  = width - BS * 2 + 1
        return true
    }
}