/*******************************************************************************
 * HudPixelReloaded
 *
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses under forge-docs/. These parts can be downloaded at files.minecraftforge.net.
 *
 * This project contains a unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 *
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously intended for usage in this kind of application. By default, all rights are reserved.
 *
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license. The majority of code left from palechip's creations is the component implementation.
 *
 * The ported version to Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license (to be changed to the new license as detailed below in the next minor update).
 *
 * For the rest of the code and for the build the following license applies:
 *
 * alt-tag
 *
 * HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions. Based on a work at HudPixelExtended & HudPixel.
 *
 * Restrictions:
 *
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow the following license terms and the license terms given by the listed above Creative Commons License, however in extreme cases the authors reserve the right to revoke all rights for usage of the codebase.
 *
 * PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their code, but only when it is used separately from HudPixel and any license header must indicate that.
 * You shall not claim ownership over this project and repost it in any case, without written permission from at least two of the authors.
 * You shall not make money with the provided material. This project is 100% non commercial and will always stay that way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed code is merged to the release branch you cannot revoke the given freedoms by this license.
 * If your own project contains a part of the licensed material you have to give the authors full access to all project related files.
 * You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors reserve the right to take down any infringing project.
 ******************************************************************************/
package eladkay.modulargui.lib;


import eladkay.hudpixel.HudPixelMod;
import eladkay.hudpixel.config.GeneralConfigSettings;
import eladkay.hudpixel.util.BansHandler;
import eladkay.hudpixel.util.DisplayUtil;
import eladkay.modulargui.lib.base.SimpleModularGuiProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

import java.util.ArrayList;

/**
 * This class is responsible for rendering the templates of the modular GUI.
 *
 * @author Eladkay
 * @since 1.5
 */
public class Renderer {

    //Replace with some sort of config option
    private boolean isEnabled = true;

    /**
     * Event: When the game renders its overlay.
     */
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        BansHandler.checkForBan();
        if(GeneralConfigSettings.getHudDisabled()) return;
        if (! HudPixelMod.Companion.isHypixelNetwork() && !HudPixelMod.Companion.getIS_DEBUGGING()) return;
        if (!(Minecraft.getMinecraft().inGameHasFocus)) return;
        ArrayList<ModularGuiRegistry.Element> display = ModularGuiRegistry.allElements; //the templates
        int w = GeneralConfigSettings.getDisplayXOffset(); //width, change this if needed
        int h = GeneralConfigSettings.getDisplayYOffset(); //height, you shouldn't touch this usually
        if (isEnabled) { //if enabled...
            @SuppressWarnings("LocalVariableDeclarationSideOnly") FontRenderer fontRendererObj = FMLClientHandler.instance().getClient().fontRendererObj; //get the font renderer
            for (ModularGuiRegistry.Element element : display) { //for each element...
                if (!element.provider.showElement()) continue; //if you shouldn't show it, skip it.
                String aDisplay;
                if (!element.name.isEmpty() && !(element.provider instanceof SimpleModularGuiProvider))
                    aDisplay = element.name + ": " + element.provider.content(); //get the display text for the element
                else
                    aDisplay = element.provider.content();
                if (element.provider.content() == null) return;
                if (element.provider instanceof SimpleModularGuiProvider || !(element.provider.content().isEmpty() && element.name.isEmpty()) || element.provider.ignoreEmptyCheck()) { //if it's not empty or it's allowed to override this isHypixelNetwork...
                    int xOffset = 0;
                    if(GeneralConfigSettings.getHudRenderRight())
                        xOffset = DisplayUtil.getScaledMcWidth() - fontRendererObj.getStringWidth(aDisplay) - 4;
                    if (GeneralConfigSettings.getHudBackground())
                        RenderUtils.renderBoxWithColor(w - 2 + xOffset, h - 1, fontRendererObj.getStringWidth(aDisplay) + 3, 10, (float)(GeneralConfigSettings.getHudRed()) / 255, (float)(GeneralConfigSettings.getHudGreen()) / 255, (float)(GeneralConfigSettings.getHudBlue()) / 255, (float)(GeneralConfigSettings.getHudAlpha()) / 255);
                    fontRendererObj.drawString(aDisplay, w + xOffset, h, 0xffffff); //draw it
                    h += 10; //increment height
                }
            }
        }
    }
}
