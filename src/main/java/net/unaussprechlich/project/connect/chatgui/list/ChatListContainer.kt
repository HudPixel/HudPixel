/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.project.connect.chatgui.list

import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefScrollableContainer
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.project.connect.chatgui.ChatWrapper


object ChatListContainer : DefScrollableContainer(RGBA.C_161b21.get(), ChatWrapper.stdChatListWidth, ChatWrapper.stdHeight, null ){

    init {
        isResizeable = false
        hasScrollbar = false
        isAlignTop = true
        hasTopFade = false
    }

    fun addChatListElement(elementContainer: ChatListElementContainer){
        registerScrollElement(elementContainer)
    }

    fun addChatListElement(elementContainer: ChatListElementContainer, category: ChatListCategoryContainer){
        category.registerElement(elementContainer)
    }
}