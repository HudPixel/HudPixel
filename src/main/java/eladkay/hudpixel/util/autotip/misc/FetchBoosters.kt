package eladkay.hudpixel.util.autotip.misc

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.event.Tipper
import eladkay.hudpixel.util.autotip.util.Hosts
import org.apache.commons.lang3.StringUtils
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import java.io.IOException
import java.util.*

// Gets boosters from the Autotip Network
class FetchBoosters : Runnable {

    override fun run() {
        val tipHost = Hosts.instance.getHostById("totip")
        if (tipHost.isEnabled) {
            try {
                val httpClient = HttpClientBuilder.create().build()
                val request = HttpPost(tipHost.url)
                request.addHeader("Content-Type", "application/x-www-form-urlencoded")
                request.entity = StringEntity(Autotip.mc.thePlayer.displayNameString + ":" + Autotip.alreadyTipped.joinToString(":"))
                val response = httpClient.execute(request)
                Collections.addAll(Tipper.tipQueue, *EntityUtils.toString(response.entity).split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                println("Fetched Boosters: " + StringUtils.join(Tipper.tipQueue, ", "))
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            Collections.addAll(Tipper.tipQueue, "all")
        }
    }
}
