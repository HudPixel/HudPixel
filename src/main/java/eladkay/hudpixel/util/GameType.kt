/*
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
 */

package eladkay.hudpixel.util

/**
 * Replaces GameType and only holds the modID. It can be used to statically reference a certain game without storing any information which may be changed.
 */
enum class GameType private constructor(val modID: Int, val databaseID: Int, val nm: String, val tipName: String, val scoreboardName: String, val statsName: String, val inputNames: Array<String>) {
    UNKNOWN(-1, -1, "UNKNOWN", "", "none", "", arrayOf("")),
    ALL_GAMES(0, -1, "ALL GAMES", "", "any", "", arrayOf("all", "all games", "allgames")),
    QUAKECRAFT(1, 2, "Quakecraft", "quakecraft", "QUAKECRAFT", "Quake", arrayOf("quake", "quakecraft")),
    THE_WALLS(2, 3, "Walls", "walls", "THE WALLS", "Walls", arrayOf("walls", "the walls", "thewalls")),
    PAINTBALL(3, 4, "Paintball", "paintball", "PAINTBALL", "Paintball", arrayOf("paintball", "paint")),
    BLITZ(4, 5, "Blitz Survival Games", "blitz", "BLITZ SG", "HungerGames", arrayOf("blitz", "hungergames", "hunger games", "survival games", "survivalgames", "survival", "hunger")),

    TNT_GAMES(5, 6, "TNT Games", "tnt", "THE TNT GAMES", "TNTGames", arrayOf("tnt", "tntgames", "tnt games")),
    BOW_SPLEEF(6, -1, "Bow Spleef", "", TNT_GAMES.scoreboardName, "", arrayOf("bow", "bowspleef", "bow spleef", "spleef", "bspleef")),
    TNT_RUN(7, -1, "TNT Run", "", TNT_GAMES.scoreboardName, "TNTGames", arrayOf("run", "tntrun", "tnt run")),
    TNT_WIZARDS(8, -1, "TNT Wizards", "", TNT_GAMES.scoreboardName, "TNTGames", arrayOf("wizards", "tntwizards", "tnt wizards")),
    TNT_TAG(9, -1, "TNT Tag", "", TNT_GAMES.scoreboardName, "TNTGames", arrayOf("tag", "tnttag", "tnt tag")),
    ANY_TNT(5, -1, TNT_GAMES.nm, TNT_GAMES.tipName, TNT_GAMES.scoreboardName, "TNTGames", arrayOf("anytnt", "alltnt", "any tnt", "all tnt")),

    VAMPIREZ(10, 7, "VampireZ", "vampirez", "VAMPIREZ", "VampireZ", arrayOf("vamp", "vampire", "vampz", "vampirez")),
    MEGA_WALLS(11, 13, "Mega Walls", "mega", "MEGA WALLS", "Walls3", arrayOf("mega", "megawalls", "mega walls", "walls3")),
    ARENA(12, 17, "Arena Brawl", "arena", "ARENA BRAWL", "Arena", arrayOf("areana", "brawl", "arena brawl", "arenabrawl")),
    UHC(13, 20, "UHC Champions", "uhc", "UHC CHAMPIONS", "UHC", arrayOf("uhc", "hardcore", "hard core", "ultra hardcore", "champions", "uhc champions", "champ")),
    COPS_AND_CRIMS(14, -1, "Cops and Crims", "cops", "COPS AND CRIMS", "MCGO", arrayOf("cops", "csgo", "cs go", "cops and crims", "crims", "copsandcrims")),
    WARLORDS(15, 23, "Warlords", "warlords", "WARLORDS", "Battleground", arrayOf("war", "warlords", "warlord")),

    ARCADE_GAMES(16, 14, "Arcade Games", "Arcade", "ARCADE GAMES", "Arcade", arrayOf("arcade", "arcadegames", "arcade games", "arcades")),
    BLOCKING_DEAD(17, -1, "Blocking Dead", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("blocking dead", "blockingdead", "minez", "blockdead", "dead")),
    BOUNTY_HUNTERS(18, -1, "Bounty Hunters", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("bounty hunters", "bountyhunters", "bounty", "hunters", "one in the quiver", "oneinthequiver", "quiver")),
    BUILD_BATTLE(19, -1, "Build Battle", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("build battle", "buildbattle", "build")),
    CREEPER_ATTACK(20, -1, "Creeper Attack", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("creeper attack", "creeperattack", "creeper")),
    DRAGON_WARS(21, -1, "Dragon Wars", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("dragon wars", "dragonwars", "dragon")),
    ENDER_SPLEEF(22, -1, "Ender Spleef", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("ender spleef", "enderspleef", "ender", "espleef")),
    FARM_HUNT(23, -1, "Farm Hunters", "", "Farm Hunt", "Arcade", arrayOf("farm hunt", "farmhunt", "farm", "hunt")),
    GALAXY_WARS(24, -1, "Galaxy Wars", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("galaxy wars", "galaxywars", "galaxy")),
    PARTY_GAMES_1(25, -1, "Party Games", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("party games 1", "partygames1")),
    PARTY_GAMES_2(26, -1, "Party Games", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("party games 2", "partygames2")),
    TRHOW_OUT(27, -1, "Throw Out", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("throw out", "throwout", "throw")),
    FOOTBALL(33, -1, "Football", "", ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("football", "soccer", "foot")),

    TURBO_KART_RACERS(28, 25, "Turbo Kart Racers", "turbo", "TURBO KART RACERS", "GingerBread", arrayOf("turbo kart racers", "turbo kart racers", "tkr", "turbo")),
    SPEED_UHC(29, 54, "Speed UHC", "speed", "SPEED UHC", "SpeedUHC", arrayOf("speed uhc", "speeduhc", "speed")),
    CRAZY_WALLS(31, 52, "Crazy Walls", "crazy", "CRAZY WALLS", "TrueCombat", arrayOf("crazy walls", "crazywalls", "crazy")),
    SMASH_HEROES(32, 24, "Smash Heroes", "smash", " SMASH HEROES", "SuperSmash", arrayOf("smash heroes", "smashheroes", "smash")),

    SMASH_HEROES_WOSPACE(32, -1, "Smash Heroes", "smash", "SMASH HEROES", "SuperSmash", SMASH_HEROES.inputNames),
    SKYWARS(30, 0, "SkyWars", "skywars", "SKYWARS", "SkyWars", arrayOf("sky wars", "skywars", "sky")),
    HOUSING(34, 26, "Housing", "Housing", "HOUSING", "Housing", arrayOf("housing", "house", "houses")),
    SKYCLASH(35, 55, "SkyClash", "SkyClash", "SKYCLASH", "SkyClash", arrayOf("sky clash", "skyclash", "clash")),/*
    PROTOTYPE(36, NO_IDEA, "Prototype", "", "Prototype", "Prototype", arrayOf("prototype")),
    CLASSIC(37, NO_IDEA, "Classic", "classic", "Classic", "Classic", arrayOf("classic games", "classicgames", "classic", "deadgames")),*/

    ANY_ARCADE(16, -1, ARCADE_GAMES.nm, ARCADE_GAMES.tipName, ARCADE_GAMES.scoreboardName, "Arcade", arrayOf("anyarcade", "any arcade"));


    companion object {
        val NO_IDEA get() = -21452412
        fun getTypeByDatabaseID(ID: Int): GameType {
            for (type in GameType.values())
                if (type.databaseID == ID)
                    return type
            return GameType.UNKNOWN
        }

        fun getTypeByName(name: String): GameType {
            for (type in GameType.values())
                if (type.nm.equals(name, ignoreCase = true) || type.statsName.equals(name, ignoreCase = true))
                    return type
            return GameType.UNKNOWN
        }

        fun getTypeByInput(input: String): GameType {
            for (type in GameType.values())
                if (type.inputNames.contains(input.toLowerCase()))
                    return type
            return GameType.UNKNOWN
        }

        fun getTypeByID(modid: Int): GameType {
            for (type in GameType.values())
                if (type.modID == modid)
                    return type
            return GameType.UNKNOWN
        }
    }
}
