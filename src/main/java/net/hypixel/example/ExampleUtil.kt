package net.hypixel.example

import java.util.UUID

object ExampleUtil {

    val API_KEY = UUID.fromString("64bd424e-ccb0-42ed-8b66-6e42a135afb4") // arbitrary key, replace with your own to test

    interface UUIDList {
        companion object {
            val HYPIXEL = UUID.fromString("f7c77d99-9f15-4a66-a87d-c4a51ef30d19")
        }
    }

    /**
     * Keep the program alive till we explicitly exit.
     */
    internal fun await() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }
}
