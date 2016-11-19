package net.unaussprechlich.managedgui.lib.helper

import net.minecraftforge.client.event.ClientChatReceivedEvent

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
interface Child {

    fun onClientTick()

    fun onRenderTick()

    fun onChatMessage(e: ClientChatReceivedEvent)

    fun onClick(clickType: MouseHandler.ClickType)

    fun onScroll(i: Int)

    fun onMouseMove(mX: Int, mY: Int)

}
