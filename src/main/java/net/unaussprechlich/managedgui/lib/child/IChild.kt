/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.managedgui.lib.child

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler

interface IChild {

    fun onClientTick()

    fun onRender(xStart: Int, yStart: Int)

    fun onChatMessage(e: ClientChatReceivedEvent)

    fun onClick(clickType: MouseHandler.ClickType)

    fun onScroll(i: Int)

    fun onMouseMove(mX: Int, mY: Int)

    fun <T : Event<*>> onEventBus(event: T)

    fun onOpenGui(event: GuiOpenEvent)

    fun onResize()

}
