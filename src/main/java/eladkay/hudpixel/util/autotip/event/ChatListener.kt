package eladkay.hudpixel.util.autotip.event

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.command.LimboCommand
import eladkay.hudpixel.util.autotip.misc.TipTracker
import eladkay.hudpixel.util.autotip.misc.Writer
import eladkay.hudpixel.util.autotip.util.ChatColor
import eladkay.hudpixel.util.autotip.util.ClientMessage
import eladkay.hudpixel.util.autotip.util.MessageOption
import eladkay.hudpixel.util.autotip.util.UniversalUtil
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import java.util.regex.Matcher
import java.util.regex.Pattern

class ChatListener {

    private val xpPattern = Pattern.compile("\\+50 experience \\(Gave a player a /tip\\)")
    private val playerPattern = Pattern.compile("You tipped (?<player>\\w+) in .*")
    private val coinPattern = Pattern.compile(
            "\\+(?<coins>\\d+) coins for you in (?<game>.+) for being generous :\\)")
    private val earnedPattern = Pattern.compile(
            "You earned (?<coins>\\d+) coins and (?<xp>\\d+) experience from (?<game>.+) tips in the last minute!")

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {

        if (!Autotip.onHypixel) {
            return
        }

        val msg = UniversalUtil.getUnformattedText(event)
        val mOption = Autotip.messageOption

        if (Autotip.toggle) {
            if (msg == "Slow down! You can only use /tip every few seconds."
                    || msg == "Still processing your most recent request!"
                    || msg == "You are not allowed to use commands as a spectator!"
                    || msg == "You cannot tip yourself!"
                    || msg.startsWith("You can only use the /tip command")
                    || msg.startsWith("You can't tip the same person")
                    || msg.startsWith("You've already tipped someone in the past hour in ")
                    || msg.startsWith("You've already tipped that person")) {
                event.isCanceled = true
            }

            if (xpPattern.matcher(msg).matches()) {
                event.isCanceled = mOption == MessageOption.COMPACT || mOption == MessageOption.HIDDEN
                return
            }

            val playerMatcher = playerPattern.matcher(msg)
            if (playerMatcher.matches()) {
                TipTracker.addTip(playerMatcher.group("player"))
                event.isCanceled = mOption == MessageOption.HIDDEN
                return
            }

            val coinMatcher = coinPattern.matcher(msg)
            if (coinMatcher.matches()) {
                val coins = Integer.parseInt(coinMatcher.group("coins"))
                val game = coinMatcher.group("game")

                (TipTracker.tipsSentEarnings as java.util.Map<String, Int>).merge(game, coins) { a, b -> a + b }
                event.isCanceled = mOption == MessageOption.COMPACT || mOption == MessageOption.HIDDEN

                println("Earned $coins coins in $game")
                return
            }

            val earnedMatcher = earnedPattern.matcher(msg)
            if (earnedMatcher.matches()) {
                val coins = Integer.parseInt(earnedMatcher.group("coins"))
                val xp = Integer.parseInt(earnedMatcher.group("xp"))
                val game = earnedMatcher.group("game")

                (TipTracker.tipsReceivedEarnings as java.util.Map<String, Int>).merge(game, coins) { a, b -> a + b }
                TipTracker.tipsReceived += xp / 60
                Writer.execute()

                if (mOption == MessageOption.COMPACT) {
                    ClientMessage.sendRaw(
                            String.format("%sEarned %s%d coins%s and %s%d experience%s in %s.",
                                    ChatColor.GREEN, ChatColor.YELLOW, coins,
                                    ChatColor.GREEN, ChatColor.BLUE, xp,
                                    ChatColor.GREEN, game
                            ))
                }
                event.isCanceled = mOption == MessageOption.COMPACT || mOption == MessageOption.HIDDEN
                println("Earned $coins coins and $xp experience in $game")
                return
            }
        }

        if (LimboCommand.executed && msg.startsWith("A kick occurred in your connection") &&
                msg.contains("Illegal characters")) {
            event.isCanceled = true
            LimboCommand.executed = false
        }

    }
}
