/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.managedgui.lib

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ChatAllowedCharacters
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedCodeEvent
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedEvent
import net.unaussprechlich.managedgui.lib.event.events.ScreenResizeEvent
import net.unaussprechlich.managedgui.lib.event.events.TimeEvent
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.LoggerHelperMG
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import org.lwjgl.input.Keyboard
import kotlin.collections.ArrayList

/**
 * ManagedGui Created by unaussprechlich on 18.12.2016.
 * Description:
 */
object ManagedGui : GuiScreen() {

    //DEFAULT TYPED ----------------------------------------------------------------------------------------------------


    //EVENT_HANDLING ---------------------------------------------------------------------------------------------------
    private var prevWidth = DisplayUtil.scaledMcWidth
    private var prevHeight = DisplayUtil.scaledMcHeight
    private val guiList = ArrayList<GUI>()

    private val eventBusCallbacks = ArrayList<(T : Event<*>) -> Unit>()

    private fun getEventBus() :ArrayList<(T : Event<*>) -> Unit> {
        return ArrayList(eventBusCallbacks)
    }

    fun registerEventBusCallback(callback : (T : Event<*>) -> Unit){
        eventBusCallbacks.add(callback)
    }

    fun unregisterEventBusCallback(callback : (T : Event<*>) -> Unit){
        eventBusCallbacks.remove(callback)
    }

    var currentlyDisplayed : GUI? = null
        private set

    fun displayGUI(gui : GUI){
        closeCurrentGUI()
        currentlyDisplayed = gui
        addGUI(gui)
        guiList.forEach({it.isVisible = false})
        bindScreen()
        gui.isVisible = true
    }

    private fun closeGUI(gui : GUI){
        currentlyDisplayed = null
        Minecraft.getMinecraft().displayGuiScreen(null)
        gui.isVisible = false
        if(gui.keepInCache) removeGUI(gui)
        onGuiClosed()
    }

    fun closeCurrentGUI(){
        if(currentlyDisplayed != null) closeGUI(currentlyDisplayed!!)
    }

    private var isBinded = false
    private fun bindScreen() {
        isBinded = true
        Keyboard.enableRepeatEvents(true)
        guiList.forEach({if(it.getMode() == GUI.Mode.WHILEBINDED) it.isVisible = true})
        Minecraft.getMinecraft().displayGuiScreen(this)
    }

    @SubscribeEvent
    fun onClientTick(e: TickEvent.ClientTickEvent) {
        if (ManagedGuiLib.isIsDisabled) return



        if (prevWidth != DisplayUtil.scaledMcWidth || prevHeight != DisplayUtil.scaledMcHeight) {
            postEvent(ScreenResizeEvent())
            prevWidth = DisplayUtil.scaledMcWidth
            prevHeight = DisplayUtil.scaledMcHeight
        }
        processTimeEvents()
        try {
            MouseHandler.onClientTick()
            RenderUtils.onClientTick()

            //LoggerHelperMG.logInfo("--------------------------------------------------------------------------------");

            guiList.forEach(GUI::onClientTick)

        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    init {
        LoggerHelperMG.logInfo("Setting up GuiManager!")
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        isBinded = false
        Keyboard.enableRepeatEvents(false)
        guiList.forEach { gui -> gui.onCloseGUI() }
        guiList.forEach({if(it.getMode() == GUI.Mode.WHILEBINDED) it.isVisible = false})
        closeCurrentGUI()
        if (Minecraft.getMinecraft().currentScreen == null)
            Minecraft.getMinecraft().setIngameFocus()
    }

    override fun handleMouseInput() {
        super.handleMouseInput()
        if (ManagedGuiLib.isIsDisabled) return
        MouseHandler.handleMouseClick()
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)
        if (ManagedGuiLib.isIsDisabled) return

        if(ChatAllowedCharacters.isAllowedCharacter(typedChar))
            postEvent(KeyPressedEvent(typedChar + ""))
        postEvent(KeyPressedCodeEvent(Keyboard.getEventKey()))
    }


    @SubscribeEvent
    fun onRender(e: RenderGameOverlayEvent.Post) {
        if (ManagedGuiLib.isIsDisabled) return
        if (e.type != RenderGameOverlayEvent.ElementType.ALL || e.isCanceled) return
        try {

            RenderUtils.setPartialTicks(e.partialTicks)

            guiList.forEach { it.onRender(0, 0) }

        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }
    }

    @SubscribeEvent
    fun onOpenGui(e: GuiOpenEvent) {
        if (ManagedGuiLib.isIsDisabled) return
        try {
            guiList.forEach { it.onOpenGUI(Minecraft.getMinecraft().currentScreen) }
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if (ManagedGuiLib.isIsDisabled) return
        try {

            guiList.forEach { it.onChatMessage(e) }

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

    }



    //METHODS ----------------------------------------------------------------------------------------------------------

    fun addGUI(gui: GUI) {
        if (!guiList.contains(gui)) guiList.add(gui)
    }

    fun removeGUI(gui: GUI) {
        if (!guiList.contains(gui)) guiList.remove(gui)
    }


    private var tick: Short = 0
    private var sec: Short = 0
    private var min: Short = 0

    private fun processTimeEvents() {

        tick++

        if (tick % 2 == 0)  postEvent(TimeEvent(EnumTime.TICK_2))
        if (tick % 5 == 0)  postEvent(TimeEvent(EnumTime.TICK_5))
        if (tick % 10 == 0) postEvent(TimeEvent(EnumTime.TICK_10))
        if (tick % 15 == 0) postEvent(TimeEvent(EnumTime.TICK_15))

        if (tick >= 20) {
            tick = 0
            sec++

            postEvent(TimeEvent(EnumTime.SEC_1))

            if (sec % 2 == 0)  postEvent(TimeEvent(EnumTime.SEC_2))
            if (sec % 5 == 0)  postEvent(TimeEvent(EnumTime.SEC_5))
            if (sec % 10 == 0) postEvent(TimeEvent(EnumTime.SEC_10))
            if (sec % 15 == 0) postEvent(TimeEvent(EnumTime.SEC_15))
            if (sec % 30 == 0) postEvent(TimeEvent(EnumTime.SEC_30))


            if (sec >= 60) {
                sec = 0
                min++

                postEvent(TimeEvent(EnumTime.MIN_1))

                if (min % 2 == 0)  postEvent(TimeEvent(EnumTime.MIN_2))
                if (min % 2 == 0)  postEvent(TimeEvent(EnumTime.MIN_2))
                if (min % 5 == 0)  postEvent(TimeEvent(EnumTime.MIN_5))
                if (min % 10 == 0) postEvent(TimeEvent(EnumTime.MIN_10))
                if (min % 15 == 0) postEvent(TimeEvent(EnumTime.MIN_15))
                if (min % 20 == 0) postEvent(TimeEvent(EnumTime.MIN_20))
                if (min % 30 == 0) postEvent(TimeEvent(EnumTime.MIN_30))

                if (min >= 60) min = 0

            }
        }
    }

    fun <T : Event<*>> postEvent(e: T) {
        if (ManagedGuiLib.isIsDisabled) return
        try {

            getEventBus().forEach { it.invoke(e)}

            guiList.forEach { it.onEventBus(e) }

        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    fun onClick(clickType: MouseHandler.ClickType) {
        if (ManagedGuiLib.isIsDisabled) return
        try {
            guiList.forEach { it.onClick(clickType) }
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    fun onScroll(i: Int) {
        if (ManagedGuiLib.isIsDisabled) return
        try {
            guiList.forEach { it.onScroll(i) }
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    fun onMouseMove(mX: Int, mY: Int) {
        if (ManagedGuiLib.isIsDisabled) return
        try {
            guiList.forEach { it.onMouseMove(mX, mY) }
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

}
