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

import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler
import com.palechip.hudpixelmod.extended.util.IEventHandler
import com.palechip.hudpixelmod.util.DisplayUtil
import net.minecraft.client.gui.GuiScreen
import java.util.*

abstract class FancyListManager(protected var shownObjects: Int, xStart: Float, yStart: Float, renderRightSide: Boolean) : IEventHandler {
    protected var isButtons = false
    protected var isMouseHander = false
    protected var xStart = 2f
    protected var yStart = 2f
    protected var renderRightSide = false

    protected var fancyListObjects = ArrayList<FancyListObject>()
    private var indexScroll = 0

    init {
        this.xStart = xStart
        this.yStart = yStart
        this.renderRightSide = renderRightSide
        HudPixelExtendedEventHandler.registerIEvent(this)
    }

    fun size(): Int {
        return fancyListObjects.size
    }

    override fun openGUI(guiScreen: GuiScreen?) {
        indexScroll = 0
    }

    override fun onConfigChanged() {
        this.renderRightSide = configRenderRight
        this.xStart = configxStart.toFloat()
        this.yStart = configyStart.toFloat()
    }

    abstract val configxStart: Int

    abstract val configyStart: Int

    abstract val configRenderRight: Boolean

    /**
     * this really renders nothing it just sets rge right offset for each element
     */
    protected fun renderDisplay() {

        if (fancyListObjects.isEmpty()) return

        var xStart = this.xStart
        var yStart = this.yStart

        if (renderRightSide) xStart = DisplayUtil.getScaledMcWidth().toFloat() - xStart - 140f

        if (fancyListObjects.size <= shownObjects) {
            yStart += 13f
            for (fco in fancyListObjects) {
                fco.onRenderTick(false, xStart, yStart, renderRightSide)
                yStart += 25f
            }
            return
        }

        if (indexScroll > 0) fancyListObjects[indexScroll - 1].onRenderTick(true, xStart, yStart, renderRightSide)
        yStart += 13f

        for (i in indexScroll..indexScroll + shownObjects - 1) {
            fancyListObjects[i].onRenderTick(false, xStart, yStart, renderRightSide)
            yStart += 25f
        }

        if (indexScroll + shownObjects <= size() - 1)
            fancyListObjects[indexScroll + shownObjects].onRenderTick(true, xStart, yStart, renderRightSide)
    }


    override fun onMouseClick(mX: Int, mY: Int) {
        if (!isMouseHander) return
        if (fancyListObjects.isEmpty()) return
        if (fancyListObjects.size <= shownObjects)
            for (fco in fancyListObjects)
                fco.onMouseClick(mX, mY)
        else
            for (i in indexScroll..indexScroll + shownObjects - 1) {
                if (isButtons)
                    fancyListObjects[i].onMouseClick(mX, mY)
            }
    }

    /**
     * handels the scrollinput and processes the right index for the list

     * @param i scroll input
     */

    override fun handleMouseInput(i: Int, mX: Int, mY: Int) {
        if (!isMouseHander) return
        if (fancyListObjects.isEmpty()) return
        if (fancyListObjects.size <= shownObjects)
            for (fco in fancyListObjects)
                fco.onMouseInput(mX, mY)
        else
            for (fco in indexScroll..indexScroll + shownObjects - 1) {
                if (isButtons)
                    fancyListObjects[fco].onMouseInput(mX, mY)
            }

        if (mY > 26 * shownObjects + 28) return
        if (renderRightSide) {
            val xStart = DisplayUtil.getScaledMcWidth().toFloat() - this.xStart - 140f
            if (mX < xStart || mX > xStart + 140) return
        } else if (mX > 140 + xStart) return

        if (i != 0) {
            if (i < 0) {
                if (indexScroll >= size() - shownObjects) {
                    indexScroll = size() - shownObjects
                } else {
                    indexScroll++
                }
            } else if (i > 0) {
                if (indexScroll - 1 <= 0) {
                    indexScroll = 0
                } else {
                    indexScroll--
                }
            }
        }
    }




    companion object {
        /**
         * process the current loadingbar value
         */
        private var tickCounter = 0
        fun processLoadingBar() {
            if (tickCounter >= 2) {
                if (FancyListObject.loadingBar >= 15)
                    FancyListObject.loadingBar = 0
                else
                    FancyListObject.loadingBar++
                tickCounter = 0
            } else
                tickCounter++
        }
    }



}
