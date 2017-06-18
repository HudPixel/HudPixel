/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.databases.player.PlayerModel
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.helper.DateHelper
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.storage.ContainerSide

/**
 * DefChatMessageContainer Created by Alexander on 27.02.2017.
 * Description:
 */
class DefChatMessageContainer(private val player: PlayerModel, message: String , private val date: DateHelper = DateHelper(), width: Int = 100) : Container() {

    private companion object {
        val SPACE = 4
    }

    private var message_con: DefTextListAutoLineBreakContainer? = null
    private val username_con: DefTextContainer = DefTextContainer(player.rankName + ChatFormatting.GRAY + ChatFormatting.ITALIC + "  " + date.dateTimeTextPassed)
    private val avatar_con = DefPictureContainer(player.playerHead)




    init {
        setWidth(width)
        setup(message)
        player.loadPlayerHead {avatar_con.setBackgroundImage(it)}
    }


    val playername: String
        get() = player.name

    private fun setup(message: String) {
        avatar_con.apply {
            setMargin(SPACE)
            width = 18
            height = 18
            yOffset = 1
        }
        username_con.margin = ContainerSide().BOTTOM(4).TOP(4)
        username_con.xOffset = avatar_con.widthMargin
        message_con = DefTextListAutoLineBreakContainer(message, width - avatar_con.widthMargin - SPACE - 14) { update() }
        message_con!!.setXYOffset(avatar_con.widthMargin, username_con.heightMargin + 2)

        registerChild(message_con!!)
        registerChild(avatar_con)
        registerChild(username_con)

        update()
    }

    fun addMessage(s: String) {
        message_con!!.addEntry(s)
        update()
    }

    private fun update() {
        super.setHeight(SPACE * 2 + username_con.heightMargin + message_con!!.heightMargin)
        message_con!!.width = width - avatar_con.widthMargin - SPACE - 14
        heightChangedCallback.broadcast(this)
    }

    override fun setHeight(height: Int) {
        throw UnsupportedOperationException("[ManagedGuiLib][DefMessageContainer] setHeight() is handled automatically use setPadding() instead!")
    }

    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        return true
    }

    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean {
        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean {
        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        if (iEvent.id != EnumDefaultEvents.TIME.get()) return true
        if (iEvent.data == EnumTime.SEC_5) {
            username_con.text = player.rankName + ChatFormatting.GRAY + ChatFormatting.ITALIC + "  " + date.dateTimeTextPassed
        }
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        update()
        return true
    }

}
