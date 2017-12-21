/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.managedgui.lib.util

import eladkay.hudpixel.HudPixelMod


object LoggerHelperMG {

    private val LOGGER = HudPixelMod.instance().logger!!

    fun logInfo(s: String) {
        LOGGER.info("[ManagedGuiLib]" + s)
    }

    fun logWarn(s: String) {
        LOGGER.warn("[ManagedGuiLib]" + s)
    }

    fun logError(s: String) {
        LOGGER.error("[ManagedGuiLib]" + s)
    }

    fun logDebug(s: String) {
        LOGGER.debug("[ManagedGuiLib]" + s)
    }

}
