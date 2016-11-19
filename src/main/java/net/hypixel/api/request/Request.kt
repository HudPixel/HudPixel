package net.hypixel.api.request

import net.hypixel.api.HypixelAPI

class Request(val requestType: RequestType?, val params: Map<RequestParam, Any>) {

    fun getURL(hypixelAPI: HypixelAPI): String {
        var url = BASE_URL

        url += requestType?.key
        url += "?" + RequestParam.KEY.queryField + "=" + hypixelAPI.apiKey0

        for (entry in params.entries) {
            url += "&" + entry.key.queryField + "=" + entry.key.serialize(entry.value)
        }

        return url
    }

    companion object {

        private val BASE_URL = "https://api.hypixel.net/"
    }
}
