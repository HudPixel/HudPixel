/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.managedgui.lib

import net.minecraftforge.common.MinecraftForge
import net.unaussprechlich.managedgui.lib.helper.SetupHelper
import net.unaussprechlich.managedgui.lib.util.LoggerHelperMG


object ManagedGuiLib {

    var isIsDisabled = true
    var iSetupHelper: SetupHelper? = null
        private set

    fun setup(ISetupHelper: SetupHelper) {
        LoggerHelperMG.logInfo("Setting up ManagedGuiLib!")
        ManagedGuiLib.iSetupHelper = ISetupHelper
        ManagedGui
        MinecraftForge.EVENT_BUS.register(ManagedGui)

    }

    var MODID = ""
        get() { return iSetupHelper!!.getMODID() }

}
