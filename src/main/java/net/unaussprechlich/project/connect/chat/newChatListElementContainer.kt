package net.unaussprechlich.project.connect.chat

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.Callback
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.handler.ResourceHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefPictureContainer
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.FontUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils


class newChatListElementContainer : Container() {

    val activateCallback = Callback()
    var isActive = false
        set(value) {
            field = value
            if(isActive) activateCallback.broadcast(this)
        }

    var title = "NULL"
    var text = "NULL"
        set(value){
            field = value
            if(value.length > 29)
                renderText = value.substring(0, 25) + " ..."
            else
                renderText = value
        }

    private var renderText = "NULL"

    val iconCon = DefPictureContainer(ResourceHandler.STEVE_HEAD.res)

    var color = RGBA.WHITE.get()

    init {
        width = ChatWrapper.stdChatListWidth
        height = 26
        isRenderBackground = true

        iconCon.height = 16
        iconCon.width = 16
        iconCon.xOffset = 7
        iconCon.yOffset = 5

        registerChild(iconCon)
    }



    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if(ees == EnumEventState.PRE) return true

        if(isHover) RenderUtils.renderBoxWithColor(xStart, yStart, 3, height, color)
        else        RenderUtils.renderBoxWithColor(xStart, yStart, 2, height, color)


        FontUtil.drawWithColor(title, xStart + 26, yStart + 4, color)
        FontUtil.drawWithColor(renderText, xStart + 26, yStart + 13, RGBA.C_9e9e9e.get())

        return true
    }

    override fun doClientTickLocal(): Boolean {
        if(isHover && !isActive) backgroundRGBA = RGBA.P1B6_5A6775.get()
        else {
            if(isActive)backgroundRGBA = RGBA.P1B1_DEF.get()
            else backgroundRGBA = RGBA.P1B6_404C59.get()

        }
        text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore"
        return true
    }

    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean { return true }
    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        if(clickType == MouseHandler.ClickType.SINGLE && isThisContainer)
            isActive = true
        return true
    }
    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean { return true }
    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean { return true }
    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean { return true }
    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean { return true }
    override fun doResizeLocal(width: Int, height: Int): Boolean { return true }


}