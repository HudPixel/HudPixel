package eladkay.hudpixel.command.statsgamemodes

import com.google.gson.JsonParser
import eladkay.hudpixel.command.StatsCommand
import eladkay.hudpixel.util.GameType
import eladkay.hudpixel.util.plus
import jdk.nashorn.internal.parser.JSONParser
import net.hypixel.api.reply.PlayerReply
import net.unaussprechlich.hudpixelextended.util.LoggerHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper.GOLD

object MegaWallsStats {
    fun getMegaWallsStats(pr: PlayerReply): String {
        val megawallsClasses: Array<String> = arrayOf("skeleton", "pigman", "dreadlord", "herobrine", "spider", "zombie", "hunter")

        val outText = StringBuilder()
        val megawallsStats = pr.player.get("stats").asJsonObject.get(GameType.MEGA_WALLS.statsName).asJsonObject

        var playerHasClasses = emptyArray<String>()
        var mwPackages = megawallsStats.get("packages").asJsonArray
        var jsonParser = JsonParser()
//        megawallsClasses
//                .filter { mwPackages.contains(jsonParser.parse("\"" + it + "_class\"").asJsonObject) }
//                .forEach { playerHasClasses += it }
        megawallsClasses
                .filter { megawallsStats.has("kills_" + it.capitalize()) }
                .forEach { playerHasClasses += it }

        // Title
        outText.append(pr.formattedDisplayName + McColorHelper.GOLD + "'s Mega Walls Stats:")
        outText.append("\n")

        // Coins
        outText.append(McColorHelper.GOLD + "Coins: " + McColorHelper.BOLD + megawallsStats.get("coins"))
        outText.append("\n")
        outText.append("\n")
        // TODO add final kills and stuff.
        outText.append(McColorHelper.GOLD + "Kills: " + McColorHelper.BOLD + megawallsStats.get("kills") + McColorHelper.RESET + McColorHelper.GOLD +
                " | Deaths: " + McColorHelper.BOLD + megawallsStats.get("deaths") + McColorHelper.RESET + McColorHelper.GOLD +
                " | K/D " + McColorHelper.BOLD + StatsCommand.calculateKD(megawallsStats.get("kills").asInt, megawallsStats.get("deaths").asInt))
        outText.append("\n")
        outText.append(McColorHelper.GOLD + "Wins: " + McColorHelper.BOLD + megawallsStats.get("wins") + McColorHelper.RESET + McColorHelper.GOLD +
                " | W/L: " + McColorHelper.BOLD + StatsCommand.calculateKD(megawallsStats.get("wins").asInt, megawallsStats.get("losses").asInt))
        outText.append("\n")
        outText.append("\n")
        // Classes
        outText.append(GOLD + "Classes:")
        outText.append("\n")
        val spacesNum = 8
        outText.append(GOLD + "Class" + " ".repeat(spacesNum) + "FKs" + " ".repeat(spacesNum) + "Wins" + " ".repeat(spacesNum) + "Kills" + " ".repeat(spacesNum) + "Deaths" + " ".repeat(spacesNum) + "K/D")
        outText.append("\n")
        for (mwClass in playerHasClasses) {
            var finalKills = "0"
            if (megawallsStats.has("finalKills_" + mwClass.capitalize()))
                finalKills = megawallsStats.get("finalKills_" + mwClass.capitalize()).asString
            var wins = "0"
            if (megawallsStats.has("wins_" + mwClass.capitalize()))
                wins = megawallsStats.get("wins_" + mwClass.capitalize()).asString
            var kills = "0"
            if (megawallsStats.has("kills_" + mwClass.capitalize()))
                kills = megawallsStats.get("kills_" + mwClass.capitalize()).asString
            var deaths = "0"
            if (megawallsStats.has("deaths_" + mwClass.capitalize()))
                deaths = megawallsStats.get("deaths_" + mwClass.capitalize()).asString
            var kd = kills
            if (deaths != "0")
                kd = StatsCommand.calculateKD(kills.toInt(), deaths.toInt()).toString()

            outText.append(GOLD + mwClass.capitalize())
            // Magic number 5 is the "Class" in the header string.
            outText.append(GOLD + " ".repeat(spacesNum + 5 - mwClass.length))
            outText.append(GOLD + finalKills)
            outText.append(GOLD + " ".repeat(spacesNum + 3 - finalKills.length))
            outText.append(GOLD + wins)
            outText.append(GOLD + " ".repeat(spacesNum + 4 - wins.length))
            outText.append(GOLD + kills)
            outText.append(GOLD + " ".repeat(spacesNum + 5 - kills.length))
            outText.append(GOLD + deaths)
            outText.append(GOLD + " ".repeat(spacesNum + 6 - deaths.length))
            outText.append(GOLD + kd)
        }



        return outText.toString()
    }
}