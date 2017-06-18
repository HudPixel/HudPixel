package net.unaussprechlich.project.connect.chat

import net.unaussprechlich.managedgui.lib.databases.player.PlayerDatabaseMG
import net.unaussprechlich.managedgui.lib.helper.DateHelper
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefChatMessageContainer

data class Message(val name : String, val msg : String, val time : DateHelper){
    override fun toString(): String {
        return "$name§$msg§" + time.date.toString()
    }

    fun toContainer() : (DefChatMessageContainer){
        return DefChatMessageContainer(PlayerDatabaseMG.get(name), msg, time)
    }
}

/**
 * MessageBuffer Created by Alexander on 06.05.2017.
 * Description:
 */
class MessageBuffer {

    private val messages = arrayOfNulls<CharArray>(100)

    fun addMessage(message: Message) {
        try {
            if (messages.size > 100) throw IndexOutOfBoundsException("[MessageBuffer] yeah, we got a buffer overflow here :(")
            messages[messages.size] = message.toString().toCharArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getEncodedMessages(): Array<Message?>{
        val result = arrayOfNulls<Message>(messages.size)
        for(i in 0 .. messages.size){
            result[i] = getEncodedMessage(i)
        }
        return result
    }

    fun getEncodedMessage(index: Int): Message? {
        try {
            if (index > messages.size)
                throw IndexOutOfBoundsException("[MessageBuffer] the buffer does not contain you Index :[")
            val msg = String(messages[index] as CharArray).split("§".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return Message(msg[0], msg[1], DateHelper(msg[2]))
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            return null
        }
    }
}
