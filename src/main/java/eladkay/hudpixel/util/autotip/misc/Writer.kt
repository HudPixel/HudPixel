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