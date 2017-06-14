package eladkay.hudpixel.command

import eladkay.hudpixel.command.statsgamemodes.GeneralStats
import eladkay.hudpixel.command.statsgamemodes.MegaWallsStats
import eladkay.hudpixel.command.statsgamemodes.SkyWarsStats
import eladkay.hudpixel.command.statsgamemodes.TNTGamesStats
import eladkay.hudpixel.util.GameType
import eladkay.hudpixel.util.plus
import net.hypixel.api.reply.PlayerReply
import net.minecraft.client.Minecraft
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.unaussprechlich.hudpixelextended.hypixelapi.ApiQueueEntryBuilder
import net.unaussprechlich.hudpixelextended.hypixelapi.callbacks.PlayerResponseCallback
import net.unaussprechlich.hudpixelextended.util.LoggerHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper.*
import java.util.*

object StatsCommand : HpCommandBase(), PlayerResponseCallback, McColorHelper {

    var chosenGamemode = GameType.UNKNOWN

    val GM_NOT_SUPPORTED_TEXT = GOLD + "This gamemode is not supported yet!"
    val PLANCKE_STATS_URL = "https://plancke.io/hypixel/stats/"


    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/stats <Player Name> [Minigame]"
    }

    override fun getCommandName(): String {
        return "stats"
    }

    // wtf this crashes.
//    override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<out String>?, pos: BlockPos?): MutableList<String> {
//        // TODO add all the gametypes supported preferably using gametypes.
//        if (args != null && args.size > 1) {
//            return mutableListOf("skywars", "megawalls", "general")
//        } else
//            return super.addTabCompletionOptions(sender, args, pos)
//    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (args!!.size > 2) {
            sender!!.addChatMessage(ChatComponentText(getCommandUsage(sender)))
        } else {
            if (args.size == 2) {
                chosenGamemode = GameType.getTypeByInput(args[1].toLowerCase())
                if (chosenGamemode == GameType.UNKNOWN)
                    sender?.addChatMessage(ChatComponentText(GOLD + "Could not find that game. Showing general stats."))
            }
            else
                chosenGamemode = GameType.UNKNOWN

            sender!!.addChatMessage(ChatComponentText(GOLD + "Trying to get stats..."))
            if (args.isEmpty()) {
                ApiQueueEntryBuilder.newInstance()
                        .playerRequestByName(sender.name)
                        .setCallback(this)
                        .create()
            } else {
                ApiQueueEntryBuilder.newInstance()
                        .playerRequestByName(args[0])
                        .setCallback(this)
                        .create()
            }
        }
    }

    override fun onPlayerResponse(playerReply: PlayerReply?) {
        LoggerHelper.logInfo("Printing PlayerReply...")
        val outText = StringBuilder()
        val player = Minecraft.getMinecraft().thePlayer

        if (playerReply!!.player == null || !playerReply.isSuccess) {
            outText.append(RED + "An error has occurred; try entering a valid name.")
        } else {
            when (chosenGamemode) {
                GameType.QUAKECRAFT -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.THE_WALLS -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.PAINTBALL -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.BLITZ -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.VAMPIREZ -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.ARENA -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.UHC -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.COPS_AND_CRIMS -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.WARLORDS -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.ARCADE_GAMES -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.TURBO_KART_RACERS -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.SPEED_UHC -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.CRAZY_WALLS -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.SMASH_HEROES -> outText.append(GM_NOT_SUPPORTED_TEXT)
                GameType.SKYCLASH -> outText.append(GM_NOT_SUPPORTED_TEXT)

                GameType.SKYWARS -> outText.append(SkyWarsStats.getSkywarsStats(playerReply))
                GameType.MEGA_WALLS -> outText.append(MegaWallsStats.getMegaWallsStats(playerReply))
                GameType.TNT_GAMES -> outText.append(TNTGamesStats.getTNTGamesStats(playerReply))

                GameType.HOUSING -> outText.append(GOLD + "lol 0 cookies for you.")

                else -> outText.append(GeneralStats.getGeneralStats(playerReply))
            }
        }


        if (playerReply.player != null) player.addChatComponentMessage(ChatComponentText("${BLUE}Plancke.io stats link").setChatStyle(ChatStyle().setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "$PLANCKE_STATS_URL${playerReply.displayName}"))))
        player.addChatMessage(ChatComponentText(outText.toString()))
    }

    // Stole this from AbstractStatsViewer
    fun calculateKD(kills: Int, deaths: Int, precision: Byte): Double {
        if (deaths > 0)
            return Math.round(kills.toDouble() / deaths.toDouble() * (Math.pow(10.0, precision.toDouble()))).toDouble() / (Math.pow(10.0, precision.toDouble()))
        else
            return kills.toDouble()
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
}