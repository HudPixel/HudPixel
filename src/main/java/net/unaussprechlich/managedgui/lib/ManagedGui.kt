package net.unaussprechlich.managedgui.lib

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.managedgui.lib.helper.ChildRegistry
import net.unaussprechlich.managedgui.lib.helper.MouseHandler

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
object ManagedGui {

    init {
        setup()
    }

    private fun setup() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onClientTick(e: TickEvent.ClientTickEvent) {
        try {
            MouseHandler.onClientTick()
            childRegistry.onClientTick()
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onRender(e: RenderGameOverlayEvent.Post) {
        try {
            childRegistry.onRender()
        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onOpenGui(e: GuiOpenEvent) {
        try {


        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        }

    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChatMessage(e: ClientChatReceivedEvent) {
        try {
            childRegistry.onChatMessage(e)

        } catch (ex: Exception) {
            //TODO print some nice debuging
            ex.printStackTrace()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

    }

        val childRegistry = ChildRegistry()


}
