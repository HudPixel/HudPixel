package net.hypixel.api.util

@SuppressWarnings("unused")
enum class GameType private constructor(
        /**
         * @return The official name of the GameType
         */
        val nm: String, private val dbName: String, val id: Int?) {
    QUAKECRAFT("Quakecraft", "Quake", 2),
    WALLS("Walls", "Walls", 3),
    PAINTBALL("Paintball", "Paintball", 4),
    SURVIVAL_GAMES("Blitz Survival Games", "HungerGames", 5),
    TNTGAMES("The TNT Games", "TNTGames", 6),
    VAMPIREZ("VampireZ", "VampireZ", 7),
    WALLS3("Mega Walls", "Walls3", 13),
    ARCADE("Arcade", "Arcade", 14),
    ARENA("Arena Brawl", "Arena", 17),
    MCGO("Cops and Crims", "MCGO", 21),
    UHC("UHC Champions", "UHC", 20),
    BATTLEGROUND("Warlords", "Battleground", 23),
    SUPER_SMASH("Smash Heroes", "SuperSmash", 24),
    GINGERBREAD("Turbo Kart Racers", "GingerBread", 25),
    HOUSING("Housing", "Housing", 26),
    SKYWARS("SkyWars", "SkyWars", 51),
    TRUE_COMBAT("Crazy Walls", "TrueCombat", 52),
    SPEED_UHC("Speed UHC", "SpeedUHC", 54),
    SKYCLASH("SkyClash", "SkyClash", 55);

    /**
     * @return The internal ID that is occasionally used in various database schemas
     */
    fun getId(): Int {
        return id!!
    }

    companion object {

        private val v = values()

        /**
         * @param id The internal id
         * *
         * @return The GameType associated with that id, or null if there isn't one.
         */
        fun fromId(id: Int): GameType? {
            for (gameType in v) {
                if (gameType.id === id) {
                    return gameType
                }
            }
            return null
        }

        /**
         * @param dbName The key used in the database
         * *
         * @return The GameType associated with that key, or null if there isn't one.
         */
        fun fromDatabase(dbName: String): GameType? {
            for (gameType in v) {
                if (gameType.dbName == dbName) {
                    return gameType
                }
            }
            return null
        }
    }
}
