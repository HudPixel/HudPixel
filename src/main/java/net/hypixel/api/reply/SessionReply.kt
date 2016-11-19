package net.hypixel.api.reply

import net.hypixel.api.request.RequestType
import net.hypixel.api.util.GameType
import java.util.UUID


@SuppressWarnings("unused")
class SessionReply : AbstractReply() {
    /**
     * Session can be null if
     * 1) The player is in a lobby or offline
     * 2) The player has a staff rank

     * @return The session, or null if either of above reasons is met
     */
    val session: Session? = null

    override val requestType: RequestType
        get() = RequestType.SESSION

    override fun toString(): String {
        return "SessionReply{" +
                "session=" + session +
                ",super=" + super.toString() + "}"
    }

    inner class Session {
        /**
         * GameType could be null if a new game has been released
         * and GameType is not yet added to [GameType].
         *
         *
         * This will NOT throw an exception.
         */
        val gameType: GameType? = null
        /**
         * Server name for session
         */
        val server: String? = null
        /**
         * Set of UUIDs of players currently in this session
         */
        val players: Set<UUID>? = null

        override fun toString(): String {
            return "Session{gameType=$gameType, server='$server', players=$players}"
        }
    }
}
