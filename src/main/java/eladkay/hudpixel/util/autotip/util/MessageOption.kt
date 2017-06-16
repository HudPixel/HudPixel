package eladkay.hudpixel.util.autotip.util

import org.apache.commons.lang3.StringUtils

enum class MessageOption {
    SHOWN, COMPACT, HIDDEN;

    operator fun next(): MessageOption {
        when (this) {
            SHOWN -> return COMPACT
            COMPACT -> return HIDDEN
            HIDDEN -> return SHOWN
            else -> return SHOWN
        }
    }

    override fun toString(): String {
        var color = ChatColor.GREEN
        when (this) {
            COMPACT -> color = ChatColor.YELLOW
            HIDDEN -> color = ChatColor.RED
        }
        return color.toString() + StringUtils.capitalize(this.name.toLowerCase())
    }
}