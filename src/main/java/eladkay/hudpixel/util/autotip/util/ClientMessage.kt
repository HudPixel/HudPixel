package eladkay.hudpixel.util.autotip.util

object ClientMessage {

    private val prefix =
            ChatColor.GOLD.toString() + "A" + ChatColor.YELLOW + "T" + ChatColor.DARK_GRAY + " > "+ ChatColor.GRAY

    fun send(msg: String) {
        UniversalUtil.chatMessage(prefix + msg)
    }

    fun send(msg: String, url: String, hoverText: String) {
        UniversalUtil.chatMessage(prefix + msg, url, hoverText)
    }

    fun sendRaw(msg: String) {
        UniversalUtil.chatMessage(msg)
    }

    fun separator() {
        UniversalUtil.chatMessage(
                ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "----------------------------------")
    }

}
