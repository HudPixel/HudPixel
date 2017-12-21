/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
package net.unaussprechlich.project.connect.socket.io

import com.mojang.realmsclient.gui.ChatFormatting
import eladkay.hudpixel.HudPixelMod
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import net.minecraft.client.Minecraft
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefNotificationContainer
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.project.connect.ConnectAPI
import net.unaussprechlich.project.connect.gui.LoginGUI
import net.unaussprechlich.project.connect.gui.NotificationGUI
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

/**
 * SocketConnection Created by Alexander on 23.02.2017.
 * Description:
 */
object SocketConnection{

    val CONNECT_IO_VERSION = "0.1"

    var sessionToken = ""
        get() {
            if(field == "" && isSucessfullPrelogin)
                ConnectAPI.showLogin()
            return field
        }

    var userID : Long = 0

    private var isSucessfullPrelogin = false

    private val SERVER_URL = "http://localhost:3000/connectio"
    val socket : Socket = IO.socket(SERVER_URL)

    fun isConnected() = userID != 0L && sessionToken != ""

    fun emit(event : EnumSocketEvents, args : Any){
        socket.emit(event.toString(), args)
    }

    fun emit(event : EnumSocketEvents, args : Any, ack : Ack){
        socket.emit(event.toString(), args, ack)
    }

    init {
        try {
            socket.on(Socket.EVENT_CONNECT) { _ ->
                try {
                    val obj = JSONObject()

                    obj.put("NAME", Minecraft.getMinecraft().thePlayer.name)
                    obj.put("USER_ID", "")
                    obj.put("VERSION", CONNECT_IO_VERSION)
                    obj.put("UUID", Minecraft.getMinecraft().thePlayer.uniqueID.toString())

                    socket.emit(EnumSocketEvents.PRELOGIN.toString(), obj, Ack { args ->
                        try {
                            NotificationGUI.addNotification(DefNotificationContainer("You can now Login.", "Connected to HudPixel", RGBA.GOLD_MC.get(), 10))
                            val json = args[0] as JSONObject
                            if(!json.getBoolean("success")) throw Exception("Internal server error!")
                            isSucessfullPrelogin = true
                            ConnectAPI.showLogin()

                            if( json.has("userExists") && json.get("userExists") != null){
                                LoginGUI.userExists = true
                            }

                        } catch (e : Exception){
                            e.printStackTrace()
                        }
                    })

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            socket.on(Socket.EVENT_DISCONNECT) { _ -> println("DISCONNECTED")}

            socket.connect()


        } catch (e1: URISyntaxException) {
            e1.printStackTrace()
        }

    }
}
