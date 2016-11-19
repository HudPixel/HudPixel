package net.hypixel.api.adapters

import com.google.gson.*

import java.lang.reflect.Type
import java.util.UUID

class UUIDTypeAdapter : JsonDeserializer<UUID>, JsonSerializer<UUID> {

    override fun serialize(src: UUID, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UUID {
        val uuid = json.asString
        if (uuid.contains("-")) {
            return UUID.fromString(uuid)
        } else {
            return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32))
        }
    }

}
