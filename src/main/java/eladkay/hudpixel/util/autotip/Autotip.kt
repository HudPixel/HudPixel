package eladkay.hudpixel.util.autotip

import eladkay.hudpixel.HudPixelMod
import eladkay.hudpixel.modulargui.components.AutotipWaveModularGuiProvider
import eladkay.hudpixel.util.autotip.command.AutotipCommand
import eladkay.hudpixel.util.autotip.command.LimboCommand
import eladkay.hudpixel.util.autotip.command.TipHistoryCommand
import eladkay.hudpixel.util.autotip.event.ChatListener
import eladkay.hudpixel.util.autotip.event.Tipper
import eladkay.hudpixel.util.autotip.util.FileUtil
import eladkay.hudpixel.util.autotip.util.Hosts
import eladkay.hudpixel.util.autotip.util.MessageOption
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.FMLCommonHandler
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

// Handles autotipping in the Autotip Network.
object Autotip {

    // For threads
    val THREAD_POOL = Executors
            .newCachedThreadPool()

    // The run directory
    var USER_DIR = ""

    // The Minecraft instance
    var mc = Minecraft.getMinecraft()

    // Whether or not to show autotip messages.
    var messageOption = MessageOption.SHOWN

    // The player's UUID
    var playerUUID = ""

    // Alias, gets whether or not the player is on Hypixel
    var onHypixel: Boolean = HudPixelMod.isHypixelNetwork
        get() = HudPixelMod.isHypixelNetwork

    // Whether or not autotip is even on. Requires both an internal toggle and a config option.
    var toggle: Boolean = true
        get() {
            return field && AutotipWaveModularGuiProvider.autotip
        }
        set(value) {
            field = value
            AutotipWaveModularGuiProvider.autotip = value
        }


    // Tracks total tips sent.
    var totalTipsSent: Int = 0

    // Tracks boosters that were already tipped.
    var alreadyTipped: MutableList<String> = ArrayList()


    // Runs on init
    init {
        try {
            playerUUID = Minecraft.getMinecraft().session.profile.id.toString()
            USER_DIR = "mods" + File.separator + "hudpixelautotip" + File.separator + playerUUID + File.separator
            this.registerEvents(
                    Tipper(),
                    ChatListener()
            )
            AutotipCommand()
            TipHistoryCommand()
            LimboCommand()

            // Handles stat files.
            FileUtil.getVars()

            // Handles connection to the Autotip Network.
            Hosts.updateHosts()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    // Registers event classes.
    private fun registerEvents(vararg events: Any) {
        events.forEach { event ->
            // To both buses.
            MinecraftForge.EVENT_BUS.register(event)
            FMLCommonHandler.instance().bus().register(event)
        }
    }





}