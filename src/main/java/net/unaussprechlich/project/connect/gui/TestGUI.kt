/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.gui

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler

object TestGUI : GUI(){

    override fun getMode(): Mode {
        return Mode.ALWAYS
    }

    init {

    }

    fun update(){

    }

    override fun doClientTick(): Boolean { update() ; return true }

}


