package eladkay.hudpixel.util.autotip.util

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.misc.Stats
import eladkay.hudpixel.util.autotip.misc.TipTracker
import eladkay.hudpixel.util.autotip.misc.Writer
import org.apache.commons.io.FileUtils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Objects
import java.util.TimeZone
import java.util.stream.Collectors

object FileUtil {

    /*
    mods/autotip/
    -- stats (dir)
    -- options.at
    -- tipped.at

    mods/autotip/uuid/
    -- stats (dir)
    -- options.at
    -- tipped.at
    */
    /*
    if (exists(user_dir) && (exists(oldDir + "stats") || exists(oldDir + "options.at") || exists(oldDir + "tipped.at"))) {
                FileUtils.copyDirectory(Paths.get(user_dir).toFile(), Paths.get("mods/autotip_temp").toFile());
                FileUtils.deleteDirectory(Paths.get(user_dir).toFile());
            }

            if (!exists(user_dir)) FileUtils.copyDirectory(Paths.get("mods/autotip").toFile(), Paths.get("mods/autotip/uuid").toFile());

            if (exists(user_dir + "stats")) FileUtils.deleteDirectory(Paths.get(oldDir + "stats").toFile());
            if (exists(user_dir + "options.at")) Files.deleteIfExists(Paths.get(oldDir + "options.at"));
            if (exists(user_dir + "tipped.at")) Files.deleteIfExists(Paths.get(oldDir + "tipped.at"));
     */

    @Throws(IOException::class)
    fun getVars() {
        try {
            val statsDir = File(Autotip.USER_DIR + "stats")

            if (!statsDir.exists()) {
                statsDir.mkdirs()
            }

            if (exists(Autotip.USER_DIR + "upgrade-date.at")) {
                val explainCode = "I had to write this crappy code because 2pi didn't learn about proper serialization."
                val date = FileUtils
                        .readFileToString(Paths.get(Autotip.USER_DIR + "upgrade-date.at").toFile())
                var parsed: LocalDate
                try {
                    parsed = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                } catch (e: DateTimeParseException) {
                    e.printStackTrace()
                    parsed = LocalDate.now()
                }

                Stats.setUpgradeDate(parsed)
            } else {
                val date = LocalDate.now().plusDays(1)
                val dateString = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date)
                FileUtils
                        .writeStringToFile(Paths.get(Autotip.USER_DIR + "upgrade-date.at").toFile(),
                                dateString)
                Stats.setUpgradeDate(date)
            }

            // String oldDir = "mods" + File.separator + "autotip" + File.separator;
            var executeWriter = false

            //            if (exists(Autotip.USER_DIR) && (exists(oldDir + "stats") || exists(oldDir + "options.at") || exists(
            //                    oldDir + "tipped.at"))) {
            //                FileUtils.copyDirectory(Paths.get(Autotip.USER_DIR).toFile(),
            //                                        Paths.get("mods" + File.separator + "autotip_temp").toFile());
            //                FileUtils.deleteDirectory(Paths.get(Autotip.USER_DIR).toFile());
            //            }

            //            if (!exists(Autotip.USER_DIR))
            //                FileUtils.copyDirectory(Paths.get(oldDir).toFile(), Paths.get(Autotip.USER_DIR).toFile());
            //
            //            if (exists(Autotip.USER_DIR + "stats") && exists(oldDir + "stats"))
            //                FileUtils.deleteDirectory(Paths.get(oldDir + "stats").toFile());
            //            if (exists(Autotip.USER_DIR + "options.at") && exists(oldDir + "options.at"))
            //                Files.deleteIfExists(Paths.get(oldDir + "options.at"));
            //            if (exists(Autotip.USER_DIR + "tipped.at") && exists(oldDir + "tipped.at"))
            //                Files.deleteIfExists(Paths.get(oldDir + "tipped.at"));

            if (exists(Autotip.USER_DIR + "options.at")) {
                BufferedReader(
                        FileReader(Autotip.USER_DIR + "options.at")).use { readOptions ->
                    val lines = readOptions.lines().collect(Collectors.toList<String>())
                    if (lines.size >= 4) {
                        Autotip.toggle = java.lang.Boolean.parseBoolean(lines[0]) // mod enabled
                        val chatSetting = lines[1]
                        when (chatSetting) {
                            "true", "false" -> Autotip.messageOption = if (java.lang.Boolean.parseBoolean(chatSetting))
                                MessageOption.SHOWN
                            else
                                MessageOption.COMPACT
                            "SHOWN", "COMPACT", "HIDDEN" -> Autotip.messageOption = MessageOption.valueOf(chatSetting)
                            else -> Autotip.messageOption = MessageOption.SHOWN
                        }
                        //lines.get(2); // anonymous tips // does exist, but no use anymore
                        try {
                            Autotip.totalTipsSent = Integer.parseInt(lines[3])
                        } catch (e: NumberFormatException) {
                            Autotip.totalTipsSent = 0
                            executeWriter = true
                        }

                    } else {
                        executeWriter = true
                    }
                }
            } else {
                executeWriter = true
            }

            if (exists(Autotip.USER_DIR + "stats" + File.separator + date + ".at")) {
                BufferedReader(
                        FileReader(
                                Autotip.USER_DIR + "stats" + File.separator + date + ".at")).use { readDaily ->
                    val lines = readDaily.lines().collect(Collectors.toList<String>())
                    if (lines.size >= 2) {
                        val tipStats = lines[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        TipTracker.tipsSent = Integer.parseInt(tipStats[0])
                        TipTracker.tipsReceived = if (tipStats.size > 1) Integer.parseInt(tipStats[1]) else 0
                        TipTracker.karmaCount = Integer.parseInt(lines[1])
                        lines.stream().skip(2).forEach { line ->
                            val stats = line.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            TipTracker.tipsSentEarnings.put(stats[0], Integer.parseInt(stats[1]))
                            if (stats.size > 2) {
                                TipTracker.tipsReceivedEarnings
                                        .put(stats[0], Integer.parseInt(stats[2]))
                            }
                        }
                    }
                }
            } else {
                executeWriter = true
            }

            if (File(Autotip.USER_DIR + "tipped.at").exists()) {
                BufferedReader(
                        FileReader(Autotip.USER_DIR + "tipped.at")).use { f ->
                    val lines = f.lines().collect(Collectors.toList<String>())
                    if (lines.size >= 1) {
                        val date = lines[0]
                        if (date == date) {
                            lines.stream().skip(1).forEach { line -> Autotip.alreadyTipped.add(line) }
                        } else {
                            executeWriter = true
                        }
                    } else {
                        executeWriter = true
                    }
                }
            } else {
                executeWriter = true
            }

            if (executeWriter) {
                Writer.execute()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }

    private fun exists(path: String): Boolean {
        return Files.exists(Paths.get(path))
    }

    val date: String
        get() = SimpleDateFormat("dd-MM-yyyy").format(Date())

    val serverDate: String
        get() {
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            sdf.timeZone = TimeZone.getTimeZone("EST")
            return sdf.format(Date())
        }

}