package eladkay.hudpixel.util.autotip.misc

import eladkay.hudpixel.util.autotip.Autotip
import java.util.*

object TipTracker {

    var tipsSentHistory: MutableMap<Long, String> = TreeMap(Collections.reverseOrder<Long>())
    var tipsSentEarnings: MutableMap<String, Int> = HashMap()
    var tipsReceivedEarnings: MutableMap<String, Int> = HashMap()
    var tipsSent = 0
    var tipsReceived = 0
    var karmaCount = 0

    fun addTip(username: String) {
        tipsSentHistory.put(System.currentTimeMillis(), username)
        tipsSent++
        Autotip.totalTipsSent++
        Autotip.alreadyTipped.add(username)
        println("Tipped: " + username)
        Writer.execute()
    }

}