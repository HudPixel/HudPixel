/* *****************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 * <p>
 * Copyright (c) 2016 unaussprechlich and contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package net.unaussprechlich.managedgui.lib.util

import com.palechip.hudpixelmod.config.GeneralConfigSettings
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.unaussprechlich.managedgui.lib.util.storage.StorageFourSide
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
object RenderUtils {

    fun renderItemStackWithText(id: Int, meta: Int, xStart: Int, yStart: Int, overlay: String?) {
        GL11.glPushMatrix()
        RenderHelper.enableStandardItemLighting()
        GlStateManager.color(0.0f, 0.0f, 32.0f)
        val mc = Minecraft.getMinecraft()
        val iStack = ItemStack(Item.getItemById(id))
        if (meta > 0) iStack.itemDamage = meta
        val renderItem = mc.renderItem
        renderItem.renderItemAndEffectIntoGUI(iStack, xStart, yStart)
        renderItem.renderItemOverlayIntoGUI(mc.fontRendererObj, iStack, xStart, yStart, overlay)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableBlend()
        GL11.glPopMatrix()
    }

    fun renderItemStack(iStack: ItemStack, xStart: Int, yStart: Int) {
        GL11.glPushMatrix()
        RenderHelper.enableStandardItemLighting()

        GlStateManager.color(0.0f, 0.0f, 32.0f)
        val renderItem = Minecraft.getMinecraft().renderItem
        renderItem.renderItemAndEffectIntoGUI(iStack, (xStart + 1).toShort().toInt(), (yStart + 1).toShort().toInt())

        RenderHelper.disableStandardItemLighting()

        val dur = 1 - iStack.item.getDurabilityForDisplay(iStack)

        if (iStack.item.showDurabilityBar(iStack))
            renderBoxWithColor((xStart + 1).toDouble(), (yStart + 16).toDouble(), 16 * dur, 1.0, (1 - dur).toFloat(), dur.toFloat(), 0f, 1f)

        GlStateManager.disableBlend()
        GL11.glPopMatrix()
    }

    fun renderItemStackHudBackground(iStack: ItemStack, xStart: Int, yStart: Int) {
        renderBoxWithHudBackground(xStart + 1, yStart + 1, 16, 16)
        renderItemStack(iStack, xStart, yStart)
    }

    fun renderBorder(xStart: Short, yStart: Short, innerHight: Short, innerWidth: Short, border: StorageFourSide, color: ColorRGBA) {
        renderBoxWithColor(xStart.toFloat(), yStart.toFloat(), (innerWidth.toInt() + border.LEFT.toInt() + border.RIGHT.toInt()).toShort().toInt(), border.TOP.toInt(), color)
        renderBoxWithColor(xStart.toFloat(), (yStart.toInt() + border.TOP.toInt() + innerHight.toInt()).toShort().toFloat(), (innerWidth.toInt() + border.LEFT.toInt() + border.RIGHT.toInt()).toShort().toInt(), border.BOTTOM.toInt(), color)
        renderBoxWithColor(xStart.toFloat(), (yStart + border.TOP).toShort().toFloat(), border.LEFT.toInt(), innerHight.toInt(), color)
        renderBoxWithColor((xStart.toInt() + border.LEFT.toInt() + innerWidth.toInt()).toShort().toFloat(), (yStart + border.TOP).toShort().toFloat(), border.RIGHT.toInt(), innerHight.toInt(), color)
    }

    fun renderBoxWithHudBackground(xStart: Int, yStart: Int, width: Int, height: Int) {
        renderBoxWithColor(
                xStart.toDouble(), yStart.toDouble(), width.toDouble(), height.toDouble(),
                GeneralConfigSettings.hudRed.toFloat() / 255,
                GeneralConfigSettings.hudGreen.toFloat() / 255,
                GeneralConfigSettings.hudBlue.toFloat() / 255,
                GeneralConfigSettings.hudAlpha.toFloat() / 255)
    }


    /**
     * helper-method, that draws a box wth semitransparent background. Should be with the onRender
     * event.

     * @param xStart left upper x-cord
     * *
     * @param yStart left upper y-cord
     * *
     * @param width  with of the box
     * *
     * @param height height of the box
     */
    fun renderBox(xStart: Short, yStart: Short, width: Short, height: Short) {
        renderBoxWithColor(xStart.toFloat(), yStart.toFloat(), width.toInt(), height.toInt(), ColorRGBA(0f, 0f, 0f, 0.5f))
    }

    fun renderBoxWithColor(xStart: Float, yStart: Float, width: Int, height: Int, color: ColorRGBA) {

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.buffer

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(color.RED, color.GREEN, color.BLUE, color.ALPHA)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(xStart.toDouble(), (yStart + height).toDouble(), 0.0).endVertex()
        worldrenderer.pos((xStart + width).toDouble(), (yStart + height).toDouble(), 0.0).endVertex()
        worldrenderer.pos((xStart + width).toDouble(), yStart.toDouble(), 0.0).endVertex()
        worldrenderer.pos(xStart.toDouble(), yStart.toDouble(), 0.0).endVertex()
        tessellator.draw()

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

    }

    fun renderBoxWithColor(xStart: Double, yStart: Double, width: Double, height: Double,
                           red: Float, green: Float, blue: Float, alpha: Float) {

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.buffer

        GlStateManager.popMatrix()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

        GlStateManager.color(red, green, blue, alpha)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(xStart, yStart + height, 0.0).endVertex()
        worldrenderer.pos(xStart + width, yStart + height, 0.0).endVertex()
        worldrenderer.pos(xStart + width, yStart, 0.0).endVertex()
        worldrenderer.pos(xStart, yStart, 0.0).endVertex()
        tessellator.draw()

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.pushMatrix()

    }


    /**
     * Draws a textured rectangle at z = 0. Args: x, y width, height, textureWidth, textureHeight
     */
    fun drawModalRectWithCustomSizedTexture(x: Short, y: Short, width: Short, height: Short, resourceLocation: ResourceLocation, alpha: Float?) {

        GlStateManager.popMatrix()
        Minecraft.getMinecraft().textureManager.bindTexture(resourceLocation)

        //this line took me like 3 hours to fine out the color wasn't resetting :D
        GlStateManager.color(1f, 1f, 1f, alpha!!)

        val f = 1.0f / height
        val f1 = 1.0f / width
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.buffer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX)
        worldrenderer.pos(x.toDouble(), (y + height).toDouble(), 0.0).tex((width * f).toDouble(), ((height + height) * f1).toDouble()).endVertex()
        worldrenderer.pos((x + width).toDouble(), (y + height).toDouble(), 0.0).tex(((width + width) * f).toDouble(), ((height + height) * f1).toDouble()).endVertex()
        worldrenderer.pos((x + width).toDouble(), y.toDouble(), 0.0).tex(((width + width) * f).toDouble(), (height * f1).toDouble()).endVertex()
        worldrenderer.pos(x.toDouble(), y.toDouble(), 0.0).tex((width * f).toDouble(), (height * f1).toDouble()).endVertex()
        tessellator.draw()

        GlStateManager.pushMatrix()

    }


    fun drawModalRectWithCustomSizedTexture(round: Int, round1: Int, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, resourceLocation: ResourceLocation, v: Float) {
        drawModalRectWithCustomSizedTexture(round.toShort(), round1.toShort(), i4.toShort(), i5.toShort(), resourceLocation, v)
    }

    fun renderBox(xStart: Int, yStart: Int, fieldWidth: Int, i: Int) {
        renderBox(xStart.toShort(), yStart.toShort(), fieldWidth.toShort(), i.toShort())
    }
}
