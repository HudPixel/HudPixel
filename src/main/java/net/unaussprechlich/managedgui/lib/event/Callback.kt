
/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.managedgui.lib.event

import net.unaussprechlich.managedgui.lib.container.Container

infix fun Callback.broadcast(container: Container) = this.broadcast(container)
infix fun Callback.register(f: CallFun)   = this.registerListener(f)
infix fun Callback.unregister(f: CallFun) = this.unregisterListener(f)

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

infix fun <T> PayloadCallback<T>.register(f: PayloadFun<T>)   = this.registerListener(f)
infix fun <T> PayloadCallback<T>.unregister(f: PayloadFun<T>) = this.unregisterListener(f)

typealias PayloadFun<T> = (T, Container) -> Unit

class PayloadCallback<T>{

    private val listeners = ArrayList<PayloadFun<T>>()

    fun registerListener(f : PayloadFun<T>){
        listeners.add(f)
    }

    fun unregisterListener(f : PayloadFun<T>){
        listeners.remove(f)
    }

    fun broadcast(payload : T, container: Container){
        listeners.forEach{it.invoke(payload, container)}
    }
}


