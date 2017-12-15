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

import com.google.common.collect.Lists;
import eladkay.modulargui.lib.base.EmptyModularGuiProvider;
import eladkay.modulargui.lib.base.NameModularGuiProvider;
import eladkay.modulargui.lib.base.StringAbstractModularGuiProvider;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class is meant to serve as a registry to the Modular GUI Lib.
 *
 * @author Eladkay
 * @since 1.6
 */
public class ModularGuiRegistry {

    //Should register example templates?
    public static boolean shouldRegisterExampleElements = false;

    //List of all templates in the Modular GUI
    public static ArrayList<Element> allElements = Lists.newArrayList();
    /**
     * Example templates.
     * You should keep a constant of your templates in some sort of registry class.
     */
    public static final Element TITLE = new Element("", new StringAbstractModularGuiProvider(EnumChatFormatting.AQUA + "Modular" + EnumChatFormatting.GOLD + "GUI"));
    public static final Element NAME = new Element("IGN", new NameModularGuiProvider());
    public static final Element GROUPER = new Element("", new EmptyModularGuiProvider(), true);

    /**
     * Register an element to the modular GUI.
     *
     * @param element The element to register
     */
    public static void registerElement(Element element) {
        if (!allElements.contains(element) || element.allowsDuplicates) {
            allElements.add(element);
            MinecraftForge.EVENT_BUS.register(element.provider);
        } else Logger.getLogger("modulargui").warning("Tried to register element " + element.name +
                " and it was already registered!");
    }

    static {

        if (shouldRegisterExampleElements) { //if it should register example templates...
            //register the example templates
            registerElement(TITLE);
            registerElement(GROUPER);
            registerElement(GROUPER);
            registerElement(NAME);
        }
    }

    /**
     * Instances of this class represent seperate lines in the Modular GUI HUD.
     * This class is immutable.
     *
     * @author Eladkay
     * @since 1.6
     */
    public static class Element {


        @Override
        public String toString() {
            return "Element{" +
                    "nm='" + name + '\'' +
                    ", provider=" + provider +
                    '}';
          }

        /**
         * Default constructor.
         *
         * @param name     The nm to be displayed before the content of the element in the modular GUI HUD
         * @param provider The IModularGuiProvider that provides the content for the element
         */
        public Element(String name, IModularGuiProvider provider) {
            this.name = name;
            this.provider = provider;
            this.allowsDuplicates = false;
        }

        /**
         * Should allow duplicate templates of this type?
         *
         * @param name           The nm to be displayed before the content of the element in the modular GUI HUD
         * @param provider       The IModularGuiProvider that provides the content for the element
         * @param allowDuplicate should duplicate templates of this type be allowed?
         */
        public Element(String name, IModularGuiProvider provider, boolean allowDuplicate) {
            this.name = name;
            this.provider = provider;
            this.allowsDuplicates = allowDuplicate;
        }

        //the nm of the element
        public final String name;
        //the provider of the element
        public final IModularGuiProvider provider;
        //should this templates be allowed to be registered more than once?
        public final boolean allowsDuplicates;
    }
}
