package eladkay.hudpixel.command.statsgamemodes

import eladkay.hudpixel.command.StatsCommand
import eladkay.hudpixel.util.asInt
import eladkay.hudpixel.util.plus
import net.hypixel.api.reply.PlayerReply
import net.unaussprechlich.hudpixelextended.util.McColorHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper.*

object SkyWarsStats : McColorHelper {
    fun getSkywarsStats(pr: PlayerReply): String {
        val outText = StringBuilder()
        val skywarsStats = pr.player.get("stats").asJsonObject.get("SkyWars").asJsonObject
        // Title
        outText.append(pr.formattedDisplayName + GOLD + "'s SkyWars Stats:")
        outText.append("\n")

        // Coins
        outText.append(GOLD + "Coins: " + BOLD + skywarsStats.get("coins"))
        outText.append("\n")
        outText.append("\n")
        // Solo stats
        outText.append(GOLD + "Solo - K: " + BOLD + skywarsStats.get("kills_solo") + RESET + GOLD +
                " | D: " + BOLD + skywarsStats.get("deaths_solo") + RESET + GOLD +
                " | K/D " + BOLD + StatsCommand.calculateKD(skywarsStats.get("kills_solo").asInt, skywarsStats.get("deaths_solo").asInt) + RESET + GOLD +
                " | W: " + BOLD + skywarsStats.get("wins_solo") + RESET + GOLD +
                " | W/L: " + BOLD + StatsCommand.calculateKD(skywarsStats.get("wins_solo").asInt, skywarsStats.get("losses_solo").asInt))
        outText.append("\n")
        //Teams
        outText.append(GOLD + "Teams - K: " + BOLD + skywarsStats.get("kills_team") + RESET + GOLD +
                " | D: " + BOLD + skywarsStats.get("deaths_team") + RESET + GOLD +
                " | K/D " + BOLD + StatsCommand.calculateKD(skywarsStats.get("kills_team").asInt, skywarsStats.get("deaths_team").asInt) + RESET + GOLD +
                " | W: " + BOLD + skywarsStats.get("wins_team") + RESET + GOLD +
                " | W/L: " + BOLD + StatsCommand.calculateKD(skywarsStats.get("wins_team").asInt, skywarsStats.get("losses_team").asInt))
        outText.append("\n")
        // Mega
        outText.append(GOLD + "Mega - K: " + BOLD + skywarsStats.get("kills_mega") + RESET + GOLD +
                " | D: " + BOLD + skywarsStats.get("deaths_mega") + RESET + GOLD +
                " | K/D " + BOLD + StatsCommand.calculateKD(skywarsStats.get("kills_mega").asInt, skywarsStats.get("deaths_mega").asInt) + RESET + GOLD +
                " | W: " + BOLD + skywarsStats.get("wins_mega") + RESET + GOLD +
                " | W/L: " + BOLD + StatsCommand.calculateKD(skywarsStats.get("wins_mega").asInt, skywarsStats.get("losses_mega").asInt))
        outText.append("\n")
        // Ranked
        outText.append(GOLD + "Ranked - K: " + BOLD + skywarsStats.get("kills_ranked") + RESET + GOLD +
                " | D: " + BOLD + skywarsStats.get("deaths_ranked") + RESET + GOLD +
                " | K/D " + BOLD + StatsCommand.calculateKD(skywarsStats.get("kills_ranked").asInt, skywarsStats.get("deaths_ranked").asInt) + RESET + GOLD +
                " | W: " + BOLD + skywarsStats.get("wins_ranked") + RESET + GOLD +
                " | W/L: " + BOLD + StatsCommand.calculateKD(skywarsStats.get("wins_ranked").asInt, skywarsStats.get("losses_ranked").asInt))
        outText.append("\n")
        outText.append("\n")
        // Total
        outText.append(GOLD + "Total - K: " + BOLD + skywarsStats.get("kills") + RESET + GOLD +
                " | D: " + BOLD + skywarsStats.get("deaths") + RESET + GOLD +
                " | K/D " + BOLD + StatsCommand.calculateKD(skywarsStats.get("kills_ranked").asInt, skywarsStats.get("deaths").asInt) + RESET + GOLD +
                " | W: " + BOLD + skywarsStats.get("wins") + RESET + GOLD +
                " | W/L: " + BOLD + StatsCommand.calculateKD(skywarsStats.get("wins").asInt, skywarsStats.get("losses").asInt))
        outText.append("\n")
        return outText.toString()
    }
}