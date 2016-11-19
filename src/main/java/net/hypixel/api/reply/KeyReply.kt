package net.hypixel.api.reply

import net.hypixel.api.request.RequestType

import java.util.UUID

@SuppressWarnings("unused")
class KeyReply : AbstractReply() {
    val record: Key? = null

    override val requestType: RequestType
        get() = RequestType.KEY

    override fun toString(): String {
        return "KeyReply{" +
                "record=" + record +
                ", super=" + super.toString() + "}"
    }

    inner class Key {
        val key: UUID? = null
        val ownerUuid: UUID? = null
        val totalQueries: Int = 0
        val queriesInPastMin: Int = 0

        override fun toString(): String {
            return "Key{key=$key, ownerUuid=$ownerUuid, totalQueries=$totalQueries, queriesInPastMin=$queriesInPastMin}"
        }
    }
}
