/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.socket.io.subscriptions



import net.unaussprechlich.project.connect.chatgui.chat.Message
import java.util.*

//######################################################################################################################

open class Subscription<T>{
    protected val subscribers = ArrayList<T>()
    fun subscribe(subscriber : T) = subscribers.add(subscriber)
    fun unsubscribe(subscriber : T) = subscribers.remove(subscriber)
}

infix fun <A> Subscription<A>.subscribe(subscriber : A) = this.subscribe(subscriber)
infix fun <A> Subscription<A>.unsubscribe(subscriber : A) = this.unsubscribe(subscriber)


//######################################################################################################################

object ChatMessageSubscribtion : Subscription<(UUID, Message) -> Unit>() {

    fun broadcast(channelID : UUID, msg : Message){
        subscribers.forEach { it.invoke(channelID, msg) }
    }

}

//######################################################################################################################