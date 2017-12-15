/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.ConstantsMG
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.container.ContainerFrame
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import java.util.*

/**
 * DefScrollableContainer Created by Alexander on 26.02.2017.
 * Description:
 */
open class DefScrollableContainer(color: ColorRGBA, width: Int, height: Int, spacer: IScrollSpacerRenderer?) : ContainerFrame(width, height, 100, 50, color) {

    private var indexScroll: Int = 0
    private var pixelSize = 0
    private var pixelPos = 0

    private val hasSpacer: Boolean = spacer != null
    private val spacer: IScrollSpacerRenderer?
    private val spacerHeight: Int

    var hasScrollbar = true
    var isAlignTop = false
    var hasTopFade = true

    val scrollElements = ArrayList<Container>()
    private val spacerPositions = ArrayList<Int>()

    fun clearElements(){
        scrollElements.clear()
        spacerPositions.clear()
        updateWithoutAnimation()
    }

    fun changeElementPosition(pos : Int, container: Container){
        var conPos = scrollElements.indexOf(container)
        if(conPos > pos){
            while (conPos > pos){
                Collections.swap(scrollElements, conPos, conPos -1)
                conPos--
            }
        }else if(conPos < pos){
            while (conPos < pos){
                Collections.swap(scrollElements, conPos, conPos +1)
                conPos++
            }
        }
    }

    //##################################################################################################################

    private val pixelPosFromIndex: Int
        get() {
            if ((indexScroll ) * PIXEL_PER_INDEX > pixelSize - height) return pixelSize - height
            else  return PIXEL_PER_INDEX * indexScroll
        }

    private val indexFromPixelPos: Int
        get() = Math.ceil((pixelPos.toDouble() / PIXEL_PER_INDEX.toDouble())).toInt()

    private val maxScrollIndex: Int
        get() {
            if (PIXEL_PER_INDEX == 0) return 0
            return (pixelSize - height) / PIXEL_PER_INDEX
        }

    private val scrollBarPosY: Int
        get() {
            val scrollHeight = height - 45
            if (maxScrollIndex != 0)
                return yStart + 5 + (scrollHeight - Math.round(scrollHeight * (indexScroll.toFloat() / ((pixelSize - height).toFloat() / PIXEL_PER_INDEX.toFloat()))))
            else
                return yStart + 5 + height - 20
        }

    //##################################################################################################################

    init {
        if (hasSpacer) {
            this.spacer = spacer
            this.spacerHeight = spacer!!.spacerHeight
        } else {
            this.spacer = null
            this.spacerHeight = 0
        }
    }

    //##################################################################################################################

    open fun registerScrollElement(container: Container) {
        if (scrollElements.size + 1 > MAX_STORED)
            unregisterScrollElement(scrollElements[scrollElements.size - 1])

        container.heightChangedCallback.registerListener { updateWithoutAnimation() }

        registerChild(container)
        scrollElements.add(container)

        indexScroll = 0
        pixelPos = 0
        updateWithoutAnimation()
    }

    fun unregisterScrollElement(container: Container) {
        unregisterChild(container)
        scrollElements.remove(container)
        updateWithoutAnimation()
    }

    private fun updateWithAnimation() {
        scrollAnimated = PIXEL_PER_INDEX
    }

    private fun updateWithoutAnimation() {
        spacerPositions.clear()
        if(hasSpacer) pixelSize = scrollElements.stream().mapToInt({ it.height }).sum() + scrollElements.size * spacerHeight
        else pixelSize = scrollElements.stream().mapToInt({ it.height }).sum()

        indexScroll = indexFromPixelPos

        var offset = 0

        if (isAlignTop) for (con in scrollElements) {
            con.yOffset = - pixelPos - offset - scrollAnimated
            if (hasSpacer && scrollElements.indexOf(con) != scrollElements.size - 1)
                spacerPositions.add(- pixelPos + offset + con.height  - scrollAnimated)
            offset -= con.height + spacerHeight
        }

        else for (con in scrollElements) {
            con.yOffset = pixelPos - offset + height - pixelSize - scrollAnimated
            if (hasSpacer && scrollElements.indexOf(con) != scrollElements.size - 1)
                spacerPositions.add(pixelPos - offset + con.height + height - pixelSize - scrollAnimated)
            offset -= con.height + spacerHeight
        }
    }

    private var isScrollUp = false
    private var scrollAnimated = 0
    private fun scrollAnimation() {
        if (scrollAnimated <= 0){
            updateWithoutAnimation()
            return
        }
        scrollAnimated -= 1
        spacerPositions.clear()

        if(hasSpacer) pixelSize = scrollElements.stream().mapToInt({ it.height }).sum() + scrollElements.size * spacerHeight
        else pixelSize = scrollElements.stream().mapToInt({ it.height }).sum()

        pixelPos = pixelPosFromIndex

        var offset = 0

        if (isScrollUp)
            for (con in scrollElements) {
                if(isAlignTop){
                    con.yOffset = - pixelPos - offset  + scrollAnimated
                    if (hasSpacer && scrollElements.indexOf(con) != scrollElements.size - 1)
                        spacerPositions.add(- pixelPos + offset + con.height   + scrollAnimated)
                    offset -= con.height + spacerHeight
                }else {
                    con.yOffset =  pixelPos - offset + height - pixelSize - scrollAnimated
                    if (hasSpacer && scrollElements.indexOf(con) != scrollElements.size - 1)
                        spacerPositions.add(indexScroll * PIXEL_PER_INDEX - offset + con.height + height - pixelSize - scrollAnimated)
                    offset -= con.height + spacerHeight
                }

            }
        else
            for (con in scrollElements) {
                if(isAlignTop){
                    con.yOffset = - pixelPos - offset - scrollAnimated
                    if (hasSpacer && scrollElements.indexOf(con) != scrollElements.size - 1)
                        spacerPositions.add(- pixelPos + offset + con.height  - scrollAnimated)
                    offset -= con.height + spacerHeight
                } else {
                    con.yOffset = pixelPos - offset + height - pixelSize + scrollAnimated
                    if (hasSpacer && scrollElements.indexOf(con) != scrollElements.size - 1)
                        spacerPositions.add(pixelPos - offset + con.height + height - pixelSize + scrollAnimated)
                    offset -= con.height + spacerHeight
                }
            }
    }

    //##################################################################################################################


    private var isScrollByBar = false
    override fun doClientTickLocal(): Boolean {
        scrollAnimation()

        if (isScrollByBar) {
            val i = (height - 50).toFloat()
            val mY = MouseHandler.mY - yStart - 25
            if (mY < 0)
                pixelPos = pixelSize - height
            else if (mY > i)
                pixelPos = 0
            else
                pixelPos = Math.round(Math.abs(mY - i) * ((pixelSize - height).toFloat() / i))
            updateWithoutAnimation()
        }


        return true
    }

    /**
     * Renderer
     */
    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {

        if (ees == EnumEventState.PRE) {
            RenderUtils.renderBoxWithColor(xStart - 5, yStart - 5, width + 100, height + 100, backgroundRGBA)
            if (scrollElements.size > 1 && hasSpacer)
                spacerPositions.forEach { y -> spacer!!.render(xStart, yStart + y, width) }
        }

        if (ees == EnumEventState.POST) {
            if(hasTopFade){
                RenderUtils.renderBoxWithColor(xStart, yStart - 1, width, 7, backgroundRGBA)
                RenderUtils.rect_fade_horizontal_s1_d1(xStart, yStart + 6, width, 30, backgroundRGBA,
                        ColorRGBA(backgroundRGBA.red, backgroundRGBA.green, backgroundRGBA.blue, 0))
            }

            if (!(pixelSize < height || !hasScrollbar)){
                val scroll = Math.round(Math.abs(pixelPos - (pixelSize - height)) * ((height - 50).toFloat() / (pixelSize - height).toFloat()))
                RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + width - 10, yStart + 10, height - 20, 4, RGBA.BLACK_LIGHT.get(), ConstantsMG.DEF_BACKGROUND_RGBA, 2)
                RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + width - 11, yStart + scroll + 10, 30, 6, RGBA.P1B1_596068.get(), RGBA.NULL.get(), 2)
            }
        }
        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        if (clickType == MouseHandler.ClickType.DRAG)
            if (this.checkIfMouseOver(xStart + width - 11, scrollBarPosY, 6, 30)) isScrollByBar = true

        if (clickType == MouseHandler.ClickType.DROP)
            if (isScrollByBar) isScrollByBar = false

        return true
    }

    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean {
        if (!isThisContainer) return true
        if (i != 0) {
            if (i > 0) {
                if ((indexScroll ) * PIXEL_PER_INDEX > pixelSize - height) return true
                indexScroll++
                isScrollUp = true
            } else {
                if (indexScroll - 1 < 0) return true
                indexScroll--
                isScrollUp = false
            }
            updateWithAnimation()
        }
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        scrollElements.forEach { container -> container.width = getWidth() }
        indexScroll = 0
        updateWithoutAnimation()
        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean { return true }
    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean { return true }
    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean { return true }
    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean { return true }

    companion object {
        private val PIXEL_PER_INDEX = 15
        private val MAX_STORED = 50
    }
}
