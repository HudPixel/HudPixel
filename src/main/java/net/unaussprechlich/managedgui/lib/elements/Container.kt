package net.unaussprechlich.managedgui.lib.elements

import net.unaussprechlich.managedgui.lib.helper.Child
import net.unaussprechlich.managedgui.lib.helper.MouseHandler
import net.unaussprechlich.managedgui.lib.helper.RenderHelper
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.storage.StorageFourSide
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
abstract class Container : Child {

    var padding = StorageFourSide()
    var margin = StorageFourSide()
    var border = StorageFourSide()


    var height: Short = 0
    var width: Short = 0
    var xStart: Short = 0
    var yStart: Short = 0
    var xStartInner: Short = 0
    var yStartInner: Short = 0
    var borderRGBA = ColorRGBA()
    var backgroundRGBA = ColorRGBA()

    var backgroundImage: ResourceLocation? = null

    internal abstract fun onClientTickLocal(): Boolean

    internal abstract fun onRenderTickLocal(xStart: Short, yStart: Short, width: Short, height: Short): Boolean

    internal abstract fun onChatMessageLocal(e: ClientChatReceivedEvent): Boolean

    internal abstract fun onClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean

    internal abstract fun onScrollLocal(i: Int, isThisContainer: Boolean): Boolean

    internal abstract fun onMouseMoveLocal(mX: Int, mY: Int): Boolean

    private fun checkIfMouseOverContainer(mX: Int, mY: Int): Boolean {
        return mX > xStart + margin.LEFT && mX < xStartInner.toInt() + padding.RIGHT.toInt() + border.RIGHT.toInt() + width.toInt()
                && mY > yStart + margin.TOP && mY < yStartInner.toInt() + padding.BOTTOM.toInt() + border.BOTTOM.toInt() + height.toInt()
    }

    override fun onClientTick() {
        if (!onClientTickLocal()) return
        xStartInner = (xStart.toInt() + padding.LEFT.toInt() + border.LEFT.toInt() + margin.LEFT.toInt()).toShort()
        yStartInner = (yStart.toInt() + padding.TOP.toInt() + border.TOP.toInt() + margin.TOP.toInt()).toShort()
    }

    override fun onRenderTick() {
        if (!onRenderTickLocal(xStartInner, yStartInner, width, height)) return
        RenderHelper.renderContainer(this)
    }

    override fun onChatMessage(e: ClientChatReceivedEvent) {
        if (!onChatMessageLocal(e)) return
    }

    override fun onClick(clickType: MouseHandler.ClickType) {
        val isThisContainer = checkIfMouseOverContainer(MouseHandler.getmX(), MouseHandler.getmY())
        if (!onClickLocal(clickType, isThisContainer)) return
    }

    override fun onScroll(i: Int) {
        val isThisContainer = checkIfMouseOverContainer(MouseHandler.getmX(), MouseHandler.getmY())
        if (!onScrollLocal(i, isThisContainer)) return
    }

    override fun onMouseMove(mX: Int, mY: Int) {
        if (!onMouseMoveLocal(mX, mY)) return
    }
}//CONSTRUCTOR
