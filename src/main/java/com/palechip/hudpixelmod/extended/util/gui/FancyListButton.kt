package com.palechip.hudpixelmod.extended.util.gui

import net.minecraft.util.ResourceLocation

import net.unaussprechlich.managedgui.lib.util.RenderUtils.drawModalRectWithCustomSizedTexture
import net.unaussprechlich.managedgui.lib.util.RenderUtils.renderBoxWithColor
import java.lang.Math.round

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
abstract class FancyListButton protected constructor(private val r: Float, private val g: Float, private val b: Float, private val resourceLocation: ResourceLocation) {

    var isHover: Boolean = false
    private var xStart = -1f
    private var yStart = -1f

    protected abstract fun onClick()

    internal fun onMouseClick(mX: Int, mY: Int) {
        if (isHover && mX > xStart && mX < xStart + 140 && mY > yStart && mY < yStart + 24)
            onClick()
    }

    internal fun onMouseInput(mX: Int, mY: Int) {
        if (xStart < 0 || yStart < 0) return
        isHover = mX > xStart && mX < xStart + 24 && mY > yStart && mY < yStart + 24
    }

    internal fun onRender(xStart: Float, yStart: Float) {
        this.xStart = xStart
        this.yStart = yStart
        if (!isHover)
            renderBoxWithColor(xStart.toDouble(), yStart.toDouble(), 24.0, 24.0, r, g, b, 0.5f) //draws the background
        else
            renderBoxWithColor(xStart.toDouble(), yStart.toDouble(), 24.0, 24.0, r, g, b, 0.8f)
        drawModalRectWithCustomSizedTexture(//draws the image shown
                round(xStart), round(yStart), 0, 0,
                24, 24, 24, 24, resourceLocation, 1f)
    }
}

