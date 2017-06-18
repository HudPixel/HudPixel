package net.unaussprechlich.project.connect.chat

import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefScrollableContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefWrapperContainer
import net.unaussprechlich.managedgui.lib.util.RGBA


object newChatListContainer : DefScrollableContainer(RGBA.C_161b21.get(), ChatWrapper.stdChatListWidth, ChatWrapper.stdHeight, null ){

    val friendsCate     = newChatListCategoryContainer("Friends", RGBA.PURPLE_LIGHT_MC.get())
    val groupsCate      = newChatListCategoryContainer("Groups", RGBA.YELLOW_MC.get())
    val gamemodesCate   = newChatListCategoryContainer("Gamemodes", RGBA.RED.get())
    val favoriteCate    = newChatListCategoryContainer("Favorite *", RGBA.GREEN.get())

    val guildElement    = newChatListElementContainer().apply {
        title = "GuildChat"
        color = RGBA.GREEN.get()
    }

    val partyElement    = newChatListElementContainer().apply {
        title = "PartyChat"
        color = RGBA.BLUE_MC.get()
    }

    init {
        isResizeable = false
        hasScrollbar = false
        isAlignTop = true
        hasTopFade = false

        registerScrollElement(guildElement)
        registerScrollElement(DefWrapperContainer().apply { height = 1 })
        registerScrollElement(partyElement)
        registerScrollElement(favoriteCate)
        registerScrollElement(groupsCate)
        registerScrollElement(friendsCate)
        registerScrollElement(gamemodesCate)

    }

    var activeElement : newChatListElementContainer? = null
    fun activateElement(con : Container){
        if(activeElement != null) activeElement!!.isActive = false
        activeElement = con as newChatListElementContainer
        println("ACTIVATED :D")
    }

    fun addChatListElement(elementContainer: newChatListElementContainer){
        registerScrollElement(elementContainer)
    }

    fun addChatListElement(elementContainer: newChatListElementContainer, category: newChatListCategoryContainer){
        category.registerElement(elementContainer)
    }

    override fun registerScrollElement(container: Container) {
        super.registerScrollElement(container)
        if(container is newChatListElementContainer)
            container.activateCallback.registerListener { activateElement(it) }
        else if(container is newChatListCategoryContainer)
            container.activateCallback.registerListener { activateElement(it) }
    }

}