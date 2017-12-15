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
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedCodeEvent
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedEvent
import net.unaussprechlich.managedgui.lib.event.events.ScreenResizeEvent
import net.unaussprechlich.managedgui.lib.event.events.TimeEvent
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.exceptions.NameInUseException
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.LoggerHelperMG
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import org.lwjgl.input.Keyboard
import java.util.*

/**
 * GuiManagerMG Created by unaussprechlich on 18.12.2016.
 * Description:
 */
object GuiManagerMG : GuiScreen() {

    //DEFAULT TYPED ----------------------------------------------------------------------------------------------------

    enum class EnumGUITypes {
        TEST, CHAT, ESC, INGAME
    }

    //EVENT_HANDLING ---------------------------------------------------------------------------------------------------
    private var prevWidth = DisplayUtil.scaledMcWidth
    private var prevHeight = DisplayUtil.scaledMcHeight
    private val GUIs = HashMap<EnumGUITypes, GUI>()
    private val GUIsDynamic = HashMap<String, GUI>()

    private val eventBusCallbacks = ArrayList<(T : Event<*>) -> Unit>()

    private fun getEventBus() :ArrayList<(T : Event<*>) -> Unit> {
        return ArrayList(eventBusCallbacks)
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        isBinded = false
    }


    fun registerEventBusCallback(callback : (T : Event<*>) -> Unit){
        eventBusCallbacks.add(callback)
    }

    fun unregisterEventBusCallback(callback : (T : Event<*>) -> Unit){
        eventBusCallbacks.remove(callback)
    }

    private var isBinded = false
    fun bindScreen() {
        isBinded = true
        Minecraft.getMinecraft().displayGuiScreen(this)
    }

    fun unbindScreen(){

        onGuiClosed()

        Minecraft.getMinecraft().displayGuiScreen(null)
        if (Minecraft.getMinecraft().currentScreen == null)
            Minecraft.getMinecraft().setIngameFocus()
    }

    @SubscribeEvent
    fun onClientTick(e: TickEvent.ClientTickEvent) {
        if (ManagedGui.isIsDisabled) return

        if(isBinded && this.mc.currentScreen != this){
            bindScreen()
        }

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

            GUIs.values.forEach(GUI::onClientTick)
            GUIsDynamic.values.forEach(GUI::onClientTick)

        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    init {
        LoggerHelperMG.logInfo("Setting up GuiManager!")
    }

    override fun handleMouseInput() {
        if (ManagedGui.isIsDisabled) return
        MouseHandler.handleMouseClick()
    }

    var lastKeyAction = System.currentTimeMillis()
    val delay = 20
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (ManagedGui.isIsDisabled) return
        if(System.currentTimeMillis() < lastKeyAction + delay) return
        lastKeyAction = System.currentTimeMillis()
        super.keyTyped(typedChar, keyCode)
        if(ChatAllowedCharacters.isAllowedCharacter(typedChar))
            postEvent(KeyPressedEvent(typedChar + ""))
        postEvent(KeyPressedCodeEvent(Keyboard.getEventKey()))
    }

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent){
        if (ManagedGui.isIsDisabled) return
        if(System.currentTimeMillis() < lastKeyAction + delay) return
        lastKeyAction = System.currentTimeMillis()
        val c = (if (Keyboard.getEventKey() == 0) Keyboard.getEventCharacter() + 256 else Keyboard.getEventCharacter())

        postEvent(KeyPressedCodeEvent(Keyboard.getEventKey()))
        if(ChatAllowedCharacters.isAllowedCharacter(c))
            postEvent(KeyPressedEvent(c.toString()))
    }

    @SubscribeEvent
    fun onRender(e: RenderGameOverlayEvent.Post) {
        if (ManagedGui.isIsDisabled) return
        if (e.type != RenderGameOverlayEvent.ElementType.ALL || e.isCanceled) return
        try {

            RenderUtils.setPartialTicks(e.partialTicks)
            GUIs.values.forEach { gui -> gui.onRender(0, 0) }
            GUIsDynamic.values.forEach { gui -> gui.onRender(0, 0) }

        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onOpenGui(e: GuiOpenEvent) {
        if (ManagedGui.isIsDisabled) return
        try {

            GUIs.values.forEach { gui -> gui.onOpenGui(e) }
            GUIsDynamic.values.forEach { gui -> gui.onOpenGui(e) }

        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if (ManagedGui.isIsDisabled) return
        try {

            GUIs.values.forEach { gui -> gui.onChatMessage(e) }
            GUIsDynamic.values.forEach { gui -> gui.onChatMessage(e) }

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

    }



    //METHODS ----------------------------------------------------------------------------------------------------------

    fun <T : GUI> addGUI(name: String, GUI: T) {
        if (GUIsDynamic.containsKey(name)) {
            throw NameInUseException(name, "GuiManagerMG")
        }
        GUIsDynamic.put(name, GUI)
    }

    fun removeGUI(name: String) {
        if (GUIsDynamic.containsKey(name)) GUIsDynamic.remove(name)
        else throw Exception("There is not GUI called $name!")
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
        if (ManagedGui.isIsDisabled) return
        try {

            getEventBus().forEach { it.invoke(e)}
            GUIs.values.forEach { gui -> gui.onEventBus(e) }
            GUIsDynamic.values.forEach { gui -> gui.onEventBus(e) }

        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    fun onClick(clickType: MouseHandler.ClickType) {
        if (ManagedGui.isIsDisabled) return
        try {
            GUIs.values.forEach { gui -> gui.onClick(clickType) }
            GUIsDynamic.values.forEach { gui -> gui.onClick(clickType) }
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    fun onScroll(i: Int) {
        if (ManagedGui.isIsDisabled) return
        try {
            GUIs.values.forEach { gui -> gui.onScroll(i) }
            GUIsDynamic.values.forEach { gui -> gui.onScroll(i) }
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    fun onMouseMove(mX: Int, mY: Int) {
        if (ManagedGui.isIsDisabled) return
        try {
            GUIs.values.forEach { gui -> gui.onMouseMove(mX, mY) }
            GUIsDynamic.values.forEach { gui -> gui.onMouseMove(mX, mY) }
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

}
