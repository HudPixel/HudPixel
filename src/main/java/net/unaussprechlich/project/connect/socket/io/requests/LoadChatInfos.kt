/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.socket.io.requests

import net.unaussprechlich.project.connect.socket.io.EnumSocketEvents
import org.json.JSONObject


class LoadChatInfos (ack : ((args : JSONObject, success : Boolean) -> (Unit))?) : AbstractRequest(ack) {


    override fun assembleArgs(obj: JSONObject): JSONObject {
        return obj
    }

    override fun getEventName(): EnumSocketEvents {
        return EnumSocketEvents.CHATEVENT
    }


}