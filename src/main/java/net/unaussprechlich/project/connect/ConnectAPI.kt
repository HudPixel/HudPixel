/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect

import net.unaussprechlich.managedgui.lib.ManagedGui
import net.unaussprechlich.project.connect.gui.LoginGUI


object ConnectAPI {

    fun showLogin(){
        ManagedGui.displayGUI(LoginGUI)
    }
}