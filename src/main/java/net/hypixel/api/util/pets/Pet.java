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
package net.hypixel.api.util.pets;

import java.util.Map;

public class Pet {

    private static final int[] EXPERIENCE_LEVELS = {
            200, 210, 230, 250, 280, 310, 350, 390, 450, 500, 570, 640, 710, 800, 880, 980, 1080, 1190, 1300, 1420,
            1540, 1670, 1810, 1950, 2100, 2260, 2420, 2580, 2760, 2940, 3120, 3310, 3510, 3710, 3920, 4140, 4360, 4590,
            4820, 5060, 5310, 5560, 5820, 6090, 6360, 6630, 6920, 7210, 7500, 7800, 8110, 8420, 8740, 9070, 9400, 9740,
            10080, 10430, 10780, 11150, 11510, 11890, 12270, 12650, 13050, 13440, 13850, 14260, 14680, 15100, 15530,
            15960, 16400, 16850, 17300, 17760, 18230, 18700, 19180, 19660, 20150, 20640, 21150, 21650, 22170, 22690,
            23210, 23750, 24280, 24830, 25380, 25930, 26500, 27070, 27640, 28220, 28810, 29400, 30000
    };

    private Map<String, Object> stats;
    private int level;
    private int experience;
    private String name;

    public Pet(Map<String, Object> stats) {
        this.stats = stats;

        if (stats != null) {
            if (stats.get("experience") != null) {
                experience = ((Number) stats.get("experience")).intValue();
            }
            if (stats.get("nm") != null) {
                name = (String) stats.get("nm");
            }
        }

        updateLevel();
    }

    public String getName() {
        return name;
    }

    public double getAverageHappiness() {
        double attributeAverage = 0;
        for (PetAttribute attribute : PetAttribute.values()) {
            attributeAverage += getAttribute(attribute);
        }

        return attributeAverage / PetAttribute.values().length;
    }

    public int getAttribute(PetAttribute attribute) {
        @SuppressWarnings("unchecked")
        Map<String, Object> attributeObject = (Map<String, Object>) stats.get(attribute.name());

        if (attributeObject == null) {
            return 1;
        }

        Object timestampObj = attributeObject.get("timestamp");
        Object valueObj = attributeObject.get("value");
        if (!(timestampObj instanceof Number) || !(valueObj instanceof Number)) {
            return 1;
        }

        long currentTime = System.currentTimeMillis();
        long timestamp = ((Number) timestampObj).longValue();
        int value = ((Number) valueObj).intValue();

        long timeElapsed = currentTime - timestamp;
        long minutesPassed = timeElapsed / (1000 * 60);
        long iterations = (long) Math.floor(minutesPassed / 5);

        return Math.max(0, Math.round(value - iterations * attribute.getDecay()));
    }

    public boolean updateLevel() {
        int xp = experience;
        int level = 1;
        for (int xpLevel : EXPERIENCE_LEVELS) {
            if (xp < xpLevel) {
                break;
            } else {
                xp -= xpLevel;
                level++;
            }
        }
        this.level = level;
        return false;
    }

    public int getLevel() {
        return level;
    }

    public int getExperienceUntilLevel(int level) {
        int xp = 0;
        for (int i = 0; i < Math.min(level - 1, 100); i++) {
            xp += EXPERIENCE_LEVELS[i];
        }
        return xp;
    }

    public int getLevelProgress() {
        return experience - getExperienceUntilLevel(level);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "stats=" + stats +
                ", level=" + level +
                ", experience=" + experience +
                ", nm='" + name + '\'' +
                '}';
    }
}
