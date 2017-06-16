package eladkay.hudpixel.util.autotip.event

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.misc.StartLogin
import eladkay.hudpixel.util.autotip.util.UniversalUtil
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent

class HypixelListener {

    @SubscribeEvent
    fun playerLoggedIn(event: ClientConnectedToServerEvent) {
        lastIp = UniversalUtil.getRemoteAddress(event).toString().toLowerCase()
        if (lastIp.contains(".hypixel.net") || lastIp.contains("209.222.115.14")) {
            Autotip.onHypixel = true
            Tipper.waveCounter = 910
            Autotip.THREAD_POOL.submit(StartLogin())
        } else {
            Autotip.onHypixel = false
        }
    }

    @SubscribeEvent
    fun playerLoggedOut(event: ClientDisconnectionFromServerEvent) {
        Autotip.onHypixel = false
    }

    companion object {

        var lastIp: String = ""
    }

}