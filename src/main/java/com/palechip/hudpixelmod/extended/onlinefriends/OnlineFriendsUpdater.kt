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

package com.palechip.hudpixelmod.extended.onlinefriends

import com.mojang.realmsclient.gui.ChatFormatting
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler
import com.palechip.hudpixelmod.extended.util.IEventHandler
import com.palechip.hudpixelmod.extended.util.LoggerHelper
import com.palechip.hudpixelmod.util.plus
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

@SideOnly(Side.CLIENT)
class OnlineFriendsUpdater internal constructor(private val callback: IUpdater) : IEventHandler {
    //######################################################################################################################

    private val onlineFriends = HashMap<String, String>()
    private var friendListExpected = false
    private var friendsListStarted = false
    private var maybeFriendsList = false
    private var lastRequest: Long = 0
    private var hasFinished = false

    private var count = 0
    private var page = 1
    private var requestNextPage = false

    init {
        LoggerHelper.logInfo("[OnlineFriends][Updater]: going to update your online friends!")
        HudPixelExtendedEventHandler.registerIEvent(this)
        onlineFriends.clear()
        requestPage(1)
    }

    private fun requestPage(page: Int) {
        requestNextPage = false
        lastRequest = System.currentTimeMillis()
        if (Minecraft.getMinecraft().thePlayer == null) return
        friendListExpected = true
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/f list " + page)
    }

    /**
     * function which checks if the friendlist is outdated and if there is another
     * request for a second,... /f list X command

     * @firedBY HudPixelExtendedEventHandler > onClientTick()
     */
    override fun onClientTick() {
        if (requestNextPage && System.currentTimeMillis() > lastRequest + REQUEST_DELAY) {
            LoggerHelper.logInfo("[OnlineFriends][Updater]: Why do you have so many friends ... i will request page $page now!")
            requestPage(page)
        }
    }

    /**
     * listens to the chatevents if there is a requested friendlist

     * @param e ClientChatReceivedEven
     * *
     * @firedBY HudPixelExtendedEventHandler > onChat()
     */
    @Throws(Throwable::class)
    override fun onChatReceived(e: ClientChatReceivedEvent) {
        //TODO: THIS IS PRETTY AF ... I NEED MORE VARS TO MAKE IT WORK ... YOU CAN STILL UNDERSTAND HOW IT SEPARATES o.O
        // checks first if there is a request
        if (friendListExpected) {
            val m = e.message.unformattedText
            //starts and stops the friendlistchatparser by the separation message

            if (m.startsWith(SEPARATION_MESSAGE)) {
                if (friendsListStarted) {
                    resetFriendList(false)
                } else {
                    maybeFriendsList = true
                }

            } else if (maybeFriendsList) {
                if (m.contains(FRIENDS_LIST_START)) {
                    maybeFriendsList = false
                    friendsListStarted = true
                } else
                    resetFriendList(true)
            } else if (m.contains(IS_CURRENTLY_OFFLINE)) {
                hasFinished = true
            } else if (!m.contains(IS_CURRENTLY_OFFLINE) && friendsListStarted) {
                count++
                if (count >= 8) {
                    page++
                    requestNextPage = true
                }
                chatParser(m)
            }


            // deletes the message
            e.isCanceled = true
        }
    }

    /**
     * a internal function which separates the message and extracts player, game and servertype

     * @param m message to parse
     */
    private fun chatParser(m: String) {


        //splits the string
        val singleWords = m.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        //the first element has to be the playername
        val playerName = singleWords[0]
        var gameType = ChatFormatting.GRAY + "in an unknown realm."

        //if the player is in any gamemode game/lobby
        if (singleWords[3].equals("a", ignoreCase = true)) {

            if (singleWords[singleWords.size - 1].equals("game", ignoreCase = true))
                gameType = ChatFormatting.RED + singleWords[4]
            else if (singleWords[singleWords.size - 1].equals("lobby", ignoreCase = true))
                gameType = ChatFormatting.GREEN + singleWords[4]

            //needed to add support for games like Crazy Walls who are written in two words
            for (i in 5..singleWords.size - 1) {
                gameType += " "
                gameType += singleWords[i]
            }

            // if the player is in housing
        } else if (singleWords[3].equals("housing", ignoreCase = true))
            gameType = ChatFormatting.GREEN + singleWords[3]
        else if (singleWords[2].equals("idle", ignoreCase = true))
            gameType = ChatFormatting.GRAY + "idle in Limbo"// if the player is idling in the limbo
        LoggerHelper.logInfo("[OnlineFriends][Updater]: [$playerName] -> $gameType")
        onlineFriends.put(playerName, gameType)
    }

    private fun resetFriendList(error: Boolean) {
        if (error) {
            callback.onUpdaterResponse(null)
            LoggerHelper.logInfo("[OnlineFriends][Updater]: Something went wrong on Page: $page Line: $count!")
            HudPixelExtendedEventHandler.unregisterIEvent(this)
            callback.onUpdaterResponse(null)
        } else {
            if (hasFinished) {
                HudPixelExtendedEventHandler.unregisterIEvent(this)
                callback.onUpdaterResponse(onlineFriends)
            }
            friendsListStarted = false
            maybeFriendsList = false
            friendListExpected = false
            hasFinished = false
        }
    }

    override fun onRender() {
    }

    override fun handleMouseInput(i: Int, mX: Int, mY: Int) {
    }

    override fun onMouseClick(mX: Int, mY: Int) {
    }


    override fun everyTenTICKS() {

    }

    override fun everySEC() {

    }

    override fun everyFiveSEC() {

    }

    override fun everyMIN() {

    }

    override fun openGUI(guiScreen: GuiScreen?) {

    }

    override fun onConfigChanged() {

    }

    companion object {

        //######################################################################################################################
        // [SETTING] the friendlist separation message
        private val SEPARATION_MESSAGE = "-----------------------------------------------------"
        // [SETTING] the friendlist headline message
        private val FRIENDS_LIST_START = "Friends (Page "
        // [SETTING] the friendlist offline message
        private val IS_CURRENTLY_OFFLINE = " is currently offline"
        // [SETTING] time between every request(in ms)
        private val REQUEST_DELAY = 5000
    }
}