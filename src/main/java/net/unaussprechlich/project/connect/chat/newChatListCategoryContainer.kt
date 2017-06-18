package net.unaussprechlich.project.connect.chat

import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.Callback
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.*
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils

class newChatListCategoryContainer(title : String, val colorRGBA: ColorRGBA) : DefWrapperContainer(){

    private val elements = ArrayList<Container>()

    private val elementWrapperCon = DefWrapperContainer()

    private var title = title
        set(value) {
            titleCon.text = value
            field = value
        }

    private var isExpanded = false

    private val titleCon = DefTextContainer("NULL")
    private val categoryBackgroundCon = DefBackgroundContainer(RGBA.C_161b21.get(), ChatWrapper.stdChatListWidth, 13)
    private val shadowRenderer = DefCustomRenderContainer(renderer = {xStart, yStart, width , height, con , ees ->
        if(ees == EnumEventState.PRE) RenderUtils.rect_fade_horizontal_s1_d1(xStart, yStart, width, 13, RGBA.P1B1_DEF.get(), RGBA.C_161b21.get())
    })

    private val expandButtonCon = DefButtonContainer(9, 9,
            clickedListener = {clickType , _ ->
                if(clickType == MouseHandler.ClickType.SINGLE){
                    isExpanded = !isExpanded
                    elementWrapperCon.isVisible = isExpanded
                    updateHeight()
                }
            },
            customRender = {xStart, yStart, width, height ->
                if(isExpanded) RenderUtils.renderBoxWithColor(xStart , yStart + width/2 , width , 1, RGBA.P1B1_596068.get())
                else {
                    RenderUtils.renderBoxWithColor(xStart, yStart + width/2 , width, 1, RGBA.P1B1_596068.get())
                    RenderUtils.renderBoxWithColor(xStart + height/2, yStart, 1, height, RGBA.P1B1_596068.get())
                }
            },
            customRenderHoover = {xStart, yStart, width, height ->
                if(isExpanded) RenderUtils.renderBoxWithColor(xStart , yStart + width/2 , width , 1, RGBA.WHITE.get())
                else {
                    RenderUtils.renderBoxWithColor(xStart, yStart + width/2 , width , 1 , RGBA.WHITE.get())
                    RenderUtils.renderBoxWithColor(xStart+ height/2, yStart , 1 , height , RGBA.WHITE.get())
                }
            })

    init {
        width = ChatWrapper.stdChatListWidth
        height = 13

        titleCon.xOffset = 3
        titleCon.yOffset = 1
        titleCon.text = title

        expandButtonCon.yOffset = 2
        expandButtonCon.xOffset = width - 11

        elementWrapperCon.yOffset = 13
        elementWrapperCon.isVisible = false

        registerChild(elementWrapperCon)
        registerChild(categoryBackgroundCon)
        shadowRenderer.width = ChatWrapper.stdChatListWidth
        categoryBackgroundCon.registerChild(shadowRenderer)
        categoryBackgroundCon.registerChild(titleCon)
        categoryBackgroundCon.registerChild(expandButtonCon)
    }

    val activateCallback = Callback()

    fun registerElement(con : newChatListElementContainer){
        con.color = colorRGBA
        con.activateCallback.registerListener {activateCallback.broadcast(it)}
        elements.add(con)
        elementWrapperCon.registerChild(con)
        updateHeight()
    }

    fun unregisterElement(con : newChatListElementContainer){
        elements.remove(con)
        elementWrapperCon.unregisterChild(con)
        updateHeight()
    }

    fun updateHeight(){
        var index = 0
        elements.forEach {
            it.yOffset = (it.height + 1) * index
            index++
        }

        elementWrapperCon.height = elements.size * 27

        if(isExpanded) height = categoryBackgroundCon.height + elementWrapperCon.height
        else height = categoryBackgroundCon.height
    }
}


