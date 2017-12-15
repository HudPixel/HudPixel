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
package eladkay.hudpixel.util;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Currently not possible, sorry folks
 */
public class EnchantmentStringHandler {
    public static class EnchInfo {
        public final Enchantment ench;
        public final short level;
        public final ItemStack stack;

        public EnchInfo(Enchantment ench, short level, ItemStack stack) {
            this.ench = ench;
            this.level = level;
            this.stack = stack;
        }
    }

    public static class RenderTooltipEvent extends Event {

        public final float y;
        public final float x;
        public final ItemStack stack;

        public RenderTooltipEvent(float y, float x, ItemStack stack) {
            this.y = y;
            this.x = x;
            this.stack = stack;
        }
    }

    public static List<EnchInfo> getEnchants(ItemStack stack) {
        List<EnchInfo> list = Lists.newArrayList();
        for (int i = 0; i < stack.getEnchantmentTagList().tagCount(); i++) {
            NBTTagCompound tag = stack.getEnchantmentTagList().getCompoundTagAt(i);
            short id = tag.getShort("id");
            short lvl = tag.getShort("lvl");
            Enchantment ench = Enchantment.getEnchantmentById(id);
            list.add(new EnchInfo(ench, lvl, stack));
        }
        return list;
    }

    public static String getEnchantString(EnchInfo info) {
        return info.ench.getTranslatedName(info.level);
    }

    public static List<String> map(List<EnchInfo> list) {
        return list.stream().map(EnchantmentStringHandler::getEnchantString).collect(Collectors.toList());
    }

    public static List<String> map(ItemStack stack) {
        return getEnchants(stack).stream().map(EnchantmentStringHandler::getEnchantString).collect(Collectors.toList());
    }

    static {
        MinecraftForge.EVENT_BUS.register(new EnchantmentStringHandler());
    }

    private static final String TAG_DYE = "Quark:ItemNameDye";

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void makeTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_DYE)) {
            FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
            int len = font.getStringWidth(stack.getDisplayName());
            String spaces = "";
            while (font.getStringWidth(spaces) < len)
                spaces += " ";

            event.toolTip.set(0, spaces);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderTooltip(RenderTooltipEvent event) {
        ItemStack stack = event.stack;
        if (stack != null && stack.hasTagCompound()) {
            int dye = stack.getTagCompound().hasKey(TAG_DYE) ? stack.getTagCompound().getInteger(TAG_DYE) : -1;
            if (dye != -1) {
                int rgb = ItemDye.dyeColors[Math.min(15, dye)];
                Color color = new Color(rgb);
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(stack.getDisplayName(), event.x, event.y, color.getRGB());
            }
        }
    }


}
