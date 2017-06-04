package eladkay.hudpixel.command.statsgamemodes

import eladkay.hudpixel.util.plus
import net.hypixel.api.reply.PlayerReply
import net.hypixel.api.util.ILeveling
import net.unaussprechlich.hudpixelextended.util.LoggerHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper
import java.math.BigDecimal
import java.math.MathContext
import java.text.DateFormat
import java.util.*

object GeneralStats {
    fun getGeneralStats(pr: PlayerReply?): String {
        val outText: StringBuilder = StringBuilder()
        // Rank and name.
        outText.append(pr?.formattedDisplayName)
        // Level. Basically makes it 42.24 instead of 42.2374893749287.
        outText.append(McColorHelper.GOLD + " - Level " + McColorHelper.BOLD + Math.round(pr!!.level * 100).toDouble() / 100)
        outText.append("\n")
        // First and last login.
        outText.append(McColorHelper.GOLD + "First login: " + McColorHelper.BOLD + DateFormat.getInstance().format(Date(pr.player.get("firstLogin").asLong)))
            outText.append("\n")
        return outText.toString()
    }
}