package eladkay.hudpixel.modulargui.components

import eladkay.hudpixel.config.CCategory
import eladkay.hudpixel.config.ConfigPropertyBoolean
import eladkay.hudpixel.modulargui.HudPixelModularGuiProvider
import eladkay.hudpixel.util.autotip.event.Tipper
import java.time.LocalTime

/**
 * Created by Elad on 5/13/2017.
 */
object AutotipWaveModularGuiProvider : HudPixelModularGuiProvider() {
    @ConfigPropertyBoolean(CCategory.HUDPIXEL, "autotip", "Should autotip?", true) @JvmStatic var autotip = true

    override fun showElement(): Boolean {
        return true
    }

    override fun content(): String {
        return LocalTime.MIN.plusSeconds(
                (Tipper.waveLength - Tipper.waveCounter).toLong()).toString()
    }

    override fun ignoreEmptyCheck(): Boolean {
        return false
    }

    override fun getAfterstats(): String {
        return ""
    }

    override fun doesMatchForGame(): Boolean {
        return true
    }

    /**
     * This is called when the mod has detected that the player joined a game of this type.
     * It should reset the rendered strings to the default ones.
     */
    override fun setupNewGame() {

    }

    /**
     * Called when the game starts.
     */
    override fun onGameStart() {

    }

    /**
     * Called when the game ends.
     */
    override fun onGameEnd() {

    }

    /**
     * If the game is running, it'll receive ticks to update the rendered strings.
     */
    override fun onTickUpdate() {

    }

    /**
     * If the game is running, it'll receive the chat messages which the client receives.
     */
    override fun onChatMessage(textMessage: String?, formattedMessage: String?) {

    }
}