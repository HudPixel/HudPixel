package net.hypixel.example

import net.hypixel.api.HypixelAPI
import net.hypixel.api.reply.BoostersReply
import net.hypixel.api.request.RequestBuilder
import net.hypixel.api.request.RequestType
import net.hypixel.api.util.Callback
import java.util.*

object GetBoostersExample {
    @JvmStatic fun main(args: Array<String>) {
        HypixelAPI.apiKey0 = UUID.fromString("fd08635f-5848-4bd7-a9f6-771f5d93b1de")

        val request = RequestBuilder.newBuilder(RequestType.BOOSTERS).createRequest()
        println(request.getURL(HypixelAPI))
        HypixelAPI.getAsync(request, object : Callback<BoostersReply>(BoostersReply::class.java) {
            override fun callback(failCause: Throwable?, result: BoostersReply?) {
                if (failCause != null) {
                    failCause.printStackTrace()
                } else {
                    println(result)
                }
                HypixelAPI.finish()
                System.exit(0)
            }
        })
        ExampleUtil.await() // This is required because the API is asynchronous, so without this the program will exit.
    }
}