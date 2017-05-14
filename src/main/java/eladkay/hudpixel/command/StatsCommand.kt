package eladkay.hudpixel.command

import eladkay.hudpixel.command.statsgamemodes.GeneralStats
import eladkay.hudpixel.command.statsgamemodes.MegaWallsStats
import eladkay.hudpixel.command.statsgamemodes.SkyWarsStats
import eladkay.hudpixel.util.GameType
import eladkay.hudpixel.util.plus
import net.hypixel.api.reply.PlayerReply
import net.minecraft.client.Minecraft
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.unaussprechlich.hudpixelextended.hypixelapi.ApiQueueEntryBuilder
import net.unaussprechlich.hudpixelextended.hypixelapi.callbacks.PlayerResponseCallback
import net.unaussprechlich.hudpixelextended.util.LoggerHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper
import net.unaussprechlich.hudpixelextended.util.McColorHelper.*

object StatsCommand : HpCommandBase(), PlayerResponseCallback, McColorHelper {

    var gamemode: String = "general"


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
            if (args.size == 2)
                gamemode = args[1].toLowerCase()
                if (GameType.getTypeByName(gamemode) == GameType.UNKNOWN)
                    sender!!.addChatMessage(ChatComponentText(GOLD + "Could not find that game. Showing general stats."))
            else
                gamemode = "general"

            sender!!.addChatMessage(ChatComponentText("Getting stats..."))
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

        if (playerReply == null || !playerReply.isSuccess) {
            outText.append(RED + "An error has occurred; try entering a valid name.")
        } else {
            when (gamemode) {
                GameType.SKYWARS.nm.replace(" ", "").toLowerCase() -> outText.append(SkyWarsStats.getSkywarsStats(playerReply))
                GameType.MEGA_WALLS.nm.replace(" ", "").toLowerCase() -> outText.append(MegaWallsStats.getMegaWallsStats(playerReply))

                else -> outText.append(GeneralStats.getGeneralStats(playerReply))
            }
        }


        player.addChatMessage(ChatComponentText(outText.toString()))
    }

    // Stole this from AbstractStatsViewer
    fun calculateKD(kills: Int, deaths: Int): Double {
        if (deaths > 0)
            return Math.round(kills.toDouble() / deaths.toDouble() * 1000).toDouble() / 1000
        else
            return kills.toDouble()
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
}