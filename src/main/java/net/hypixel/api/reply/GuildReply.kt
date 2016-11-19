package net.hypixel.api.reply

import com.google.common.collect.Maps
import net.hypixel.api.request.RequestType
import net.hypixel.api.util.Banner
import org.joda.time.DateTime
import java.util.*

@SuppressWarnings("unused")
class GuildReply : AbstractReply() {
    val guild: Guild? = null

    override val requestType: RequestType
        get() = RequestType.GUILD

    override fun toString(): String {
        return "GuildReply{" +
                "guild=" + guild +
                ", super=" + super.toString() + "}"
    }

    /**
     * Mainly used to identify classes
     */
    interface GuildCoinHistoryHolding {
        val guildCoinHistory: Guild.GuildCoinHistory?
    }

    class Guild : GuildCoinHistoryHolding {
        val _id: String? = null

        val name: String? = null
        val tag: String? = null
        val publiclyListed: Boolean? = null
        val banner: Banner? = null
        val members: List<Member>? = null
        override val guildCoinHistory: GuildCoinHistory? = null
        val coins: Int = 0
        val coinsEver: Int = 0
        val created: DateTime? = null
        val joinable: Boolean? = null
        val memberSizeLevel: Int = 0
        val bankSizeLevel: Int = 0
        val canTag: Boolean? = null
        val canParty: Boolean? = null
        val canMotd: Boolean? = null

        override fun toString(): String {
            return "Guild{_id='$_id', name='$name', tag='$tag', publiclyListed=$publiclyListed, banner=$banner, members=$members, guildCoinHistory=$guildCoinHistory, coins=$coins, coinsEver=$coinsEver, created=$created, joinable=$joinable, memberSizeLevel=$memberSizeLevel, bankSizeLevel=$bankSizeLevel, canTag=$canTag, canParty=$canParty, canMotd=$canMotd}"
        }

        inner class Member : GuildCoinHistoryHolding {
            val uuid: UUID? = null
            val rank: GuildRank? = null
            val joined: DateTime? = null
            override val guildCoinHistory: GuildCoinHistory? = null

            override fun toString(): String {
                return "Member{uuid=$uuid, rank=$rank, joined=$joined, guildCoinHistory=$guildCoinHistory}"
            }
        }

        class GuildCoinHistory {

            val coinHistory: MutableMap<DateTime, Int> = Maps.newHashMap<DateTime, Int>()

            override fun toString(): String {
                return "GuildCoinHistory{coinHistory=$coinHistory}"
            }
        }

        enum class GuildRank {
            GUILDMASTER, OFFICER, MEMBER
        }
    }
}
