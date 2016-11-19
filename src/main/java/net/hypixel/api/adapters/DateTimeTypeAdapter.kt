package net.hypixel.api.adapters

import com.google.gson.*
import net.hypixel.api.util.APIUtil
import org.joda.time.DateTime

import java.lang.reflect.Type

/**
 * Our dates are always saved as a timestamp
 * if we diverge from that path we can adapt
 * it in here as well by just using some more
 * parsing.
 */
class DateTimeTypeAdapter : JsonDeserializer<DateTime>, JsonSerializer<DateTime> {

    override fun serialize(src: DateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.millis)
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DateTime {
        return APIUtil.getDateTime(java.lang.Long.parseLong(json.asString))
    }

}
