package com.palechip.hudpixelmod.extended.update

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.palechip.hudpixelmod.HudPixelMod.DEFAULT_VERSION
import com.palechip.hudpixelmod.extended.util.LoggerHelper.logError
import com.palechip.hudpixelmod.extended.util.LoggerHelper.logWarn
import com.palechip.hudpixelmod.extended.util.McColorHelper
import com.palechip.hudpixelmod.util.ChatMessageComposer
import net.minecraft.client.Minecraft.getMinecraft
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TextFormatting.*
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.System.out
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/* **********************************************************************************************************************
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 **********************************************************************************************************************/
@SideOnly(Side.CLIENT)
class UpdateNotifier//pauses the thread to
(wait: Boolean) : McColorHelper {

    init {
        object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                getHttpRequest()
            }
        }.start()
    }

    private fun getHttpRequest() {

        try {

            val u = URL(LINK_TO_UPDATEFILE)
            val con = u.openConnection() as HttpsURLConnection
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
            val br = BufferedReader(InputStreamReader(con.inputStream))

            val sBuilder = StringBuilder()
            var buff = ""
            buff = br.readLine()
            while (buff != null) {
                sBuilder.append(buff)
                buff = br.readLine()
            }
            val data = sBuilder.toString()
            out.println(data)
            val jsonObject = jsonParser(data)
            if (!getStringFromJson(KEY_VERSION, jsonObject).equals(DEFAULT_VERSION, ignoreCase = true) && !DEFAULT_VERSION.contains("dev"))
                printUpdateMessage(jsonObject)
        } catch (e: MalformedURLException) {
            logError("[UpdateNotifier]: Something went wrong while loading the URL for the update file")
            e.printStackTrace()
        } catch (e: IOException) {
            logError("[UpdateNotifier]: Something went wrong while reading the update file!")
            e.printStackTrace()
        }

    }

    private fun getStringFromJson(key: String, jsonObject: JsonObject): String {
        try {
            if (jsonObject.get(key) != null) {
                return removeQuotes(jsonObject.get(key).toString())
            } else {
                logWarn("[UpdateNotifier]: Key '$key' not Found in Json!")
                return ""
            }
        } catch (e: Exception) {
            logError("[UpdateNotifier]: Something went wrong while extracting Key '$key' from Json!")
            e.printStackTrace()
            return "${DARK_RED}Something went wrong while extracting Key '" + key + "' from Json!"
        }

    }

    private fun jsonParser(data: String): JsonObject {
        val jsonParser = JsonParser()
        return jsonParser.parse(data).asJsonObject
    }

    private fun removeQuotes(s: String): String {
        return s.replace("\"", "")
    }

    operator fun TextFormatting.plus(string: String) = "$this$string"
    operator fun String.plus(string: TextFormatting) = "$this$string"
    /**
     * Yeah, best code-style EU .... it's messy but it's just for generating a update message,
     * so no need for being nice code :P

     * @param jsonObject updateJson Array
     */
    private fun printUpdateMessage(jsonObject: JsonObject) {
        if (done) return
        done = true
        printMessage("${GOLD}SEPARATION_MESSAGE")
        printMessage("")

        //GOING TO PRINT THE DOWLOADLINK
        printMessage(GOLD + "------" + GREEN + " A new version of " + WHITE + "[" + GOLD + "Hud"
                + TextFormatting.YELLOW + "Pixel" + WHITE + "]" + GREEN + " has been published" + GOLD + " ------")
        ChatMessageComposer("v" + getStringFromJson(KEY_VERSION, jsonObject) + TextFormatting.YELLOW).appendMessage(ChatMessageComposer(" > click here to download the newest version < ", TextFormatting.ITALIC).makeLink(getStringFromJson(KEY_DOWNLOADLINK, jsonObject))).send()

        printMessage("")

        //GOING TO PRINT THE UPDATE NOE OR DIE TEXT
        printMessage(GRAY + "You are currently running v" + DEFAULT_VERSION + "! This version will now no longer be supported " +
                "by the HudPixelTeam! Make sure to update to the newest version befor sending any bug-reports or feature requests!" +
                " HudPixel Reloaded v3 is still in development state, so expect bugs and new features at any time!")

        printMessage("")

        //GOING TO PRINT THE CURRENT BUGREPORTMESSAGE
        printMessage("$GOLD------$GREEN You can enter a bugreport directly on GitHub $GOLD------")
        ChatMessageComposer(" press this link to report a bug on GitHub", TextFormatting.RED).makeLink("https://github.com/unaussprechlich/HudPixelExtended/issues").send()

        //GOING TO PRINT THE CHANGELOG
        printMessage("")
        printMessage(GOLD + "----------------- " + GREEN + "Changelog for "
                + TextFormatting.YELLOW + "v" + getStringFromJson(KEY_VERSION, jsonObject)
                + GOLD + " -----------------")
        val jsonObject2 = jsonObject.get("UpdateMessages").asJsonObject
        var i = 1
        while (true) {
            val buff: String
            buff = getStringFromJson("$i", jsonObject2)
            if (buff != "") {
                printChangelogStyle(buff)
            } else {
                break
            }
            i++
        }

        printMessage(GOLD + SEPARATION_MESSAGE)
    }

    /**
     * colors the brackets in the right color

     * @param s text to print
     */
    private fun printChangelogStyle(s: String) {
        if (s.startsWith("[+]")) {
            printMessage(GREEN + "[+]"
                    + printChangelogText(s.substring(3)))
        } else if (s.startsWith("[-]")) {
            printMessage(DARK_RED + "[-]"
                    + printChangelogText(s.substring(3)))
        } else {
            printMessage(TextFormatting.YELLOW + s.substring(0, s.indexOf("]") + 1)
                    + printChangelogText(s.substring(s.indexOf("]") + 1)))
        }
    }

    /**
     * makes everything between ' those quotes white

     * @param s text to process
     * *
     * @return the processed text
     */
    private fun printChangelogText(s: String): String {
        val singleWords = s.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sBuilder = StringBuilder()
        for (i in singleWords) {
            var i = i
            if (i.startsWith("'") && i.endsWith("'")) {
                i = i.replace("'", "")
                sBuilder.append(WHITE + " ").append(i)
            } else {
                sBuilder.append(GRAY + " ").append(i)
            }
        }
        return sBuilder.toString()
    }

    /**
     * prints the message to the clientchat

     * @param message the message
     */
    private fun printMessage(message: String) {
        getMinecraft().ingameGUI.chatGUI.printChatMessage(
                TextComponentString(message))
    }

    companion object {

        var SEPARATION_MESSAGE = "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC" +
                "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC" +
                "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC"
        internal var done = false
        //######################################################################################################################
        private val KEY_VERSION = "Version"
        private val KEY_UPDATEMESSAGE = "UpdateMessage"
        private val KEY_DOWNLOADLINK = "DownloadLink"
        //######################################################################################################################
        private val LINK_TO_UPDATEFILE = "https://raw.githubusercontent.com/unaussprechlich/HudPixelExtended/1.8.9-release/checkforversion/Version.json"
    }

}
