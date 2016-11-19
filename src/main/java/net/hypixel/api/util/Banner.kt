package net.hypixel.api.util

import com.google.gson.annotations.SerializedName

class Banner {

    @SerializedName("Base")
    val base: String? = null
    @SerializedName("Patterns")
    val patterns: List<Pattern>? = null

    override fun toString(): String {
        return "Banner{base='$base', patterns=$patterns}"
    }

    class Pattern {
        @SerializedName("Pattern")
        val pattern: String? = null
        @SerializedName("Color")
        val color: String? = null

        override fun toString(): String {
            return "Pattern{pattern='$pattern', color='$color'}"
        }
    }
}
