/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.socket.io.requests

import net.unaussprechlich.project.connect.socket.io.EnumSocketEvents
import org.json.JSONObject



class ChatRequest (ack : ((responseObject: ChatResponseObject , success : Boolean) -> (Unit))?) : AbstractRequest({
    args : JSONObject, success : Boolean -> ack?.invoke(ChatResponseObject(args), success) }) {

    var CHAT_ID  = ""

    override fun assembleArgs(obj: JSONObject): JSONObject {
        obj.put("CHAT_ID", CHAT_ID)
        return obj
    }

    override fun getEventName(): EnumSocketEvents {
        return EnumSocketEvents.CHATEVENT
    }
}

class ChatResponseObject(args : JSONObject){

    val CHAT_ID = args.getString("CHAT_ID")!!
    val CREATED_AT = args.getString("CREATED_AT")!!
    val CREATED_BY = args.getString("CREATED_BY")!!
    val CHAT_TYPE = args.getInt("IS_PRIVATE")

}