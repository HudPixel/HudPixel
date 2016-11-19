package net.hypixel.api.request

import net.hypixel.api.util.APIUtil
import java.util.*
import java.util.function.Function

@SuppressWarnings("unused")
enum class RequestParam private constructor(val requestType: RequestType, val queryField: String, valueClass: Class<*>, private val valueSerializer: Function<Any, String>? = null) {

    KEY(RequestType.KEY, "key", String::class.java),

    PLAYER_BY_NAME(RequestType.PLAYER, "name", String::class.java),
    PLAYER_BY_UUID(RequestType.PLAYER, "uuid", UUID::class.java, APIUtil.UUID_STRIPPER),

    GUILD_BY_NAME(RequestType.FIND_GUILD, "byName", String::class.java),
    GUILD_BY_PLAYER_UUID(RequestType.FIND_GUILD, "byUuid", UUID::class.java, APIUtil.UUID_STRIPPER),
    GUILD_BY_ID(RequestType.GUILD, "id", String::class.java),

    FRIENDS_BY_UUID(RequestType.FRIENDS, "uuid", UUID::class.java, APIUtil.UUID_STRIPPER),

    SESSION_BY_UUID(RequestType.SESSION, "uuid", UUID::class.java, APIUtil.UUID_STRIPPER);

    val valueClass: Class<*>

    init {
        this.valueClass = valueClass
    }

    fun serialize(value: Any): String {
        return if (valueSerializer == null) value as String else valueSerializer.apply(value)
    }

    companion object {

        private val v = values()
    }
}
