package net.hypixel.api.reply

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.hypixel.api.request.RequestType

@SuppressWarnings("unused")
class PlayerReply : AbstractReply() {
    val player: JsonElement? = null

    fun getPlayer(): JsonObject? {
        if (player == null || player.isJsonNull) {
            return null
        } else {
            return player.asJsonObject
        }
    }

    override val requestType: RequestType
        get() = RequestType.PLAYER

    override fun toString(): String {
        return "PlayerReply{" +
                "player=" + player +
                ", super=" + super.toString() + "}"
    }
}
