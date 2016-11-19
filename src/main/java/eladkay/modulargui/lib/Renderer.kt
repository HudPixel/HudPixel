package eladkay.modulargui.lib

import com.palechip.hudpixelmod.HudPixelMod
import com.palechip.hudpixelmod.config.GeneralConfigSettings
import com.palechip.hudpixelmod.util.DisplayUtil
import eladkay.modulargui.lib.base.SimpleModularGuiProvider
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.managedgui.lib.util.RenderUtils

/**
 * This class is responsible for rendering the elements of the modular GUI.

 * @author Eladkay
 * *
 * @since 1.5
 */
class Renderer {

    //Replace with some sort of config option
    private val isEnabled = true

    /**
     * Event: When the game renders its overlay.
     */
    @SubscribeEvent
    fun onRenderTick(event: TickEvent.RenderTickEvent) {
        if (GeneralConfigSettings.hudDisabled) return
        if (!HudPixelMod.isHypixelNetwork && !HudPixelMod.IS_DEBUGGING) return
        if (!Minecraft.getMinecraft().inGameHasFocus) return
        val display = ModularGuiRegistry.allElements //the elements
        val w = GeneralConfigSettings.displayXOffset //width, change this if needed
        var h = GeneralConfigSettings.displayYOffset //height, you shouldn't touch this usually
        if (isEnabled) { //if enabled...
            val fontRendererObj = FMLClientHandler.instance().client.fontRendererObj //get the font renderer
            for (element in display) { //for each element...
                if (!element.provider.showElement()) continue //if you shouldn't show it, skip it.
                val aDisplay: String
                if (!element.name.isEmpty() && element.provider !is SimpleModularGuiProvider)
                    aDisplay = element.name + ": " + element.provider.content() //get the display text for the element
                else
                    aDisplay = element.provider.content()
                if (element.provider.content() == null) return
                if (element.provider is SimpleModularGuiProvider || !(element.provider.content().isEmpty() && element.name.isEmpty()) || element.provider.ignoreEmptyCheck()) { //if it's not empty or it's allowed to override this isHypixelNetwork...
                    var xOffset = 0
                    if (GeneralConfigSettings.hudRenderRight)
                        xOffset = DisplayUtil.getScaledMcWidth() - fontRendererObj.getStringWidth(aDisplay) - 4
                    if (GeneralConfigSettings.hudBackground)
                        RenderUtils.renderBoxWithColor((w - 2 + xOffset).toDouble(), (h - 1).toDouble(), (fontRendererObj.getStringWidth(aDisplay) + 3).toDouble(), 10.0, GeneralConfigSettings.hudRed.toFloat() / 255, GeneralConfigSettings.hudGreen.toFloat() / 255, GeneralConfigSettings.hudBlue.toFloat() / 255, GeneralConfigSettings.hudAlpha.toFloat() / 255)
                    fontRendererObj.drawString(aDisplay, w + xOffset, h, 0xffffff) //draw it
                    h += 10 //increment height
                }
            }
        }
    }
}
