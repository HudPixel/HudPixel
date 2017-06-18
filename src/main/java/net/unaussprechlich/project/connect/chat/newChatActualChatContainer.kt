package net.unaussprechlich.project.connect.chat

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
object newChatActualChatContainer : DefScrollableContainer(RGBA.P1B1_DEF.get(), 500, 300, chatConSpacerRenderer){

    val loadMessageAmount = 30

    var currentChat : Chat? = null

    init {
        isResizeable = false
    }

    fun loadChat(chat : Chat){
        clearElements()
        clearChilds()

        currentChat = chat

        if(currentChat!!.latestIndex > loadMessageAmount)
            for(index in currentChat!!.latestIndex - loadMessageAmount .. currentChat!!.latestIndex){
                importChatMessage(index)
            }
        else
            for(index in 0 .. currentChat!!.latestIndex){
                importChatMessage(index)
            }
    }

    fun importChatMessage(index : Long){
        registerScrollElement(currentChat!!.getMessageAsCon(index))
    }
}

