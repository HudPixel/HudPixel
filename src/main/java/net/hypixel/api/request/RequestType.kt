package net.hypixel.api.request

import net.hypixel.api.reply.*

@SuppressWarnings("unused")
enum class RequestType private constructor(val key: String, val replyClass: Class<out AbstractReply>) {

    PLAYER("player", PlayerReply::class.java),
    FIND_GUILD("findGuild", FindGuildReply::class.java),
    GUILD("guild", GuildReply::class.java),
    FRIENDS("friends", FriendsReply::class.java),
    SESSION("session", SessionReply::class.java),
    KEY("key", KeyReply::class.java),
    BOOSTERS("boosters", BoostersReply::class.java)
}
