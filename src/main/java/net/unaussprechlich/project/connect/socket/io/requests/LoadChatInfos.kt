package net.unaussprechlich.project.connect.socket.io.requests

import net.unaussprechlich.project.connect.socket.io.EnumSocketEvents
import org.json.JSONObject


class LoadChatInfos (ack : ((args : JSONObject, success : Boolean) -> (Unit))?) : AbstractRequest(ack) {

    var SESSION_TOKEN = ""

    override fun assembleArgs(obj: JSONObject): JSONObject {
        obj.put("SESSION_TOKEN", SESSION_TOKEN)
        return obj
    }

    override fun getEventName(): EnumSocketEvents {
        return EnumSocketEvents.CHATEVENT
    }


}