/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.chatgui

import net.unaussprechlich.managedgui.lib.container.register
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefCustomRenderContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefWrapperContainer
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import net.unaussprechlich.project.connect.chatgui.list.ChatListContainer

object ChatWrapper : DefWrapperContainer(){

    var isMax = false
    var move = false
    var prevX = 0
    var prevY = 0

    val stdWidth = 500
    val stdHeight = 300
    val stdChatListWidth = 130

    var resize = false
    private set
    private var isSetup = false

    val chatController = ChatController
    val chatListCon = ChatListContainer
    val controllerCon = ChatWindowControllerContainer
    val resizeCon = DefCustomRenderContainer { xStart, yStart , _ , _, con, _ ->
        if(con.isHover || resize)   RenderUtils.iconRender_resize(xStart + con.width, yStart + con.height, RGBA.WHITE.get())
        else                        RenderUtils.iconRender_resize(xStart + con.width, yStart + con.height, RGBA.P1B1_596068.get())
    } .apply {
        width = 10
        height = 10
    }

    fun getChatWidth(): Int = width - chatListCon.width
    fun getChatHeight(): Int = height - controllerCon.height

    fun updateResizeIconPosition (){
        resizeCon.xOffset =  width - resizeCon.width - 2
        resizeCon.yOffset =  height - ChatController.getChatInputFieldHeight()  - resizeCon.height
    }

    init {
        isVisible = true

        width = stdWidth
        height = stdHeight

        resizeCon.clickedCallback.registerListener { clickType, _ ->
            if(clickType.isDrag()) resize = true
        }

        this register controllerCon
        this register chatListCon
        this register resizeCon

        minWidth  = 400
        minHeight = 200

        chatListCon.yOffset = controllerCon.height

        updateResizeIconPosition()

        setXYOffset(20, 20)
    }

    fun resizeThatThing(width : Int, height : Int){
        val w = if(width < minWidth)   minWidth  else width
        val h = if(height < minHeight) minHeight else height

        this.width = w
        this.height = h

        chatController.resize()

        controllerCon.width = w
        chatListCon.height = h - controllerCon.height

        updateResizeIconPosition()

        onResize()
    }

    override fun doClientTickLocal(): Boolean {
        if(!isSetup){
            resizeThatThing(stdWidth, stdHeight)
            isSetup = true
        }
        if (move)   ChatWrapper.setXYOffset(MouseHandler.mX - controllerCon.getMoveConXoffset() - 7, MouseHandler.mY - controllerCon.getMoveConYoffset() - 7)
        if (resize) resizeThatThing(MouseHandler.mX - xStart , MouseHandler.mY - yStart)

        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        if (clickType == MouseHandler.ClickType.DROP && move)   move = false
        if (clickType == MouseHandler.ClickType.DROP && resize) resize = false
        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent : T): Boolean {
        if(iEvent.id == EnumDefaultEvents.SCREEN_RESIZE.get()){
            if(isMax || stdWidth > DisplayUtil.scaledMcWidth - 10 || stdHeight > DisplayUtil.scaledMcHeight - 10 )
                resizeThatThing(DisplayUtil.scaledMcWidth - 10, DisplayUtil.scaledMcHeight - 10)
            else
                resizeThatThing(stdWidth, stdHeight)
            setXYOffset(5, 5)
        }
        if (!isVisible && iEvent.id == EnumDefaultEvents.KEY_PRESSED.get()) return false
        if (!isVisible && iEvent.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()) return false
        return true
    }

}