package eladkay.hudpixel.util.autotip.util

import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import java.net.SocketAddress

object UniversalUtil {


    fun getRemoteAddress(event: ClientConnectedToServerEvent): SocketAddress {
        return event.manager.remoteAddress
    }

    fun getUnformattedText(event: ClientChatReceivedEvent): String {
        return event.message.unformattedText
    }

    internal fun chatMessage(text: String) {
        chatMessage(createComponent(text))
    }

    internal fun chatMessage(text: String, url: String, hoverText: String) {
        chatMessage(createComponent(text, url, hoverText))
    }

    private fun chatMessage(component: ChatComponentText) {
        val thePlayer = Minecraft.getMinecraft().thePlayer
        thePlayer.addChatMessage(component)
    }

    private fun createComponent(text: String): ChatComponentText {
        return ChatComponentText(text)
    }

    // Don't try this at home.
    private fun createComponent(text: String, url: String, hoverText: String): ChatComponentText {
        val comp = ChatComponentText(text)
        comp.chatStyle = ChatStyle().setChatHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText(hoverText))).setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, url))
        return comp
    }

}
