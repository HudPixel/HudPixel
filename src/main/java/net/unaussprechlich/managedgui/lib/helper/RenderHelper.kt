package net.unaussprechlich.managedgui.lib.helper

import net.unaussprechlich.managedgui.lib.elements.Container
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import net.unaussprechlich.managedgui.lib.util.storage.StorageFourSide
import net.unaussprechlich.managedgui.lib.util.storage.StorageTwoSame
import net.minecraft.util.ResourceLocation

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
object RenderHelper {

    fun renderContainer(container: Container) {
        val innerSize = processInnerSize(container.width, container.height, container.padding)
        renderContainerBackground((container.xStart.toInt() + container.margin.LEFT.toInt() + container.border.LEFT.toInt()).toShort(), (container.yStart.toInt() + container.margin.TOP.toInt() + container.border.TOP.toInt()).toShort(), container.backgroundRGBA, innerSize.t0, innerSize.t1, container.backgroundImage)
        RenderUtils.renderBorder((container.xStart + container.margin.LEFT).toShort(), (container.yStart + container.margin.TOP).toShort(), innerSize.t0, innerSize.t1, container.border, container.borderRGBA)
    }

    private fun renderContainerBackground(xStart: Short, yStart: Short, color: ColorRGBA, width: Short, height: Short, image: ResourceLocation?) {
        if (image == null)
            RenderUtils.renderBoxWithColor(xStart.toFloat(), yStart.toFloat(), width.toInt(), height.toInt(), color)
        else
            RenderUtils.drawModalRectWithCustomSizedTexture(xStart, yStart, width, height, image, color.ALPHA)
    }

    private fun processInnerSize(width: Short, height: Short, padding: StorageFourSide): StorageTwoSame<Short> {
        return StorageTwoSame((width.toInt() + padding.LEFT.toInt() + padding.RIGHT.toInt()).toShort(), (height.toInt() + padding.BOTTOM.toInt() + padding.TOP.toInt()).toShort())
    }
}
