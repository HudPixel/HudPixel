package eladkay.hudpixel.util

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Created by Elad on 6/17/2017.
 * Ain't no party like a Kotlin party, 'cause a Kotlin party don't stnoop.
 */
object PartyUtil {
    const val notInParty = "You must be in a party to use this command!"
    val partyMembers = "Party members (\\d+): (.*)".toRegex()
    private var needsUpdate = false
    val membersList = mutableListOf<String>()
    var owner: String? = null
        private set
    fun disband() = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p disband")
    fun invite(string: String) = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p invite $string")
    fun leave() = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p leave")
    fun promote(string: String) = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p promote $string")
    fun warpToHousing() = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p home")
    fun remove(string: String) = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p remove $string")
    fun warp() = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p warp")
    fun accept(string: String) = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p accept $string")
    fun kickOffline() = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p kickoffline")
    fun toggleAllInvite() = Minecraft.getMinecraft().thePlayer.sendChatMessage("/p settings allinvite")
    private var next = 0
    @SubscribeEvent
    fun chat(clientChatReceivedEvent: ClientChatReceivedEvent) {
        val msg = clientChatReceivedEvent.message.unformattedText
        if(next > 0) {
            clientChatReceivedEvent.isCanceled = true
            next--
        } else if((/*"-----------------------------------------------------" in msg || */notInParty in msg || partyMembers.matches(msg))) {
            next = if(needsUpdate && partyMembers.matches(msg)) 2 else next
            clientChatReceivedEvent.isCanceled = needsUpdate
            needsUpdate = false
            ChatMessageComposer(msg).send()
            if(partyMembers.matches(msg)) {
                val members = partyMembers.matchEntire(msg)?.groups?.get(1)?.value
                if(members != null) {
                    ChatMessageComposer(members).send()
                    membersList.apply {
                        clear()
                        addAll(members.split(", "))
                    }
                    owner = membersList[0]
                    ChatMessageComposer(membersList.toString()).send() // println(membersList)
                    isInParty = true
                } else if(notInParty in msg) {
                    membersList.clear()
                    owner = null
                    isInParty = false
                }
            } else if(notInParty in msg) {
                membersList.clear()
                owner = null
                isInParty = false
            }
        } else if ("-----------------------------------------------------" in msg) {
            needsUpdate = true
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/p list")
        }
    }
    var isInParty: Boolean = false
        private set
}