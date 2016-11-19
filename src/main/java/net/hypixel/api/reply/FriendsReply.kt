package net.hypixel.api.reply

import net.hypixel.api.request.RequestType
import org.joda.time.DateTime
import java.util.UUID

@SuppressWarnings("unused")
class FriendsReply : AbstractReply() {
    val friendShips: List<FriendShip>? = null

    override val requestType: RequestType
        get() = RequestType.FRIENDS

    override fun toString(): String {
        return "FriendsReply{" +
                "friendShips=" + friendShips +
                ", super=" + super.toString() + "}"
    }

    inner class FriendShip {

        val uuidSender: UUID? = null
        val uuidReceiver: UUID? = null
        val started: DateTime? = null

        override fun toString(): String {
            return "FriendShip{uuidSender=$uuidSender, uuidReceiver=$uuidReceiver, started=$started}"
        }
    }
}
