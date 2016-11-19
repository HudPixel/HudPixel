package net.hypixel.api.util

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*
import java.util.function.Function
import java.util.regex.Pattern

object APIUtil {

    private val dashPattern = Pattern.compile("-")
    val UUID_STRIPPER = Function<Any, String>{ value -> APIUtil.stripDashes(value as UUID) }
    private val uuidPattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})")

    fun stripDashes(inputUuid: UUID): String {
        return dashPattern.matcher(inputUuid.toString()).replaceAll("")
    }

    fun withDashes(stripped: String): UUID {
        return UUID.fromString(uuidPattern.matcher(stripped).replaceAll("$1-$2-$3-$4-$5"))
    }

    fun getDateTime(timeStamp: Long): DateTime {
        return DateTime(timeStamp, DateTimeZone.forID("America/New_York"))
    }
}
