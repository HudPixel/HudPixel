package net.hypixel.api.adapters

import com.google.gson.*
import net.hypixel.api.util.GameType

import java.lang.reflect.Type

/**
 * We need this adapter because we note GameTypes
 * as both the id and as it's enum name
 */
class GameTypeTypeAdapter : JsonDeserializer<GameType>, JsonSerializer<GameType> {

    override fun serialize(src: GameType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): GameType? {
        val raw = json.asString
        try {
            val intType = Integer.parseInt(raw)
            return GameType.fromId(intType)
        } catch (ignored: NumberFormatException) {
        }

        return GameType.valueOf(raw)
    }

}
