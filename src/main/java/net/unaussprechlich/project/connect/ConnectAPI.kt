/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect

import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.project.connect.gui.LoginGUI


object ConnectAPI {

    fun showLogin(){
        GuiManagerMG.addGUI("LoginGui", LoginGUI)
        GuiManagerMG.bindScreen()
    }
}