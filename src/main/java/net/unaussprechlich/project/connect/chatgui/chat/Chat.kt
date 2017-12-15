/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.chatgui.chat

import net.unaussprechlich.managedgui.lib.container.register
import net.unaussprechlich.managedgui.lib.container.unregister
import net.unaussprechlich.managedgui.lib.helper.DateHelper
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefChatMessageContainer
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.project.connect.chatgui.ChatController
import net.unaussprechlich.project.connect.chatgui.ChatWrapper
import net.unaussprechlich.project.connect.chatgui.list.ChatListCategoryContainer
import net.unaussprechlich.project.connect.chatgui.list.ChatListContainer
import net.unaussprechlich.project.connect.chatgui.list.ChatListElementContainer
import net.unaussprechlich.project.connect.socket.io.requests.messages.SendMessageRequest
import net.unaussprechlich.project.connect.socket.io.subscriptions.ChatMessageSubscribtion
import net.unaussprechlich.project.connect.socket.io.subscriptions.subscribe
import java.util.*

data class Message(val _id : Long, val name : String, val msg : String, val time : DateHelper){

    override fun toString(): String {
        return "$_id§$name§$msg§" + time.date.toString()
    }

    fun toContainer() : (DefChatMessageContainer){
        return DefChatMessageContainer(name, msg, time)
    }
}

class Chat(val _id : UUID, val name : String, val color : ColorRGBA, val chatListCategoryContainer: ChatListCategoryContainer? = null) {

    val chatListElement = ChatListElementContainer()

    val chatTabContainer = TabContainer(ChatWrapper.width - ChatWrapper.stdChatListWidth, ChatWrapper.height - ChatWrapper.controllerCon.height,
            sizeCallback = { ChatWrapper.updateResizeIconPosition() },
            sendCallback = { queueMessage(it) }
    ).apply {
        xOffset = ChatWrapper.stdChatListWidth
        yOffset = ChatWrapper.controllerCon.height
        isVisible = false
    }

    private val messageBuffer = mapOf<Long, Message>()
    private val containerBuffer = mapOf<Long, DefChatMessageContainer>()

    private data class UnprocessedMessage(val message : String, var failed : Boolean? = null)
    private val unprocessedMessages : ArrayList<UnprocessedMessage> = arrayListOf()
    private var isBlocked = false

    fun getLatestCon() : DefChatMessageContainer? =
        containerBuffer[containerBuffer.keys.max()]


    fun doesLatestConSenderMatchSender(sender : String) : Boolean =
        getLatestCon()?.playerName.equals(sender)

    init {
        chatListElement.color = color
        chatListElement.title = name
        chatListElement.activateCallback.registerListener { ChatController.setActive(this) }

        ChatController.registerChat(this)

        if(chatListCategoryContainer != null)
            ChatListContainer.addChatListElement(chatListElement, chatListCategoryContainer)
        else
            ChatListContainer.addChatListElement(chatListElement)

        ChatMessageSubscribtion subscribe fun(channelID : UUID, msg : Message){

            if (channelID != _id) return

            messageBuffer[msg._id] to msg
            if(getLatestCon() != null && doesLatestConSenderMatchSender(msg.name) && getLatestCon()?.date!!.minutesPassed < 5){
                containerBuffer[msg._id] to getLatestCon()!!
                getLatestCon()?.addMessage(msg.msg)
            } else {
                msg.toContainer().let {
                    containerBuffer[msg._id] to it
                    chatTabContainer.scrollCon register it
                }
            }
        }
    }

    var isActive = false
        set(value) {
            println("activated:$value")
            field = value
            chatTabContainer.isVisible = isActive
            chatListElement.isActive = isActive
            if(value)ChatWrapper register chatTabContainer
            else     ChatWrapper unregister chatTabContainer

        }

    private fun queueMessage(msg : String){
        UnprocessedMessage(msg).let {
            unprocessedMessages.add(it)
            sendMessage(it)
        }
    }

    private fun sendMessage(msg : UnprocessedMessage){
        isBlocked = true
        SendMessageRequest{ success ->
            if(success) unprocessedMessages.remove(msg)
            else        msg.failed = true

            isBlocked = false
        }.apply {
            CHAT_ID = _id.toString()
            MESSAGE = msg.message
        }
    }
}