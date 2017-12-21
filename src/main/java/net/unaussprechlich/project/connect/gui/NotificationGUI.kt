/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.gui


import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.events.ScreenResizeEvent
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefNotificationContainer
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import java.util.*

/**
 * NotificationGUI Created by Alexander on 23.02.2017.
 * Description:
 */
object NotificationGUI : GUI() {

    override fun getMode(): Mode {
        return Mode.ALWAYS
    }

    private val notifications = ArrayList<DefNotificationContainer>()

    init {
        xStart = DisplayUtil.scaledMcWidth - 200 - 10
        yStart = DisplayUtil.scaledMcHeight - 10
    }

    fun addNotification(notify: DefNotificationContainer) {
        notifications.add(notify)
        registerChild(notify)
        updatePositions()
    }

    private fun updatePositions() {
        var yPos = 0
        for (notify in notifications) {
            notify.yOffset = notify.height + yPos
            yPos += notify.height + SPACING
        }
    }

    override fun <T : Event<*>> doEventBus(event: T): Boolean {
        if(isVisible){
            if (event.id == EnumDefaultEvents.TIME.get() && event.data === EnumTime.SEC_1) {
                notifications.removeAll(notifications.filter({ cont -> cont.showtime_sec <= 0 }))
                updatePositions()
            } else if(event is ScreenResizeEvent){
                xStart = DisplayUtil.scaledMcWidth - 200 - 10
                yStart = DisplayUtil.scaledMcHeight - 10
                updatePositions()
            }
        }
        return super.doEventBus(event)
    }

    private val SPACING = 1

}
