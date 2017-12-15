
/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.chatgui

import net.unaussprechlich.project.connect.chatgui.chat.Chat
import java.util.*

object ChatController {

    private val chats : ArrayList<Chat> = arrayListOf()
    var currentChat : Chat? = null

    fun resize(){
        chats.map { it.chatTabContainer }.forEach{
            it.width = ChatWrapper.getChatWidth()
            it.height = ChatWrapper.getChatHeight()
            it.update()
        }
    }

    fun getChatInputFieldHeight() : Int{
        if(currentChat == null) return 0
        else return currentChat!!.chatTabContainer.chatInputField.height
    }

    fun setActive(chat: Chat){
        currentChat?.isActive = false
        currentChat = chat
        currentChat?.isActive = true
    }

    infix fun registerChat(chat : Chat){
        chats.add(chat)
    }

    infix fun unregisterChat(chat : Chat){
        chats.remove(chat)
    }

}