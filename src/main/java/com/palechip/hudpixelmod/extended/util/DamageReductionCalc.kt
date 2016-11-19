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
package com.palechip.hudpixelmod.extended.util

import net.minecraft.client.Minecraft
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.Item
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

@SideOnly(Side.CLIENT)
object DamageReductionCalc {

    var armorReduct: MutableList<String> = ArrayList()

    val reduction: List<String>
        get() {
            armorReduct.clear()
            PlayerArmorInfo.getValues()
            val armor = PlayerArmorInfo.helmet + PlayerArmorInfo.chest + PlayerArmorInfo.pants + PlayerArmorInfo.boots
            val epf = calcArmorEpf()
            val avgdef = addArmorProtResistance(armor, calcProtection(epf), PlayerArmorInfo.resistance)
            val mindef = addArmorProtResistance(armor, Math.ceil(epf / 2.0), PlayerArmorInfo.resistance)
            val maxdef = addArmorProtResistance(armor, Math.ceil(if (epf < 20.0) epf else 20.0), PlayerArmorInfo.resistance)
            val min = roundDouble(mindef * 100.0)
            val max = roundDouble(maxdef * 100.0)
            val avg = roundDouble(avgdef * 100.0)
            armorReduct.add(0, "$min")
            armorReduct.add(1, "$max")
            armorReduct.add(2, "$avg")
            return armorReduct
        }

    private fun addArmorProtResistance(armor: Double, prot: Double, resi: Int): Double {
        var protTotal = armor + (1.0 - armor) * prot * 0.04
        protTotal += (1.0 - protTotal) * resi.toDouble() * 0.2
        return if (protTotal < 1.0) protTotal else 1.0
    }

    private fun calcProtection(armorEpf: Double): Double {
        var protection = 0.0
        for (i in 50..100) {
            protection += if (Math.ceil(armorEpf * i / 100.0) < 20.0) Math.ceil(armorEpf * i / 100.0) else 20.0
        }
        return protection / 51.0
    }

    private fun calcArmorEpf(): Double {
        val prot = calcEpf(PlayerArmorInfo.helmetProt) + calcEpf(PlayerArmorInfo.chestProt) + calcEpf(PlayerArmorInfo.pantsProt) + calcEpf(PlayerArmorInfo.bootsProt)
        return if (prot < 25.0) prot else 25.0
    }

    private fun calcEpf(prot: Int): Double {
        return if (prot != 0) Math.floor((6.0 + Math.pow(prot.toDouble(), 2.0)) * 0.25) else 0.0
    }

    private fun roundDouble(number: Double): Double {
        val x = Math.round(number * 10000.0).toDouble()
        return x / 10000.0
    }

    object PlayerArmorInfo {

        var helmet = 0.0
        var chest = 0.0
        var pants = 0.0
        var boots = 0.0
        var helmetProt = 0
        var chestProt = 0
        var pantsProt = 0
        var bootsProt = 0
        var resistance = 0
        private val mc = Minecraft.getMinecraft()

        fun getValues() {
            helmet = 0.0
            chest = 0.0
            pants = 0.0
            boots = 0.0
            helmetProt = 0
            chestProt = 0
            pantsProt = 0
            bootsProt = 0
            resistance = 0

            if (mc.thePlayer.inventory.armorInventory[3] != null) {
                getHelmet()
                getHelmetProtection()
            } else {
                helmet = 0.0
                helmetProt = 0
            }
            if (mc.thePlayer.inventory.armorInventory[2] != null) {
                getChestplate()
                getChestplateProtection()
            } else {
                chest = 0.0
                chestProt = 0
            }
            if (mc.thePlayer.inventory.armorInventory[1] != null) {
                getPants()
                getPantsProtection()
            } else {
                pants = 0.0
                pantsProt = 0
            }
            if (mc.thePlayer.inventory.armorInventory[0] != null) {
                getBoots()
                getBootsProtection()
            } else {
                boots = 0.0
                bootsProt = 0
            }
            if (mc.thePlayer.activePotionEffects != null) {
                getResistance()
            } else {
                resistance = 0
            }
        }

        private fun getHelmet() {
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).item) == 298) {
                helmet = 0.04
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).item) == 314) {
                helmet = 0.08
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).item) == 302) {
                helmet = 0.08
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).item) == 306) {
                helmet = 0.08
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(3).item) == 310) {
                helmet = 0.12
            }
        }

        private fun getChestplate() {
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).item) == 299) {
                chest = 0.12
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).item) == 315) {
                chest = 0.20
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).item) == 303) {
                chest = 0.20
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).item) == 307) {
                chest = 0.24
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(2).item) == 311) {
                chest = 0.32
            }
        }

        private fun getPants() {
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).item) == 300) {
                pants = 0.08
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).item) == 316) {
                pants = 0.12
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).item) == 304) {
                pants = 0.16
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).item) == 308) {
                pants = 0.20
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(1).item) == 312) {
                pants = 0.24
            }
        }

        private fun getBoots() {
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).item) == 301) {
                boots = 0.04
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).item) == 317) {
                boots = 0.04
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).item) == 305) {
                boots = 0.04
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).item) == 309) {
                boots = 0.08
            }
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorItemInSlot(0).item) == 313) {
                boots = 0.12
            }
        }

        private fun getResistance() { //TODO
            /*
            if (mc.thePlayer.isPotionActive(Potion.getPotionById())) {
                Collection<PotionEffect> potionEffects = mc.thePlayer.getActivePotionEffects();
                for (PotionEffect potionEffect : potionEffects) {
                    Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                    if (potion.getName().equalsIgnoreCase(Potion.resistance.getName())) {
                        resistance = potionEffect.getAmplifier() + 1;
                    }
                }
            } else {
                resistance = 0;
            }*/
        }

        private fun getHelmetProtection() {
            if (helmet != 0.0) {
                helmetProt = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), mc.thePlayer.inventory.armorItemInSlot(3))
            } else {
                helmetProt = 0
            }
        }

        private fun getChestplateProtection() {
            if (chest != 0.0) {
                chestProt = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), mc.thePlayer.inventory.armorItemInSlot(2))
            } else {
                chestProt = 0
            }
        }

        private fun getPantsProtection() {
            if (pants != 0.0) {
                pantsProt = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), mc.thePlayer.inventory.armorItemInSlot(1))
            } else {
                pantsProt = 0
            }
        }

        private fun getBootsProtection() {
            if (boots != 0.0) {
                bootsProt = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), mc.thePlayer.inventory.armorItemInSlot(0))
            } else {
                bootsProt = 0
            }
        }

    }
}
