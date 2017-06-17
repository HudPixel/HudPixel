package eladkay.hudpixel.util.autotip.util

import eladkay.hudpixel.util.ChatMessageComposer
import net.minecraft.util.EnumChatFormatting

// Simple aliases for ChatMessageComposer
object ClientMessage {

    fun send(msg: String) {
        ChatMessageComposer(msg).send()
    }

    fun send(msg: String, url: String, hoverText: String) {
        ChatMessageComposer(msg).makeLink(url).makeHover(ChatMessageComposer(hoverText)).send()
    }

    fun sendRaw(msg: String) {
        ChatMessageComposer(msg).send(false)
    }

    fun separator() {
        ChatMessageComposer.printSeparationMessage(EnumChatFormatting.GOLD)
    }

}
