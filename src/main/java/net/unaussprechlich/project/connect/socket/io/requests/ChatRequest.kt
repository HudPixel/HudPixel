package net.unaussprechlich.project.connect.socket.io.requests

import net.unaussprechlich.project.connect.socket.io.EnumSocketEvents
import org.json.JSONObject


class ChatRequest (ack : ((args : JSONObject, success : Boolean) -> (Unit))?) : AbstractRequest(ack) {

    var SESSION_TOKEN = ""
    var CHAT_ID  = ""

    override fun assembleArgs(obj: JSONObject): JSONObject {
        obj.put("SESSION_TOKEN", SESSION_TOKEN)
        obj.put("CHAT_ID", CHAT_ID)
        return obj
    }

    override fun getEventName(): EnumSocketEvents {
        return EnumSocketEvents.CHATEVENT
    }


}