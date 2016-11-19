package net.hypixel.api.reply

import net.hypixel.api.request.RequestType

@SuppressWarnings("unused")
abstract class AbstractReply {

    var isThrottle: Boolean = false
        protected set
    var isSuccess: Boolean = false
        protected set
    var cause: String? = null
        protected set

    abstract val requestType: RequestType

    override fun toString(): String {
        return "AbstractReply{throttle=$isThrottle, success=$isSuccess, cause='$cause'}"
    }
}
