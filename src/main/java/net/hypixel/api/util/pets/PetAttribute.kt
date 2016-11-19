package net.hypixel.api.util.pets

enum class PetAttribute {

    HUNGER,
    THIRST,
    EXERCISE;

    val decay: Int
        get() = 1

}