package net.hypixel.api.reply

import net.hypixel.api.request.RequestType
import net.hypixel.api.util.GameType
import java.util.UUID
import org.joda.time.DateTime

@SuppressWarnings("unused")
class BoostersReply : AbstractReply() {
    val boosters: List<Booster>? = null

    override val requestType: RequestType
        get() = RequestType.BOOSTERS

    override fun toString(): String {
        return "BoostersReply{" +
                "boosters=" + boosters +
                ", super=" + super.toString() + "}"
    }

    inner class Booster {
        val purchaserUuid: UUID? = null
        val amount: Int = 0
        val originalLength: Int = 0
        val length: Int = 0
        val gameType: GameType? = null
        val dateActivated: DateTime? = null

        override fun toString(): String {
            return "Booster{purchaserUuid=$purchaserUuid, amount=$amount, originalLength=$originalLength, length=$length, gameType=$gameType, dateActivated=$dateActivated}"
        }
    }
}
