package eladkay.hudpixel.command.statsgamemodes

import eladkay.hudpixel.command.StatsCommand
import eladkay.hudpixel.util.GameType
import eladkay.hudpixel.util.asInt
import eladkay.hudpixel.util.plus
import net.hypixel.api.reply.PlayerReply
import net.unaussprechlich.hudpixelextended.util.McColorHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper.*
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TNTGamesStats {
    fun getTNTGamesStats(pr: PlayerReply?): String {
        val wizardsClasses = arrayOf("firewizard", "bloodwizard", "kineticwizard", "icewizard", "witherwizard", "toxicwizard")
        val outText: StringBuilder = StringBuilder()
        val tntgamesStats = pr!!.player.get("stats").asJsonObject.get(GameType.TNT_GAMES.statsName).asJsonObject
        // Header.
        outText.append(pr.formattedDisplayName + GOLD + "'s TNT Games Stats:")
        outText.append("\n")
        outText.append(GOLD + "Coins: " + BOLD + tntgamesStats.get("coins"))
        outText.append("\n")
        outText.append("\n")

        // TNT Run.
        outText.append(GOLD + BOLD + "TNT Run:")
        outText.append("\n")
        outText.append(GOLD + "Wins: " + BOLD + tntgamesStats.get("wins_tntrun"))
        outText.append("\n")
        outText.append(GOLD + "Record: " + BOLD + String.format("%d:%02d", TimeUnit.SECONDS.toMinutes(tntgamesStats.get("record_tntrun").asLong), tntgamesStats.get("record_tntrun").asLong % 60))
        outText.append("\n")
        outText.append("\n")

        // PVP Run.
        outText.append(GOLD + BOLD + "PVP Run:")
        outText.append("\n")
        outText.append(GOLD + "Wins: " + BOLD + tntgamesStats.get("wins_pvprun"))
        outText.append("\n")
        outText.append(GOLD + "Kills: " + BOLD + tntgamesStats.get("kills_pvprun"))
        outText.append("\n")
        outText.append(GOLD + "Record: " + BOLD + String.format("%d:%02d", TimeUnit.SECONDS.toMinutes(tntgamesStats.get("record_pvprun").asLong), tntgamesStats.get("record_pvprun").asLong % 60))
        outText.append("\n")
        outText.append("\n")

        // TNT Tag.
        outText.append(GOLD + BOLD + "TNT Tag:")
        outText.append("\n")
        outText.append(GOLD + "Wins: " + BOLD + tntgamesStats.get("wins_tntag"))
        outText.append("\n")
        outText.append("\n")

        // Bowspleef.
        outText.append(GOLD + BOLD + "Bow Spleef:")
        outText.append("\n")
        outText.append(GOLD + "Wins: " + BOLD + tntgamesStats.get("wins_bowspleef"))
        outText.append("\n")
        outText.append("\n")

        // Wizards.
        outText.append(GOLD + BOLD + "Wizards:")
        outText.append("\n")
        outText.append(GOLD + "Kills: " + BOLD + tntgamesStats.get("kills_capture") + RESET + GOLD +
                " | Deaths: " + BOLD + tntgamesStats.get("deaths_capture") + RESET + GOLD +
                " | K/D: " + BOLD + StatsCommand.calculateKD(tntgamesStats.get("kills_capture").asInt, tntgamesStats.get("deaths_capture").asInt))
        outText.append("\n")
        for (className in wizardsClasses) {
            // Sneaky. Prints name.
            outText.append(GOLD + className.split("wizard")[0].capitalize() + " Wizard: ")
            if (tntgamesStats.has("new_" + className + "_explode")) {
                outText.append(if (tntgamesStats.get("new_" + className + "_explode").asInt == 10) 9 else tntgamesStats.get("new_" + className + "_explode").asInt)
            } else if (tntgamesStats.has(className + "_explode")) {
                outText.append(if (tntgamesStats.get(className + "_explode").asInt == 10) 9 else tntgamesStats.get(className + "_explode").asInt)
            } else {
                // Level 1 explode.
                outText.append(1)
            }
            outText.append(" and ")
            if (tntgamesStats.has("new_" + className + "_regen")) {
                outText.append(if (tntgamesStats.get("new_" + className + "_regen").asInt == 10) 9 else tntgamesStats.get("new_" + className + "_regen").asInt)
            } else if (tntgamesStats.has(className + "_regen")) {
                outText.append(if (tntgamesStats.get(className + "_regen").asInt == 10) 9 else tntgamesStats.get(className + "_regen").asInt)
            } else {
                // Level 1 regen.
                outText.append(1)
            }
            outText.append("\n")
        }
        return outText.toString()
    }
}
