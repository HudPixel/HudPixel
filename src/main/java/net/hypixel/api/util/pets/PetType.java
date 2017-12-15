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

import net.hypixel.api.util.Rarity;

public enum PetType {

    CAT_BLACK("Cat: Black", Rarity.COMMON),
    CAT_RED("Cat: Red", Rarity.COMMON),
    CAT_SIAMESE("Cat: Siamese", Rarity.COMMON),
    SILVERFISH("Silverfish", Rarity.COMMON),
    ZOMBIE("Zombie", Rarity.COMMON),
    PIG("Pig", Rarity.COMMON),
    COW("Cow", Rarity.COMMON),
    WOLF("Wolf", Rarity.COMMON),
    CHICKEN("Chicken", Rarity.COMMON),
    HORSE_BROWN("Horse: Brown", Rarity.COMMON),
    SHEEP_WHITE("Sheep: White", Rarity.COMMON),
    SHEEP_GRAY("Sheep: Gray", Rarity.COMMON),
    SHEEP_BROWN("Sheep: Brown", Rarity.COMMON),
    SHEEP_SILVER("Sheep: Silver", Rarity.COMMON),

    HORSE_CREAMY("Horse: Creamy", Rarity.RARE),
    HORSE_CHESTNUT("Horse: Chestnut", Rarity.RARE),
    HORSE_DARK_BROWN("Horse: Dark Brown", Rarity.RARE),
    HORSE_GREY("Horse: Gray", Rarity.RARE),
    DONKEY("Donkey", Rarity.RARE),
    MULE("Mule", Rarity.RARE),
    VILLAGER_FARMER("Villager: Farmer", Rarity.RARE),
    VILLAGER_LIBRARIAN("Villager: Librarian", Rarity.RARE),
    VILLAGER_PRIEST("Villager: Priest", Rarity.RARE),
    VILLAGER_BLACKSMITH("Villager: Blacksmith", Rarity.RARE),
    VILLAGER_BUTCHER("Villager: Butcher", Rarity.RARE),
    SHEEP_ORANGE("Sheep: Orange", Rarity.RARE),
    SHEEP_MAGENTA("Sheep: Magenta", Rarity.RARE),
    SHEEP_LIGHT_BLUE("Sheep: Light Blue", Rarity.RARE),
    SHEEP_YELLOW("Sheep: Yellow", Rarity.RARE),
    SHEEP_LIME("Sheep: Lime", Rarity.RARE),
    SHEEP_CYAN("Sheep: Cyan", Rarity.RARE),
    SHEEP_PURPLE("Sheep: Purple", Rarity.RARE),
    SHEEP_BLUE("Sheep: Blue", Rarity.RARE),
    SHEEP_GREEN("Sheep: Green", Rarity.RARE),
    SHEEP_RED("Sheep: Red", Rarity.RARE),
    CAVE_SPIDER("Cave Spider", Rarity.RARE),
    SLIME_TINY("Slime (Tiny)", Rarity.RARE),
    MAGMA_CUBE_TINY("Magma Cube (Tiny)", Rarity.EPIC),
    ZOMBIE_BABY("Zombie (Baby)", Rarity.RARE),
    PIG_BABY("Pig (Baby)", Rarity.RARE),
    COW_BABY("Cow (Baby)", Rarity.RARE),
    WOLF_BABY("Wolf (Baby)", Rarity.RARE),
    CHICKEN_BABY("Chicken (Baby)", Rarity.RARE),
    CAT_BLACK_BABY("Cat: Black (Baby)", Rarity.RARE),
    CAT_RED_BABY("Cat: Red (Baby)", Rarity.RARE),
    CAT_SIAMESE_BABY("Cat: Siamese (Baby)", Rarity.RARE),
    BROWN_HORSE_BABY("Horse: Brown (Baby)", Rarity.RARE),
    SHEEP_WHITE_BABY("Sheep: White (Baby)", Rarity.RARE),
    SHEEP_GRAY_BABY("Sheep: Gray (Baby)", Rarity.RARE),
    SHEEP_BROWN_BABY("Sheep: Brown (Baby)", Rarity.RARE),
    SHEEP_SILVER_BABY("Sheep: Silver (Baby)", Rarity.RARE),

    SPIDER("Spider", Rarity.EPIC),
    SKELETON("Skeleton", Rarity.EPIC),
    HORSE_WHITE("Horse: White", Rarity.EPIC),
    HORSE_BLACK("Horse: Black", Rarity.EPIC),
    HORSE_UNDEAD("Undead Horse", Rarity.EPIC),
    ZOMBIE_VILLAGER("Villager: Zombie", Rarity.EPIC),
    PIG_ZOMBIE("Pig Zombie", Rarity.EPIC),
    SHEEP_BLACK("Sheep: Black", Rarity.EPIC),
    SHEEP_PINK("Sheep: Pink", Rarity.EPIC),
    BAT("Bat", Rarity.EPIC),
    SLIME_SMALL("Slime (Small)", Rarity.EPIC),
    MAGMA_CUBE_SMALL("Magma Cube (Small)", Rarity.EPIC),
    MOOSHROOM("Mooshroom", Rarity.EPIC),
    CREEPER("Creeper", Rarity.EPIC),
    HORSE_CREAMY_BABY("Horse: Creamy (Baby)", Rarity.EPIC),
    HORSE_CHESTNUT_BABY("Horse: Chestnut (Baby)", Rarity.EPIC),
    HORSE_DARK_BROWN_BABY("Horse: Dark Brown (Baby)", Rarity.EPIC),
    HORSE_GRAY_BABY("Horse: Gray (Baby)", Rarity.EPIC),
    VILLAGER_FARMER_BABY("Villager: Farmer (Baby)", Rarity.EPIC),
    VILLAGER_LIBRARIAN_BABY("Villager: Librarian (Baby)", Rarity.EPIC),
    VILLAGER_PRIEST_BABY("Villager: Priest (Baby)", Rarity.EPIC),
    VILLAGER_BLACKSMITH_BABY("Villager: Blacksmith (Baby)", Rarity.EPIC),
    VILLAGER_BUTCHER_BABY("Villager: Butcher (Baby)", Rarity.EPIC),
    SHEEP_ORANGE_BABY("Sheep: Orange (Baby)", Rarity.EPIC),
    SHEEP_MAGENTA_BABY("Sheep: Magenta (Baby)", Rarity.EPIC),
    SHEEP_LIGHT_BLUE_BABY("Sheep: Light Blue (Baby)", Rarity.EPIC),
    SHEEP_YELLOW_BABY("Sheep: Yellow (Baby)", Rarity.EPIC),
    SHEEP_LIME_BABY("Sheep: Lime (Baby)", Rarity.EPIC),
    SHEEP_CYAN_BABY("Sheep: Cyan (Baby)", Rarity.EPIC),
    SHEEP_PURPLE_BABY("Sheep: Purple (Baby)", Rarity.EPIC),
    SHEEP_BLUE_BABY("Sheep: Blue (Baby)", Rarity.EPIC),
    SHEEP_GREEN_BABY("Sheep: Green (Baby)", Rarity.EPIC),
    SHEEP_RED_BABY("Sheep: Red (Baby)", Rarity.EPIC),
    SHEEP_RAINBOW("Sheep: Rainbow", Rarity.LEGENDARY),

    IRON_GOLEM("Golem", Rarity.LEGENDARY),
    ENDERMAN("Enderman", Rarity.LEGENDARY),
    MOOSHROOM_BABY("Mooshroom (Baby)", Rarity.LEGENDARY),
    PIG_ZOMBIE_BABY("Pig Zombie (Baby)", Rarity.LEGENDARY),
    SHEEP_PINK_BABY("Sheep: Pink (Baby)", Rarity.LEGENDARY),
    SHEEP_BLACK_BABY("Sheep: Black (Baby)", Rarity.LEGENDARY),
    BLAZE("Blaze", Rarity.LEGENDARY),
    WITCH("Witch", Rarity.EPIC),
    HORSE_SKELETON("Skeleton Horse", Rarity.LEGENDARY),
    SNOWMAN("Snowman", Rarity.EPIC),
    SLIME_BIG("Slime (Big)", Rarity.LEGENDARY),
    MAGMA_CUBE_BIG("Magma Cube (Big)", Rarity.LEGENDARY),
    WITHER_SKELETON("Wither Skeleton", Rarity.LEGENDARY),

    GREEN_HELPER("Green Little Helper", Rarity.EPIC),
    RED_HELPER("Red Little Helper", Rarity.EPIC),

    WILD_OCELOT("Wild Ocelot"),
    WILD_OCELOT_BABY("Wild Ocelot (Baby)"),

    SQUID("Squid"),

    BROWN_RABBIT("Rabbit: Brown", Rarity.RARE),
    WHITE_RABBIT("Rabbit: White", Rarity.RARE),
    BLACK_RABBIT("Rabbit: Black", Rarity.RARE),
    BLACK_WHITE_RABBIT("Rabbit: Black & White", Rarity.EPIC),
    GOLD_RABBIT("Rabbit: Gold", Rarity.EPIC),
    SALT_PEPPER_RABBIT("Rabbit: Salt & Pepper", Rarity.EPIC),

    HEROBRINE("Herobrine", Rarity.LEGENDARY),
    CREEPER_POWERED("Powered Creeper", Rarity.LEGENDARY),
    ENDERMITE("Endermite", Rarity.LEGENDARY);

    public static final PetType[] VALUES = values();

    private final String name;
    private final Rarity rarity;

    PetType(String name) {
        this(name, null);
    }

    PetType(String name, Rarity rarity) {
        this.name = name;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public Rarity getRarity() {
        return rarity;
    }
}
