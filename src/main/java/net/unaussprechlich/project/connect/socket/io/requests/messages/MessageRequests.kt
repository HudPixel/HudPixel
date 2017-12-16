
/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.socket.io.requests.messages

import net.unaussprechlich.project.connect.socket.io.EnumSocketEvents
import net.unaussprechlich.project.connect.socket.io.requests.AbstractRequest
import org.json.JSONObject


class LoadMessageRequest (ack : ((responseObject: Message , success : Boolean) -> (Unit))?) : AbstractRequest({
    args : JSONObject, success : Boolean -> ack?.invoke(Message(args), success) }) {

    var CHAT_ID  = ""
    var MESSAGE_ID = ""

    override fun assembleArgs(obj: JSONObject): JSONObject {
        obj.put("CHAT_ID", CHAT_ID)
        obj.put("MESSAGE_ID", MESSAGE_ID)
        return obj
    }

    override fun getEventName(): EnumSocketEvents {
        //TODO
        return EnumSocketEvents.NULL
    }
}

class SendMessageRequest (ack : ((success : Boolean) -> (Unit))?) : AbstractRequest({
    _, success : Boolean -> ack?.invoke(success) }) {

    var CHAT_ID  = ""
    var MESSAGE = ""

    override fun assembleArgs(obj: JSONObject): JSONObject {
        obj.put("CHAT_ID", CHAT_ID)
        obj.put("MESSAGE", MESSAGE)
        return obj
    }

    override fun getEventName(): EnumSocketEvents {
        return EnumSocketEvents.MESSAGE
    }
}

class Message(args : JSONObject){

    val CHAT_ID = args.getString("CHAT_ID")!!
    val MESSAGE_ID = args.getString("MESSAGE_ID")!!
    val TIME = args.getString("TIME")!!
    val SENDER = args.getString("SENDER")!!
    val MESSAGE = args.getString("MESSAGE")!!

}