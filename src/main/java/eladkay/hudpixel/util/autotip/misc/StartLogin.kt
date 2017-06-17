package eladkay.hudpixel.util.autotip.misc

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.util.Hosts
import net.minecraft.client.Minecraft
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.net.URL

// Logins to the Autotip Network
class StartLogin : Runnable {

    override fun run() {
        try {
            Thread.sleep(5000)
            login()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    companion object {

        fun login() {
            val loginHost = Hosts.instance.getHostById("update")
            val downloadHost = Hosts.instance.getHostById("download")
            if (loginHost != null && loginHost.isEnabled) {
                try {
                    val ignored = IOUtils.toString(URL(String.format(
                            loginHost.url,
                            Minecraft.getMinecraft().thePlayer.uniqueID,
                            "2.0.3", // just because he asked
                            "1.8.9",
                            Autotip.totalTipsSent,
                            System.getProperty("os.name")
                    )))
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

}
