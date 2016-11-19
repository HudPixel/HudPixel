package net.hypixel.api.util.pets

class Pet(private val stats: Map<String, Any>?) {
    var level: Int = 0
        private set
    private var experience: Int = 0
    var name: String? = null
        private set

    init {

        if (stats != null) {
            if (stats["experience"] != null) {
                experience = (stats["experience"] as Number).toInt()
            }
            if (stats["name"] != null) {
                name = stats["name"] as String
            }
        }

        updateLevel()
    }

    val averageHappiness: Double
        get() {
            var attributeAverage = 0.0
            for (attribute in PetAttribute.values()) {
                attributeAverage += getAttribute(attribute).toDouble()
            }

            return attributeAverage / PetAttribute.values().size
        }

    fun getAttribute(attribute: PetAttribute): Int {
        @SuppressWarnings("unchecked")
        val attributeObject = stats?.get(attribute.name) as Map<String, Any>

        val timestampObj = attributeObject["timestamp"]
        val valueObj = attributeObject["value"]
        if (timestampObj !is Number || valueObj !is Number) {
            return 1
        }

        val currentTime = System.currentTimeMillis()
        val timestamp = timestampObj.toLong()
        val value = valueObj.toInt()

        val timeElapsed = currentTime - timestamp
        val minutesPassed = timeElapsed / (1000 * 60)
        val iterations = Math.floor((minutesPassed / 5).toDouble()).toLong()

        return Math.max(0, Math.round((value - iterations * attribute.decay).toFloat()))
    }

    fun updateLevel(): Boolean {
        var xp = experience
        var level = 1
        for (xpLevel in EXPERIENCE_LEVELS) {
            if (xp < xpLevel) {
                break
            } else {
                xp -= xpLevel
                level++
            }
        }
        this.level = level
        return false
    }

    fun getExperienceUntilLevel(level: Int): Int {
        var xp = 0
        for (i in 0..Math.min(level - 1, 100) - 1) {
            xp += EXPERIENCE_LEVELS[i]
        }
        return xp
    }

    val levelProgress: Int
        get() = experience - getExperienceUntilLevel(level)

    override fun toString(): String {
        return "Pet{stats=$stats, level=$level, experience=$experience, name='$name'}"
    }

    companion object {

        private val EXPERIENCE_LEVELS = intArrayOf(200, 210, 230, 250, 280, 310, 350, 390, 450, 500, 570, 640, 710, 800, 880, 980, 1080, 1190, 1300, 1420, 1540, 1670, 1810, 1950, 2100, 2260, 2420, 2580, 2760, 2940, 3120, 3310, 3510, 3710, 3920, 4140, 4360, 4590, 4820, 5060, 5310, 5560, 5820, 6090, 6360, 6630, 6920, 7210, 7500, 7800, 8110, 8420, 8740, 9070, 9400, 9740, 10080, 10430, 10780, 11150, 11510, 11890, 12270, 12650, 13050, 13440, 13850, 14260, 14680, 15100, 15530, 15960, 16400, 16850, 17300, 17760, 18230, 18700, 19180, 19660, 20150, 20640, 21150, 21650, 22170, 22690, 23210, 23750, 24280, 24830, 25380, 25930, 26500, 27070, 27640, 28220, 28810, 29400, 30000)
    }
}
