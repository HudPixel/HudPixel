
package net.unaussprechlich.managedgui.lib.event

import net.unaussprechlich.managedgui.lib.container.Container

typealias CallFun = (Container) -> Unit

class Callback{

    private val listeners = ArrayList<CallFun>()

    fun registerListener(f : CallFun){
        listeners.add(f)
    }

    fun unregisterListener(f : CallFun){
        listeners.remove(f)
    }

    fun broadcast(container: Container){
        listeners.forEach{it.invoke(container)}
    }
}

class PayloadCallback<T>{

    private val listeners = ArrayList<(T, Container) -> Unit>()

    fun registerListener(f : (T, Container) -> Unit){
        listeners.add(f)
    }

    fun unregisterListener(f : (T, Container) -> Unit){
        listeners.remove(f)
    }

    fun broadcast(payload : T, container: Container){
        listeners.forEach{it.invoke(payload, container)}
    }
}


