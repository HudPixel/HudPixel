package eladkay.hudpixel.util.autotip.util

object TimeUtil {

    private val ONE_SECOND: Long = 1000
    private val ONE_MINUTE = ONE_SECOND * 60
    private val ONE_HOUR = ONE_MINUTE * 60
    private val ONE_DAY = ONE_HOUR * 24

    fun formatMillis(duration: Long): String {
        val sb = StringBuilder()
        var temp: Long
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY
            if (temp > 0) {
                sb.append(temp).append(" day").append(if (temp > 1) "s" else "")
                return sb.toString() + " ago"
            }

            temp = duration / ONE_HOUR
            if (temp > 0) {
                sb.append(temp).append(" hour").append(if (temp > 1) "s" else "")
                return sb.toString() + " ago"
            }

            temp = duration / ONE_MINUTE
            if (temp > 0) {
                sb.append(temp).append(" minute").append(if (temp > 1) "s" else "")
                return sb.toString() + " ago"
            }

            temp = duration / ONE_SECOND
            if (temp > 0) {
                sb.append(temp).append(" second").append(if (temp > 1) "s" else "")
            }
            return sb.toString() + " ago"
        } else {
            return "just now"
        }
    }
}
