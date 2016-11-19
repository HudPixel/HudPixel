package com.palechip.hudpixelmod.extended.statsviewer

import com.palechip.hudpixelmod.extended.statsviewer.msc.IGameStatsViewer
import com.palechip.hudpixelmod.extended.statsviewer.msc.StatsCache.getPlayerByName
import com.palechip.hudpixelmod.util.GameType
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.Tessellator.getInstance
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderPlayerEvent.Pre
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11.glNormal3f
import java.lang.System.currentTimeMillis
import java.util.*

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
@SideOnly(Side.CLIENT)
class StatsViewerRender internal constructor(gameType: GameType, playerUUID: UUID) {

    private val iGameStatsViewer: IGameStatsViewer?
    internal val expireTimestamp: Long

    init {
        this.iGameStatsViewer = getPlayerByName(playerUUID, gameType)
        this.expireTimestamp = currentTimeMillis() + DURATION
    }

    /**
     * Renders the stats above the player

     * @param event RenderPlayerEvent
     */
    internal fun onRenderPlayer(event: Pre) {

        val offset = 0.3
        var i = 1

        if (this.iGameStatsViewer?.getRenderList() != null) {
            for (s in this.iGameStatsViewer?.getRenderList() ?: listOf<String>()) {
                renderName(event.renderer, s, event.entityPlayer, event.x, event.y + offset * i, event.z)
                i++
            }
        } else {
            renderName(event.renderer, "Loading stats ....", event.entityPlayer, event.x, event.y + offset, event.z)
        }
    }

    companion object {

        private val DURATION = 10000

        /**
         * renders a string above a Player copied from the original mc namerenderer

         * @param renderer the renderer
         * *
         * @param str      the string to render
         * *
         * @param entityIn the entity to render above
         * *
         * @param x        x-cord
         * *
         * @param y        y-cord
         * *
         * @param z        z-cord
         */
        private fun renderName(renderer: RenderPlayer, str: String, entityIn: EntityPlayer, x: Double, y: Double, z: Double) {
            val fontrenderer = renderer.fontRendererFromRenderManager
            val f = 1.6f
            val f1 = 0.016666668f * f
            pushMatrix()
            translate(x.toFloat() + 0.0f, y.toFloat() + entityIn.height + 0.5f, z.toFloat())
            glNormal3f(0.0f, 1.0f, 0.0f)
            rotate(-renderer.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            rotate(renderer.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
            scale(-f1, -f1, f1)
            disableLighting()
            depthMask(false)
            disableDepth()
            enableBlend()
            tryBlendFuncSeparate(770, 771, 1, 0)
            val tessellator = getInstance()
            val worldrenderer = tessellator.buffer
            val i = 0

            val j = fontrenderer.getStringWidth(str) / 2
            disableTexture2D()
            worldrenderer.begin(7, POSITION_COLOR)
            worldrenderer.pos((-j - 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldrenderer.pos((-j - 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldrenderer.pos((j + 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldrenderer.pos((j + 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            tessellator.draw()
            enableTexture2D()
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127)
            enableDepth()
            depthMask(true)
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1)
            enableLighting()
            disableBlend()
            color(1.0f, 1.0f, 1.0f, 1.0f)
            popMatrix()
        }
    }
}
