package net.unaussprechlich.managedgui.lib.helper

import net.minecraftforge.client.event.ClientChatReceivedEvent

import java.util.ArrayList

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
class ChildRegistry {

    val childs = ArrayList<Child>()

    fun registerChild(child: Child) {
        childs.add(child)
    }

    fun unregisterChild(child: Child) {
        childs.remove(child)
    }

    fun clearChilds() {
        childs.clear()
    }

    val childsSize: Int
        get() = childs.size

    fun containsChild(child: Child): Boolean {
        return childs.contains(child)
    }

    fun onClientTick() {
        for (child in childs) {
            child.onClientTick()
        }
    }

    fun onRender() {
        for (child in childs) {
            child.onRenderTick()
        }
    }


    fun onChatMessage(e: ClientChatReceivedEvent) {
        for (child in childs) {
            child.onChatMessage(e)
        }
    }

    fun onMouseMove(mX: Int, mY: Int) {
        for (child in childs) {
            child.onMouseMove(mX, mY)
        }
    }

    fun onScroll(i: Int) {
        for (child in childs) {
            child.onScroll(i)
        }
    }

    fun onClick(clickType: MouseHandler.ClickType) {
        for (child in childs) {
            child.onClick(clickType)
        }
    }
}
