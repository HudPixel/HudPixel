/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.container

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.*
import org.lwjgl.opengl.Display

/**
 * ContainerFrame Created by unaussprechlich on 26.02.2017.
 * Description:
 * Just 5h of work xD
 */
abstract class ContainerFrame(width: Int, height: Int, color: ColorRGBA) : Container() {

    private val frameBuffer: FrameBufferObj
    private var requireFrameUpdate = true
    var isResizeable = true
    private var isHooverResize = false

    constructor(width: Int, height: Int, minWidth: Int, minHeight: Int, color: ColorRGBA) : this(width, height, color) {
        this.minWidth = minWidth
        this.minHeight = minHeight
    }

    init {
        this.width = width
        this.height = height
        backgroundRGBA = color
        frameBuffer = FrameBufferObj(this.width * DisplayUtil.mcScale, this.height * DisplayUtil.mcScale, false)
        frameBuffer.setFramebufferColor(color.redf, color.greenf, color.bluef, color.alphaf)
    }

    fun updateFrame() {
        requireFrameUpdate = true
    }

    private var isResize = false

    override fun doClick(clickType: MouseHandler.ClickType): Boolean {
        if (clickType == MouseHandler.ClickType.DRAG && isResizeable) {
            if (checkIfMouseOver(xStart + width - 10, yStart + height - 10,
                    10, 10)) {
                isResize = true
            }
        }

        if (clickType == MouseHandler.ClickType.DROP && isResizeable) {
            if (isResize) {
                isResize = false
                onResize()
            }
        }
        return super.doClick(clickType)
    }

    override fun doMouseMove(mX: Int, mY: Int): Boolean {
        if (isResizeable)
            isHooverResize = checkIfMouseOver(xStart + width - 10, yStart + height - 10,
                    10, 10)

        return super.doMouseMove(mX, mY)
    }

    /**
     * Does fix the offset of the framebuffer for the mouse movement
     */
    override fun onMouseMove(mX: Int, mY: Int) {
        if (!doMouseMove(mX, mY)) return
        if (childs.isEmpty()) return

        val newMX = mX - xStart
        val newMY = mY + (DisplayUtil.scaledMcHeight - yStart - height)

        childs.forEach { child -> child.onMouseMove(newMX, newMY) }
    }

    override fun doResize(): Boolean {
        frameBuffer.createDeleteFramebuffer(width * DisplayUtil.mcScale, height * DisplayUtil.mcScale)
        return super.doResize()
    }

    override fun <T : Event<*>> doEventBus(event: T): Boolean {
        if (event.id == EnumDefaultEvents.SCALE_CHANGED.get()) {
            frameBuffer.createFramebuffer(width * DisplayUtil.mcScale, height * DisplayUtil.mcScale)
        }
        return super.doEventBus(event)
    }

    override fun doClientTick(): Boolean {
        if (isResize) {
            if (MouseHandler.mX - xStart + 5 > minWidth)
                width = MouseHandler.mX - xStart + 5
            if (MouseHandler.mY - yStart + 5 > minHeight)
                height = MouseHandler.mY - yStart + 5
        }
        updateFrame()
        return super.doClientTick()
    }

    override fun doRender(xStart: Int, yStart: Int): Boolean {
        updateXYStart(xStart, yStart)
        if (!isVisible) return false

        if (isResize) {
            RenderUtils.renderBoxWithColor(getXStart(), getYStart(), width, height, backgroundRGBA)
            RenderUtils.iconRender_resize(getXStart() + width, getYStart() + height, RGBA.WHITE.get())

            return false
        }

        GlStateManager.pushMatrix()
        frameBuffer.framebufferRenderTexture(getXStart(), getYStart(), width, height)
        GlStateManager.popMatrix()

        if (!requireFrameUpdate) return false

        val x = 0
        val y = DisplayUtil.scaledMcHeight - height

        GlStateManager.pushMatrix()
        GlStateManager.pushAttrib()

        frameBuffer.framebufferClear()

        //frameBuffer.deleteFramebuffer();
        //frameBuffer = new FrameBufferObj(getWidth() * DisplayUtil.getMcScale(), getHeight() * DisplayUtil.getMcScale(), false);
        //frameBuffer = new FrameBufferObj(Display.getWidth(), Display.getHeight(), false);

        frameBuffer.bindFramebuffer(false)
        GlStateManager.viewport(0, 0, Display.getWidth(), Display.getHeight())

        if (this.doRenderTickLocal(x, y, width, height, EnumEventState.PRE)) {
            for (child in childs) {
                child.onRender(x, y)
            }
        }

        this.doRenderTickLocal(x, y, width, height, EnumEventState.POST)

        if (isResizeable) {
            if (isHooverResize)
                RenderUtils.iconRender_resize(x + width, y + height, RGBA.WHITE.get())
            else
                RenderUtils.iconRender_resize(x + width, y + height, RGBA.P1B1_596068.get())
        }


        frameBuffer.unbindFramebuffer()

        Minecraft.getMinecraft().framebuffer.bindFramebuffer(true)

        GlStateManager.popMatrix()
        GlStateManager.popAttrib()

        return false
    }
}
