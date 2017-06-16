package eladkay.hudpixel.util.autotip.event

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.misc.FetchBoosters
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

import java.util.ArrayList

class Tipper {
    private var unixTime: Long = 0

    @SubscribeEvent
    fun gameTick(event: ClientTickEvent) {
        if (Autotip.onHypixel && Autotip.toggle && unixTime != System.currentTimeMillis() / 1000L) {
            if (waveCounter == waveLength) {
                Autotip.THREAD_POOL.submit(FetchBoosters())
                waveCounter = 0
            }

            if (!tipQueue.isEmpty()) {
                tipDelay++
            } else {
                tipDelay = 4
            }

            if (!tipQueue.isEmpty() && tipDelay % 5 == 0) {
                println("Attempting to tip: " + tipQueue[0])
                Autotip.mc.thePlayer.sendChatMessage("/tip " + tipQueue[0])
                tipQueue.removeAt(0)
                tipDelay = 0
            }
            waveCounter++
        }
        unixTime = System.currentTimeMillis() / 1000L
    }

    companion object {

        var waveCounter = 910
        var waveLength = 915
        var tipQueue: MutableList<String> = ArrayList()
        private var tipDelay = 4
    }
}