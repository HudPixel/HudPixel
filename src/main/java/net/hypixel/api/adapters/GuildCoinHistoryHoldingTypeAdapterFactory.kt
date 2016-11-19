package net.hypixel.api.adapters

import com.google.common.collect.Lists
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.hypixel.api.reply.GuildReply
import net.hypixel.api.util.APIUtil
import java.util.*
import java.util.function.Consumer

class GuildCoinHistoryHoldingTypeAdapterFactory<T : GuildReply.GuildCoinHistoryHolding>(customizedClass: Class<T>) : CustomizedTypeAdapterFactory<T>(customizedClass) {

    override fun afterRead(json: JsonElement) {
        val obj = json.asJsonObject

        // parse the coin history
        val history = GuildReply.Guild.GuildCoinHistory()
        val toRemove = Lists.newArrayList<String>()
        json.asJsonObject.entrySet().forEach { entry ->
            if (entry.key.startsWith("dailyCoins")) {
                val split = entry.key.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val day = Integer.parseInt(split[1])
                val month = Integer.parseInt(split[2])
                val year = Integer.parseInt(split[3])

                val c = Calendar.getInstance()
                c.set(year, month, day, 0, 0)
                history.coinHistory.put(APIUtil.getDateTime(c.time.time), entry.value.asInt)
                toRemove.add(entry.key)
            }
        }

        // remove dailyCoins-%d-%d-%d from the original object
        toRemove.forEach(Consumer<String> { obj.remove(it) })

        val coinHistory = JsonObject()
        // insert as millisecond string so we can use our standard milli -> datetime conversion
        history.coinHistory.entries.forEach { entry -> coinHistory.addProperty(entry.key.millis.toString(), entry.value) }

        // load into the json
        obj.add("guildCoinHistory", coinHistory)
    }
}
