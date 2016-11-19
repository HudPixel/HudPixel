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
package com.palechip.hudpixelmod.extended.util.gui

import com.mojang.realmsclient.gui.ChatFormatting
import com.palechip.hudpixelmod.util.plus
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import java.util.*

@SideOnly(Side.CLIENT)
abstract class FancyListObject {
    protected var renderLine1: String? = null
    protected var renderLine2: String? = null
    protected var renderLineSmall: String? = null
    protected var renderPicture: String? = null
    protected var resourceLocation: ResourceLocation? = null
    protected var fancyListObjectButtons = ArrayList<FancyListButton>()
    private var xStart: Float = 0.toFloat()
    private var yStart: Float = 0.toFloat()
    private var renderRight: Boolean = false
    private var isHover: Boolean = false

    protected fun addButton(fcob: FancyListButton) {
        fancyListObjectButtons.add(fcob)
    }


    /**
     * renders the loading animation

     * @param xStart startposition of the friendsdisplay
     * *
     * @param yStart startposition of the friendsdisplay
     */
    private fun renderLoadingBar(xStart: Float, yStart: Float) {

        val a = 2
        val b = 1
        val alpha = 0.8f

        when (loadingBar) {
            0 -> {
                RenderUtils.renderBoxWithColor((xStart.toShort() + 7).toDouble(), (yStart.toShort() + 9).toDouble(), 2.0, (6.toShort() + a).toDouble(), 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 11).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 15).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, alpha)
            }
            1 -> {
                RenderUtils.renderBoxWithColor((xStart + 7).toDouble(), (yStart + 9).toDouble(), 2.0, (6 + b).toDouble(), 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 11).toDouble(), (yStart + 9).toDouble(), 2.0, (6 + a).toDouble(), 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 15).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, alpha)
            }
            2 -> {
                RenderUtils.renderBoxWithColor((xStart + 7).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 11).toDouble(), (yStart + 9).toDouble(), 2.0, (6 + b).toDouble(), 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 15).toDouble(), (yStart + 9).toDouble(), 2.0, (6 + a).toDouble(), 1f, 1f, 1f, alpha)
            }
            3 -> {
                RenderUtils.renderBoxWithColor((xStart + 7).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 11).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 15).toDouble(), (yStart + 9).toDouble(), 2.0, (6 + b).toDouble(), 1f, 1f, 1f, alpha)
            }
            else -> {
                RenderUtils.renderBoxWithColor((xStart + 7).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, 0.8f)
                RenderUtils.renderBoxWithColor((xStart + 11).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, alpha)
                RenderUtils.renderBoxWithColor((xStart + 15).toDouble(), (yStart + 9).toDouble(), 2.0, 6.0, 1f, 1f, 1f, alpha)
            }
        }
    }

    /**
     * You have to call it each render tick from the FancyListManager

     * @param small  set it to true if you want the display small
     * *
     * @param xStart xStart
     * *
     * @param yStart yStart
     */
    internal fun onRenderTick(small: Boolean, xStart: Float, yStart: Float, renderRightSide: Boolean) {
        this.xStart = xStart
        this.yStart = yStart
        this.renderRight = renderRightSide
        if (small)
            renderBoosterSMALL()
        else
            renderBoosterSHOWN()
    }

    internal fun onMouseInput(mX: Int, mY: Int) {
        val buttonOffsetX1: Float
        val buttonOffsetX2: Float
        if (!renderRight) {
            buttonOffsetX1 = xStart
            buttonOffsetX2 = xStart + 140f + (fancyListObjectButtons.size * 24).toFloat()
        } else {
            buttonOffsetX1 = xStart - fancyListObjectButtons.size * 24
            buttonOffsetX2 = xStart + 140
        }

        if (mX > xStart && mX < xStart + 140 && mY > yStart && mY < yStart + 24) {
            isHover = true
            for (fcob in fancyListObjectButtons)
                fcob.isHover = false
        } else if (isHover && mX > buttonOffsetX1 && mX < buttonOffsetX2 && mY > yStart && mY < yStart + 24) {
            isHover = true
            for (fcob in fancyListObjectButtons)
                fcob.onMouseInput(mX, mY)
        } else
            isHover = false
    }

    fun onClientTick() {
        onTick()
    }

    abstract fun onTick()

    /**
     * This method draws the display in the smaller version and just with the first line of
     * the string!
     */
    private fun renderBoosterSMALL() {
        val fontRenderer = FMLClientHandler.instance().client.fontRendererObj

        var xStart = this.xStart
        //System.out.print(renderRight);
        if (renderRight) {
            xStart = xStart + 10
        }

        RenderUtils.renderBoxWithColor(xStart.toDouble(), yStart.toDouble(), 130.0, 12.0, 0f, 0f, 0f, 0.3f)//draws the background

        if (resourceLocation == null)
            renderLoadingBar(xStart, yStart)
        else
            RenderUtils.drawModalRectWithCustomSizedTexture(//draws the texture
                    Math.round(xStart), Math.round(yStart), 0, 0,
                    12, 12, 12, 12, resourceLocation, 1f)

        fontRenderer.drawStringWithShadow(renderLineSmall!!, xStart + 14, yStart + 2, 0xffffff) //draws the first line string
    }

    /**
     * This method draws the display with all it's components
     */
    private fun renderBoosterSHOWN() {
        val fontRenderer = FMLClientHandler.instance().client.fontRendererObj

        if (!isHover)
            RenderUtils.renderBoxWithColor(xStart.toDouble(), yStart.toDouble(), 140.0, 24.0, 0f, 0f, 0f, 0.3f) //draws the background
        else {
            RenderUtils.renderBoxWithColor(xStart.toDouble(), yStart.toDouble(), 140.0, 24.0, 1f, 1f, 1f, 0.2f)
            var xStartB: Float
            if (!renderRight)
                xStartB = xStart + 140
            else
                xStartB = xStart - 24


            val yStartB = yStart
            for (fcob in fancyListObjectButtons) {
                fcob.onRender(xStartB, yStartB)
                if (!renderRight)
                    xStartB += 24f
                else
                    xStartB -= 24f
            }

        }

        if (resourceLocation == null)
            renderLoadingBar(xStart, yStart)
        else
            RenderUtils.drawModalRectWithCustomSizedTexture(//draws the image shown
                    Math.round(xStart), Math.round(yStart), 0, 0,
                    24, 24, 24, 24, resourceLocation, 1f)

        if (renderPicture != ChatFormatting.WHITE + "") { //draws a background over the image if there is a string to render
            RenderUtils.renderBoxWithColor(xStart.toDouble(), (yStart + 12).toDouble(), 24.0, 9.0, 0f, 0f, 0f, 0.5f)
        }

        fontRenderer.drawStringWithShadow(renderLine1!!, xStart + 28, yStart + 4, 0xffffff) //draws the first line
        fontRenderer.drawStringWithShadow(renderLine2!!, xStart + 28, yStart + 13, 0xffffff) //draws the second line
        fontRenderer.drawString(renderPicture!!, Math.round(xStart), Math.round(yStart + 13), 0xffffff) //draws the string over the image

    }

    fun onMouseClick(mX: Int, mY: Int) {
        for (fcob in fancyListObjectButtons)
            fcob.onMouseClick(mX, mY)
    }

    companion object {

        var loadingBar: Int = 0
    }
}
