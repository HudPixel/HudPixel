/*******************************************************************************
 * HudPixelReloaded
 *
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses under forge-docs/. These parts can be downloaded at files.minecraftforge.net.
 *
 * This project contains a unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 *
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously intended for usage in this kind of application. By default, all rights are reserved.
 *
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license. The majority of code left from palechip's creations is the component implementation.
 *
 * The ported version to Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license (to be changed to the new license as detailed below in the next minor update).
 *
 * For the rest of the code and for the build the following license applies:
 *
 * alt-tag
 *
 * HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions. Based on a work at HudPixelExtended & HudPixel.
 *
 * Restrictions:
 *
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow the following license terms and the license terms given by the listed above Creative Commons License, however in extreme cases the authors reserve the right to revoke all rights for usage of the codebase.
 *
 * PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their code, but only when it is used separately from HudPixel and any license header must indicate that.
 * You shall not claim ownership over this project and repost it in any case, without written permission from at least two of the authors.
 * You shall not make money with the provided material. This project is 100% non commercial and will always stay that way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed code is merged to the release branch you cannot revoke the given freedoms by this license.
 * If your own project contains a part of the licensed material you have to give the authors full access to all project related files.
 * You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors reserve the right to take down any infringing project.
 ******************************************************************************/
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