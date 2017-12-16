/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.socket.io


enum class EnumSocketEvents (val eventName : String, val isServerEvent : Boolean){

    NULL("NULL", false),

    CREATE_CHAT("CREATE_CHAT", false),
    MESSAGE("MESSAGE", false),

    PRELOGIN("PRELOGIN", false),
    REGISTER("REGISTER", false),
    LOGIN("LOGIN", false);

    override fun toString() : String{
        return this.eventName
    }
}