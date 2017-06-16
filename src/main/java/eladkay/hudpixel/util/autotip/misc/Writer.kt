package eladkay.hudpixel.util.autotip.misc

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.util.FileUtil
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

class Writer : Runnable {

    override fun run() {
        try {
            FileWriter(Autotip.USER_DIR + "options.at").use { writeOptions ->
                write(writeOptions, Autotip.toggle.toString() + ls)
                write(writeOptions, Autotip.messageOption.name + ls)
                write(writeOptions, "true" + ls)
                write(writeOptions, Autotip.totalTipsSent.toString() + ls)
            }

            if (lastDate != FileUtil.date) {
                TipTracker.tipsSent = 0
                TipTracker.tipsReceived = 0
                TipTracker.tipsSentEarnings.clear()
                TipTracker.tipsReceivedEarnings.clear()
            }

            val dailyStats = FileWriter(
                    Autotip.USER_DIR + "stats" + File.separator + FileUtil.date + ".at")
            write(dailyStats, TipTracker.tipsSent.toString() + ":" + TipTracker.tipsReceived + ls)
            write(dailyStats, "0" + ls)

            val games = Stream.concat(
                    TipTracker.tipsSentEarnings.keys.stream(),
                    TipTracker.tipsReceivedEarnings.keys.stream()
            ).distinct().collect(Collectors.toList<String>())

            games.forEach { game ->
                val sent = if (TipTracker.tipsSentEarnings.containsKey(game))
                    TipTracker.tipsSentEarnings[game]
                else
                    0
                val received = if (TipTracker.tipsReceivedEarnings.containsKey(game))
                    TipTracker.tipsReceivedEarnings[game]
                else
                    0
                write(dailyStats, "$game:$sent:$received$ls")
            }
            dailyStats.close()

            lastDate = FileUtil.date

            if (File(Autotip.USER_DIR + "tipped.at").exists()) {
                BufferedReader(
                        FileReader(Autotip.USER_DIR + "tipped.at")).use { f ->
                    val lines = f.lines().collect(Collectors.toList())
                    if (lines.size >= 1) {
                        val date = lines[0]
                        if (date != FileUtil.serverDate) {
                            Autotip.alreadyTipped.clear()
                        }
                    }
                }
            }
            FileWriter(Autotip.USER_DIR + "tipped.at").use { tippedNames ->
                write(tippedNames, FileUtil.serverDate + ls)
                for (name in Autotip.alreadyTipped) {
                    write(tippedNames, name + ls)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun write(writer: FileWriter, text: String) {
        try {
            writer.write(text)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {

        private var lastDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
        private val ls = System.lineSeparator()

        fun execute() {
            Autotip.THREAD_POOL.submit(Writer())
        }
    }

}