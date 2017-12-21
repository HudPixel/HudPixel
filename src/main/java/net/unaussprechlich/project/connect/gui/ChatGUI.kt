/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.gui

import net.unaussprechlich.managedgui.lib.ManagedGui
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.gui.register
import net.unaussprechlich.project.connect.chatgui.ChatSetup
import net.unaussprechlich.project.connect.chatgui.ChatWrapper

object ChatGUI : GUI() {
    override fun getMode(): Mode {
        return Mode.NULL
    }

    init {
        this register ChatWrapper
        ChatSetup
        keepInCache = true
    }

    fun openChat(){
        ManagedGui.displayGUI(this)
    }

    fun closeChat(){
        ManagedGui.closeCurrentGUI()
    }
}