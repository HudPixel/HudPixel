/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.socket.io


enum class EnumSocketEvents (val eventName : String, val isServerEvent : Boolean){
    PRELOGIN("prelogin", false),
    CHATEVENT("chatevent", false),
    LOADCHATINFOS("loadchatinfos", false),

    //message
    LOADMESSAGE("loadmessage", false),
    SENDMESSAGE("sendmessage", false),



    LOGIN("login", false);

    override fun toString() : String{
        return this.eventName
    }
}