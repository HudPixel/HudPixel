/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.chatgui.chat

import net.minecraft.client.Minecraft
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

open class Chat(val name : String, val color : ColorRGBA, val chatListCategoryContainer: ChatListCategoryContainer? = null, val sendCallback :  (msg : String) -> Unit) {

    val chatListElement = ChatListElementContainer()

    val chatTabContainer = TabContainer(ChatWrapper.width - ChatWrapper.stdChatListWidth, ChatWrapper.height - ChatWrapper.controllerCon.height,
            sizeCallback = { ChatWrapper.updateResizeIconPosition() },
            sendCallback = sendCallback
    ).apply {
        xOffset = ChatWrapper.stdChatListWidth
        yOffset = ChatWrapper.controllerCon.height
        isVisible = false
    }

    protected fun addMessage(msg : Message) {
        currentID = msg._id
        messageBuffer[msg._id] to msg
        if (getLatestCon() != null && doesLatestConSenderMatchSender(msg.name) && getLatestCon()?.date!!.minutesPassed < 5) {
            containerBuffer[msg._id] to getLatestCon()!!
            getLatestCon()?.addMessage(msg.msg)
        } else {
            msg.toContainer().let {
                containerBuffer[msg._id] to it
                chatTabContainer.scrollCon.registerScrollElement(it)
            }
        }
    }

    private var currentID = 0L

    protected fun getNextID() : Long{
        return currentID + 1
    }

    private val messageBuffer = mapOf<Long, Message>()
    private val containerBuffer = mapOf<Long, DefChatMessageContainer>()


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
            addMessage(msg)
        }
    }

    var isActive = false
        set(value) {
            println("activated:$value")
            field = value
            chatTabContainer.isVisible = isActive
            chatListElement.isActive = isActive
            if(value)ChatWrapper register   chatTabContainer
            else     ChatWrapper unregister chatTabContainer
        }

}