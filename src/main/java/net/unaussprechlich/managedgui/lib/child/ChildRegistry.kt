/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.managedgui.lib.child

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import java.util.*
import java.util.function.Consumer

abstract class ChildRegistry : IDoEventMethods {

    val childs = ArrayList<IChild>()

    fun changeChildPosition(pos : Int, child: IChild){
        var childPos = childs.indexOf(child)
        if(childPos > pos){
            while (childPos > pos){
                Collections.swap(childs, childPos, childPos -1)
                childPos--
            }
        }else if(childPos < pos){
            while (childPos < pos){
                Collections.swap(childs, childPos, childPos +1)
                childPos++
            }
        }
    }

    infix open fun <T : IChild> registerChild(child: T) {
        childs.add(child)
    }


    infix open fun unregisterChild(child: IChild) {
        childs.remove(child)
    }

    fun clearChilds() {
        childs.clear()
    }

    val childsSize: Int
        get() = childs.size

    fun containsChild(child: IChild): Boolean {
        return childs.contains(child)
    }

    fun onClientTick() {
        if (!doClientTick()) return
        if (childs.isEmpty()) return
        childs.forEach(Consumer<IChild> { it.onClientTick() })
    }

    fun onRender(xStart: Int, yStart: Int) {
        if (!doRender(xStart, yStart)) return
        if (childs.isEmpty()) return
        for (child in childs) {
            child.onRender(this.xStart, this.yStart)
        }
    }

    fun onChatMessage(e: ClientChatReceivedEvent) {
        if (!doClientTick()) return
        if (childs.isEmpty()) return
        childs.forEach { child -> child.onChatMessage(e) }
    }

    open fun onMouseMove(mX: Int, mY: Int) {
        if (!doMouseMove(mX, mY)) return
        if (childs.isEmpty()) return
        childs.forEach { child -> child.onMouseMove(mX, mY) }
    }

    fun onScroll(i: Int) {
        if (!doScroll(i)) return
        if (childs.isEmpty()) return
        childs.forEach { child -> child.onScroll(i) }
    }

    fun onClick(clickType: MouseHandler.ClickType) {
        if (!doClick(clickType)) return
        if (childs.isEmpty()) return
        childs.forEach { child -> child.onClick(clickType) }
    }

    fun <T : Event<*>> onEventBus(event: T) {
        if (!doEventBus(event)) return
        if (childs.isEmpty()) return
        childs.forEach { child -> child.onEventBus(event) }
    }

    fun onOpenGui(e: GuiOpenEvent) {
        if (!doOpenGUI(e)) return
        if (childs.isEmpty()) return
        childs.forEach { child -> child.onOpenGui(e) }
    }

    fun onResize() {
        if (!doResize()) return
        if (childs.isEmpty()) return
        childs.forEach(Consumer<IChild> { it.onResize() })
    }


}
