/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.project.connect.chatgui.chat

import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefScrollableContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.IScrollSpacerRenderer
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils

val chatConSpacerRenderer = object : IScrollSpacerRenderer {
    override fun render(xStart: Int, yStart: Int, width: Int) {
        RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + 25, yStart, width - 42, 1, RGBA.P1B1_596068.get())
    }
    override val spacerHeight: Int
        get() = 1
}

/**
 * DefScrollableContainer Created by Alexander on 26.02.2017.
 * Description:
 */
class ChatActualChatContainer(width : Int, height : Int) : DefScrollableContainer(RGBA.P1B1_DEF.get(), width, height, chatConSpacerRenderer){

    init {
        isResizeable = false
    }

}

