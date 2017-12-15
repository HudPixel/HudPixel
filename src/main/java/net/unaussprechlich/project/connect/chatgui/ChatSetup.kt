/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.chatgui

import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.project.connect.chatgui.chat.Chat
import net.unaussprechlich.project.connect.chatgui.list.ChatListCategoryContainer
import net.unaussprechlich.project.connect.chatgui.list.ChatListContainer
import java.util.*


object ChatSetup {

    val partyChat = Chat(UUID.randomUUID(), "PartyChat", RGBA.BLUE.get())
    val guildChat = Chat(UUID.randomUUID(), "GuildChat", RGBA.GREEN.get())

    val friendsCate     = ChatListCategoryContainer("Friends", RGBA.PURPLE_LIGHT_MC.get())
    val groupsCate      = ChatListCategoryContainer("Groups", RGBA.YELLOW_MC.get())
    val gamemodesCate   = ChatListCategoryContainer("Gamemodes", RGBA.RED.get())
    val favoriteCate    = ChatListCategoryContainer("Favorite *", RGBA.GREEN.get())

    init {
        partyChat
        guildChat
        ChatListContainer.registerScrollElement(favoriteCate)
        ChatListContainer.registerScrollElement(groupsCate)
        ChatListContainer.registerScrollElement(friendsCate)
        ChatListContainer.registerScrollElement(gamemodesCate)
    }


}