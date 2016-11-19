package net.hypixel.api.adapters

import com.google.gson.*
import net.hypixel.api.reply.GuildReply
import net.hypixel.api.util.APIUtil

import java.lang.reflect.Type

class GuildCoinHistoryAdapter : JsonDeserializer<GuildReply.Guild.GuildCoinHistory>, JsonSerializer<GuildReply.Guild.GuildCoinHistory> {

    override fun serialize(src: GuildReply.Guild.GuildCoinHistory, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): GuildReply.Guild.GuildCoinHistory {
        val history = GuildReply.Guild.GuildCoinHistory()
        json.asJsonObject.entrySet().forEach { entry -> history.coinHistory.put(APIUtil.getDateTime(java.lang.Long.parseLong(entry.key)), entry.value.asInt) }
        return history
    }

}
