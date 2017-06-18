package net.unaussprechlich.project.connect.chat

import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefChatMessageContainer
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import java.util.*


class Chat(val _id : UUID, val name : String, val color : ColorRGBA) {

    val chatListElement = newChatListElementContainer()

    var latestIndex = 0L
        private set

    private val messageBuffer : HashMap<Long, Message> = hashMapOf()

    init {
        chatListElement.color = color
        chatListElement.title = name
        chatListElement.activateCallback.registerListener { openChat() }

        newChatListContainer.addChatListElement(chatListElement)
    }

    fun openChat(){
        newChatActualChatContainer.loadChat(this)
    }

    fun getMessageAsCon(index : Long) : DefChatMessageContainer{
        if(messageBuffer.contains(index))
            return messageBuffer[index]!!.toContainer()
        else
            throw Exception("[Chat] chat with $_id does not contain index:$index!")
    }

    fun addMessage(msg : Message){
        latestIndex++
        messageBuffer.put(latestIndex, msg)
    }

}