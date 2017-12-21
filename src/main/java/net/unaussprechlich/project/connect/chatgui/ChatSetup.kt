/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.chatgui

import eladkay.hudpixel.ChatDetector
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.project.connect.chatgui.chat.Chat
import net.unaussprechlich.project.connect.chatgui.chat.GuildChat
import net.unaussprechlich.project.connect.chatgui.list.ChatListCategoryContainer
import net.unaussprechlich.project.connect.chatgui.list.ChatListContainer
import java.util.*


object ChatSetup {


    val friendsCate     = ChatListCategoryContainer("Friends", RGBA.PURPLE_LIGHT_MC.get())
    val groupsCate      = ChatListCategoryContainer("Groups", RGBA.YELLOW_MC.get())
    val gamemodesCate   = ChatListCategoryContainer("Gamemodes", RGBA.RED.get())

    init {
        GuildChat
        ChatListContainer.registerScrollElement(groupsCate)
        ChatListContainer.registerScrollElement(friendsCate)
        ChatListContainer.registerScrollElement(gamemodesCate)
    }


}