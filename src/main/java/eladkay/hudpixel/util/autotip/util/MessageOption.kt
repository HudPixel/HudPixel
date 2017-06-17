package eladkay.hudpixel.util.autotip.util

import net.minecraft.util.EnumChatFormatting
import org.apache.commons.lang3.StringUtils

// Handles message displaying options.
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
        var color = EnumChatFormatting.GREEN
        when (this) {
            COMPACT -> color = EnumChatFormatting.YELLOW
            HIDDEN -> color = EnumChatFormatting.RED
            SHOWN -> color = EnumChatFormatting.GREEN
        }
        return color.toString() + StringUtils.capitalize(this.name.toLowerCase())
    }
}