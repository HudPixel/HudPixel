package net.hypixel.api.util.pets

import com.google.common.collect.Maps

class PetStats(petStats: Map<String, Map<String, Any>>) {

    private val petMap = Maps.newHashMap<PetType, Pet>()

    init {
        for (stringMapEntry in petStats.entries) {
            try {
                petMap.put(PetType.valueOf(stringMapEntry.key), Pet(stringMapEntry.value))
            } catch (e: IllegalArgumentException) {
                println("Invalid pet! " + stringMapEntry.key)
            }

        }
    }

    fun getPet(type: PetType): Pet? {
        return petMap[type]
    }

    val allPets: Map<PetType, Pet>
        get() = petMap

    override fun toString(): String {
        return "PetStats{petMap=$petMap}"
    }
}
