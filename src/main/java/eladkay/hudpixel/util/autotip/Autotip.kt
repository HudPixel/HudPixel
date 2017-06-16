package eladkay.hudpixel.util.autotip

import eladkay.hudpixel.modulargui.components.AutotipWaveModularGuiProvider
import eladkay.hudpixel.util.autotip.command.AutotipCommand
import eladkay.hudpixel.util.autotip.command.LimboCommand
import eladkay.hudpixel.util.autotip.command.TipHistoryCommand
import eladkay.hudpixel.util.autotip.event.ChatListener
import eladkay.hudpixel.util.autotip.event.HypixelListener
import eladkay.hudpixel.util.autotip.event.Tipper
import eladkay.hudpixel.util.autotip.misc.AutotipThreadFactory
import eladkay.hudpixel.util.autotip.util.FileUtil
import eladkay.hudpixel.util.autotip.util.Hosts
import eladkay.hudpixel.util.autotip.util.MessageOption
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

object Autotip {

    fun init(event: FMLInitializationEvent) {
        try {
            playerUUID = Minecraft.getMinecraft().session.profile.id.toString()
            USER_DIR = "mods" + File.separator + "hudpixelautotip" + File.separator + playerUUID + File.separator
            this.registerEvents(
                    Tipper(),
                    HypixelListener(),
                    ChatListener()
            )
            AutotipCommand()
            TipHistoryCommand()
            LimboCommand()

            FileUtil.getVars()
            Hosts.updateHosts()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun registerEvents(vararg events: Any) {
        Arrays.asList(*events).forEach { event ->
            MinecraftForge.EVENT_BUS.register(event)
            FMLCommonHandler.instance().bus().register(event)
        }
    }


    val THREAD_POOL = Executors
            .newCachedThreadPool(AutotipThreadFactory)!!
    var USER_DIR = ""

    var mc = Minecraft.getMinecraft()

    var messageOption = MessageOption.SHOWN
    var playerUUID = ""
    var onHypixel = false
    var toggle: Boolean = true
        get() {
            return field && AutotipWaveModularGuiProvider.autotip
        }
        set(value) {
            field = value
            AutotipWaveModularGuiProvider.autotip = value
        }


    var totalTipsSent: Int = 0
    var alreadyTipped: MutableList<String> = ArrayList()


}