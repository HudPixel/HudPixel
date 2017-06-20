package net.unaussprechlich.project.connect.socket.io


enum class EnumSocketEvents (val eventName : String){
    PRELOGIN("prelogin"),
    CHATEVENT("chatevent"),
    LOADCHATINFOS("loadchatinfos"),
    LOGIN("login");

    override fun toString() : String{
        return this.eventName
    }
}