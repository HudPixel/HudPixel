/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.managedgui.lib.container

import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.child.ChildRegistry
import net.unaussprechlich.managedgui.lib.child.IChild
import net.unaussprechlich.managedgui.lib.event.Callback
import net.unaussprechlich.managedgui.lib.event.PayloadCallback
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderHelper
import net.unaussprechlich.managedgui.lib.util.storage.ContainerSide



infix fun Container.register(con : Container) = this.registerChild(con)
infix fun Container.unregister(con : Container) = this.unregisterChild(con)

abstract class Container : ChildRegistry(), IContainer, IChild {

    private var height = 0
    private var width = 0
    private var xStart = 0
    private var yStart = 0

    var minWidth = 0
    var minHeight = 0

    private var xOffset = 0
    private var yOffset = 0

    private var isHover = false
    private var visible = true
    var isRenderBackground = true

    var parent: Container? = null
        private set


    private var border = ContainerSide()
    private var margin = ContainerSide()
    private var padding = ContainerSide()

    private var borderRGBA = RGBA.TRANSPARENT.get()
    private var backgroundRGBA = RGBA.TRANSPARENT.get()

    val clickedCallback = PayloadCallback<MouseHandler.ClickType>()

    private var backgroundImage: ResourceLocation? = null

    //ABSTRACT STUFF ---------------------------------------------------------------------------------------------------

    protected abstract fun doClientTickLocal(): Boolean

    protected abstract fun doRenderTickLocal(
            xStart: Int,
            yStart: Int,
            width: Int,
            height: Int,
            ees: EnumEventState
    ): Boolean

    protected abstract fun doChatMessageLocal(
            e: ClientChatReceivedEvent
    ): Boolean

    protected abstract fun doClickLocal(
            clickType: MouseHandler.ClickType,
            isThisContainer: Boolean
    ): Boolean

    protected abstract fun doScrollLocal(
            i: Int,
            isThisContainer: Boolean
    ): Boolean

    protected abstract fun doMouseMoveLocal(
            mX: Int,
            mY: Int
    ): Boolean

    protected abstract fun <T : Event<*>> doEventBusLocal(
            iEvent: T
    ): Boolean

    protected abstract fun doOpenGUILocal(
            e: GuiOpenEvent
    ): Boolean

    protected abstract fun doResizeLocal(
            width: Int,
            height: Int
    ): Boolean

    //METHODS ----------------------------------------------------------------------------------------------------------


    override fun <T : IChild> registerChild(child: T) {
        if (child is Container) child.parent = this
        super.registerChild(child)
    }

    override fun unregisterChild(child: IChild) {
        if (child is Container) child.parent = null
        super.unregisterChild(child)
    }

    fun checkIfMouseOver(xStart: Int, yStart: Int, width: Int, height: Int): Boolean {
        val mX = MouseHandler.mX
        val mY = MouseHandler.mY
        return mX > xStart && mX < xStart + width
                && mY > yStart && mY < yStart + height
    }

    fun checkIfMouseOverContainer(mX: Int, mY: Int): Boolean {
        return mX > xStartBorder && mX < xStart + padding.RIGHT() + border.RIGHT() + width
                && mY > yStartBorder && mY < yStart + padding.BOTTOM() + border.BOTTOM() + height
    }

    protected fun updateXYStart(xStart: Int, yStart: Int) {
        this.xStart = xStart + xOffset + padding.LEFT() + margin.LEFT() + border.LEFT()
        this.yStart = yStart + yOffset + padding.TOP() + margin.TOP() + border.TOP()
    }

    fun THIS(): Container {
        return this
    }


    //EVENT STUFF ------------------------------------------------------------------------------------------------------

    override fun doClientTick(): Boolean {
        return doClientTickLocal()
    }

    override fun doRender(xStart: Int, yStart: Int): Boolean {
        updateXYStart(xStart, yStart)
        if (!visible) return false

        if (!doRenderTickLocal(this.xStart, this.yStart, width, height, EnumEventState.PRE)) return false

        if (isRenderBackground) RenderHelper.renderContainer(this)

        return doRenderTickLocal(this.xStart, this.yStart, width, height, EnumEventState.POST)
    }

    override fun doChatMessage(e: ClientChatReceivedEvent): Boolean {
        return doChatMessageLocal(e)
    }

    override fun doClick(clickType: MouseHandler.ClickType): Boolean {

        if (isHover()) clickedCallback.broadcast(clickType, this)

        return doClickLocal(clickType, this.isHover)
    }

    override fun doScroll(i: Int): Boolean {
        return doScrollLocal(i, this.isHover)
    }

    override fun doMouseMove(mX: Int, mY: Int): Boolean {
        this.isHover = checkIfMouseOverContainer(mX, mY)
        return doMouseMoveLocal(mX, mY)
    }

    override fun <T : Event<*>> doEventBus(event: T): Boolean {
        return doEventBusLocal(event)
    }


    override fun doResize(): Boolean {
        return doResizeLocal(width, height)
    }


    //GETTER -----------------------------------------------------------------------------------------------------------

    override fun getHeight(): Int {
        return height
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getWidthMargin(): Int {
        return widthBorder + margin.LEFT() + margin.RIGHT()
    }

    override fun getHeightMargin(): Int {
        return heightBorder + margin.TOP() + margin.LEFT()
    }

    override fun getWidthBorder(): Int {
        return widthPadding + border.LEFT() + border.RIGHT()
    }

    override fun getHeightBorder(): Int {
        return heightPadding + border.TOP() + border.BOTTOM()
    }

    override fun getWidthPadding(): Int {
        return width + padding.LEFT() + padding.RIGHT()
    }

    override fun getHeightPadding(): Int {
        return height + padding.TOP() + padding.BOTTOM()
    }

    override fun getXOffset(): Int {
        return xOffset
    }

    override fun getYOffset(): Int {
        return yOffset
    }

    override fun isVisible(): Boolean {
        return visible
    }

    override fun isHover(): Boolean {
        return isHover
    }

    override fun getYStart(): Int {
        return yStart
    }

    override fun getXStart(): Int {
        return xStart
    }

    override fun getYStartMargin(): Int {
        return yStartBorder - margin.TOP()
    }

    override fun getXStartMargin(): Int {
        return xStartBorder - margin.LEFT()
    }

    override fun getYStartPadding(): Int {
        return yStart - padding.TOP()
    }

    override fun getXStartPadding(): Int {
        return xStart - padding.LEFT()
    }

    override fun getYStartBorder(): Int {
        return yStartPadding - border.TOP()
    }

    override fun getXStartBorder(): Int {
        return xStartPadding - border.LEFT()
    }

    override fun getBorderRGBA(): ColorRGBA {
        return borderRGBA
    }

    override fun getBackgroundRGBA(): ColorRGBA {
        return backgroundRGBA
    }

    override fun getBackgroundImage(): ResourceLocation? {
        return backgroundImage
    }

    override fun getPadding(): ContainerSide {
        return padding
    }

    override fun getMargin(): ContainerSide {
        return margin
    }

    override fun getBorder(): ContainerSide {
        return border
    }

    //SETTER -----------------------------------------------------------------------------------------------------------

    val visibleChangedCallback = Callback()
    override fun setVisible(visible: Boolean) {
        if(this.visible != visible) visibleChangedCallback.broadcast(this)
        this.visible = visible
    }

    val heightChangedCallback = Callback()
    override fun setHeight(height: Int) {
        if(this.height != height) heightChangedCallback.broadcast(this)
        this.height = height
    }

    val widthChangedCallback = Callback()
    override fun setWidth(width: Int) {
        if(this.width != width) widthChangedCallback.broadcast(this)
        this.width = width
        //onResize();
    }

    override fun setYOffset(yOffset: Int) {
        this.yOffset = yOffset
    }

    override fun setXOffset(xOffset: Int) {
        this.xOffset = xOffset
    }

    override fun setXYOffset(xOffset: Int, yOffset: Int) {
        this.xOffset = xOffset
        this.yOffset = yOffset
    }

    override fun setBorderRGBA(borderRGBA: ColorRGBA) {
        this.borderRGBA = borderRGBA
    }

    override fun setBackgroundRGBA(backgroundRGBA: ColorRGBA) {
        this.backgroundRGBA = backgroundRGBA
    }

    override fun setBackgroundImage(backgroundImage: ResourceLocation) {
        this.backgroundImage = backgroundImage
    }

    override fun setPadding(value: Int) {
        this.padding.RIGHT(value).LEFT(value).TOP(value).BOTTOM(value)
    }

    override fun setMargin(value: Int) {
        this.margin.RIGHT(value).LEFT(value).TOP(value).BOTTOM(value)
    }

    override fun setBorder(value: Int) {
        this.border.RIGHT(value).LEFT(value).TOP(value).BOTTOM(value)
    }

    override fun setPadding(padding: ContainerSide) {
        this.padding = padding
    }

    override fun setMargin(margin: ContainerSide) {
        this.margin = margin
    }

    override fun setBorder(border: ContainerSide) {
        this.border = border
    }
}
