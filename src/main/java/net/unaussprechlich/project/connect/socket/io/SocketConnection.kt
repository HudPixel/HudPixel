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

    var SESSION_TOKEN = ""

    private val SERVER_URL = "http://localhost:3000/HudPixel"
    var socket : Socket = IO.socket(SERVER_URL)

    fun isConnected() = SESSION_TOKEN != ""

    fun emit(event : EnumSocketEvents, args : Any){
        socket.emit(event.toString(), args)
    }

    fun emit(event : EnumSocketEvents, args : Any, ack : Ack){
        socket.emit(event.toString(), args, ack)
    }

    init {
        println("Setting up Socket.io")
        try {
            socket.on(Socket.EVENT_CONNECT) { _ ->
                try {
                    val obj = JSONObject()

                    obj.put("NAME", Minecraft.getMinecraft().thePlayer.name)
                    obj.put("UUID", Minecraft.getMinecraft().thePlayer.gameProfile.id.toString())
                    obj.put("VERSION", HudPixelMod.DEFAULT_VERSION.replace(" ", ""))

                    emit(EnumSocketEvents.PRELOGIN, obj, Ack { args ->
                        try {
                            val json = args[0] as JSONObject
                            if(!json.getBoolean("success")) throw Exception("Internal server error!")
                            ConnectAPI.showLogin()
                            LoginGUI.userExists = json.getBoolean("userExists")
                        } catch (e : Exception){
                            e.printStackTrace()
                        }
                    })

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            socket.on(Socket.EVENT_DISCONNECT) { _ -> }

            // Receiving an object
            socket.on("users_new_broadcast_message") { args ->
                val obj = args[0] as JSONObject
                try {
                    NotificationGUI.addNotification(DefNotificationContainer(obj.getString("msg"), "[Hud" + ChatFormatting.GOLD + "Pixel" + ChatFormatting.WHITE + "]", RGBA.RED.get(), 20))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            socket.connect()


        } catch (e1: URISyntaxException) {
            e1.printStackTrace()
        }

    }
}
