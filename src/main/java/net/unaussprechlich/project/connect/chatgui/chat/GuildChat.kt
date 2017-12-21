/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.chatgui.chat

import eladkay.hudpixel.ChatDetector
import net.minecraft.client.Minecraft
import net.unaussprechlich.managedgui.lib.helper.DateHelper
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.project.connect.chatgui.list.ChatListCategoryContainer
import java.util.*


object GuildChat : Chat("Guild", RGBA.GREEN_DARK_MC.get(), sendCallback = {
    Minecraft.getMinecraft().thePlayer.sendChatMessage("/gchat " + it)
}) {

    init {
        ChatDetector.registerEventHandler(ChatDetector.GuildChat, { event ->
            addMessage(
                    Message(
                            getNextID(),
                            event.data["name"]!! ,
                            event.data["message"]!! ,
                            DateHelper())
            )
        })
    }
}