package eladkay.hudpixel.command.statsgamemodes

import eladkay.hudpixel.util.plus
import net.hypixel.api.reply.PlayerReply
import net.unaussprechlich.hudpixelextended.util.McColorHelper
import java.text.DateFormat
import java.util.*

object GeneralStats {
    fun getGeneralStats(pr: PlayerReply?): String {
        val outText: StringBuilder = StringBuilder()
        // Rank and name.
        outText.append(pr?.formattedDisplayName)
        // Level
        outText.append(McColorHelper.GOLD + " - Level " + McColorHelper.BOLD + pr!!.level)
        outText.append("\n")
        // First and last login.
        outText.append(McColorHelper.GOLD + "First login: " + McColorHelper.BOLD + DateFormat.getInstance().format(Date(pr.player.get("firstLogin").asLong)))
            outText.append("\n")
        return outText.toString()
    }
}