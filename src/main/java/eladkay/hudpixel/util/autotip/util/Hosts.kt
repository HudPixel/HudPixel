package eladkay.hudpixel.util.autotip.util

import com.google.gson.GsonBuilder
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.net.URL

// Simple data class to keep track of host info
class Host(val id: String, val url: String, val isEnabled: Boolean)
class Hosts private constructor() {

    private val hosts: List<Host>? = null

    fun getHostById(id: String): Host {
        return hosts!!.stream()
                .filter { h -> h.id == id }
                .findFirst()
                .orElse(null)
    }

    companion object {

        lateinit var instance: Hosts

        fun updateHosts() {
            val gson = GsonBuilder().setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create()
            try {
                val json = IOUtils.toString(
                        URL("https://gist.githubusercontent.com/Semx11/35d6b58783ef8d0527f82782f6555834/raw/hosts.json"))
                instance = gson.fromJson(json, Hosts::class.java)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}
