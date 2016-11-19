package net.hypixel.api.reply

import net.hypixel.api.request.RequestType

@SuppressWarnings("unused")
class FindGuildReply : AbstractReply() {
    /**
     * @return The ID of the guild that was found, or null if there was no guild by that name
     */
    val guild: String? = null

    override val requestType: RequestType
        get() = RequestType.FIND_GUILD

    override fun toString(): String {
        return "FindGuildReply{" +
                "guild=" + guild +
                ", super=" + super.toString() + "}"
    }
}
