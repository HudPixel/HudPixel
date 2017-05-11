package eladkay.hudpixel.command.statsgamemodes

import eladkay.hudpixel.command.StatsCommand
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
        // Solo stats
        outText.append(GOLD + "Solo - K: " + BOLD + skywarsStats.get("kills_solo") + RESET + GOLD +
                " | D: " + BOLD + skywarsStats.get("deaths_solo") + RESET + GOLD +
                " | A: " + BOLD + skywarsStats.get("assists_solo") + RESET + GOLD +
                " | K/D " + BOLD + StatsCommand.calculateKD(skywarsStats.get("kills_solo").asInt, skywarsStats.get("deaths_solo").asInt))

        return outText.toString()
    }
}