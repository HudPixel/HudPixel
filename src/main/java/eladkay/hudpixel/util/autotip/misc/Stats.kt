package eladkay.hudpixel.util.autotip.misc

import eladkay.hudpixel.util.autotip.Autotip
import eladkay.hudpixel.util.autotip.util.ChatColor
import eladkay.hudpixel.util.autotip.util.ClientMessage
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

object Stats {

    private var upgradeDate: LocalDate? = null

    fun setUpgradeDate(date: LocalDate) {
        upgradeDate = date
    }

    fun printBetween(s: String, e: String) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        var start = LocalDate.parse(s, formatter)
        val end = LocalDate.parse(e, formatter)

        val totalDates = ArrayList<String>()
        while (!start.isAfter(end)) {
            totalDates.add(start.format(formatter))
            start = start.plusDays(1)
        }
        printStats(*totalDates.toTypedArray())
    }

    @JvmStatic fun printStats(vararg days: String) {

        val totalStats = HashMap<String, Int>()
        val sentStats = HashMap<String, Int>()
        val receivedStats = HashMap<String, Int>()

        val xp = intArrayOf(0, 0)
        val tips = intArrayOf(0, 0)

        for (date in days) {
            val f = File(Autotip.USER_DIR + "stats" + File.separator + date + ".at")
            if (!f.exists()) {
                continue
            }

            val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val oldTips = localDate.isBefore(LocalDate.of(2016, 11, 29))
            val fixTips = localDate.isAfter(LocalDate.of(2016, 11, 29)) && localDate
                    .isBefore(upgradeDate!!)

            val dailyStats = getDailyStats(f)

            dailyStats[0].forEach { game, coins ->
                if (game == "tips") {
                    xp[0] += 50 * coins
                    tips[0] += coins
                } else {
                    (totalStats as java.util.Map<String, Int>).merge(game, coins) { a, b -> a + b }
                    (sentStats as java.util.Map<String, Int>).merge(game, coins) { a, b -> a + b }
                }
            }
            dailyStats[1].forEach { game, coins ->
                if (game == "tips") {
                    var coins = coins
                    if (fixTips) {
                        coins /= 2
                    }
                    xp[1] += (if (oldTips) 30 else 60) * coins
                    tips[1] += coins
                } else {
                    (totalStats as java.util.Map<String, Int>).merge(game, coins) { a, b -> a + b }
                    (receivedStats as java.util.Map<String, Int>).merge(game, coins) { a, b -> a + b }
                }
            }
        }

        var karma = 0
        if (sentStats.containsKey("karma")) {
            karma = sentStats["karma"]!!
            sentStats.remove("karma")
        }

        val games = totalStats.entries.stream()
                .sorted { e1, e2 -> e2.value.compareTo(e1.value) }
                .map { it.key }
                .collect(Collectors.toList<String>())

        if (!games.isEmpty()) {
            ClientMessage.separator()
            games.forEach { game ->
                val sentCoins = if (sentStats.containsKey(game)) sentStats[game] else 0
                val receivedCoins = if (receivedStats.containsKey(game)) receivedStats[game] else 0
                if (sentStats.containsKey(game) || receivedStats.containsKey(game)) {
                    ClientMessage.send(
                            String.format("%s%s: %s%s coins",
                                    ChatColor.GREEN, game,
                                    ChatColor.YELLOW, format((sentCoins!!.toString() + receivedCoins!!.toString()).toInt()),
                            "",
                            String.format(
                                    "%s%s\n%sBy sending: %s%s coins\n%sBy receiving: %s%s coins",
                                    ChatColor.GREEN, game,
                                    ChatColor.RED,
                                    ChatColor.YELLOW, format(sentCoins!!),
                                    ChatColor.BLUE,
                                    ChatColor.YELLOW, format(receivedCoins!!))
                    ))
                }
            }
            ClientMessage.send(
                    String.format("%sTips: %s", ChatColor.GOLD, format(tips[0] + tips[1])), "",
                    String.format("%sSent: %s%s tips\n%sReceived: %s%s tips",
                            ChatColor.RED,
                            ChatColor.GOLD, format(tips[0]),
                            ChatColor.BLUE,
                            ChatColor.GOLD, format(tips[1]))
            )
            ClientMessage.send(
                    String.format("%sXP: %s", ChatColor.BLUE, format(xp[0] + xp[1]))
            )
            ClientMessage.send(                    String.format("%sBy sending: %s%s XP\n%sBy receiving: %s XP",
                    ChatColor.RED,
                    ChatColor.BLUE, format(xp[0]),
                    ChatColor.BLUE, format(xp[1])))

            ClientMessage.send(String.format("Stats from %s%s",
                    days[0].replace("-", "/"),
                    if (days.size > 1) " - " + days[days.size - 1].replace("-", "/") else ""
            ))
            ClientMessage.separator()
        } else {
            ClientMessage.send(ChatColor.RED.toString() + "You have never tipped someone in this period!")
            ClientMessage.send(String.format("(%s%s)",
                    days[0].replace("-", "/"),
                    if (days.size > 1) " - " + days[days.size - 1].replace("-", "/") else ""
            ))
        }
    }

    private fun getDailyStats(file: File): List<Map<String, Int>> {
        try {
            val sentStats = HashMap<String, Int>()
            val receivedStats = HashMap<String, Int>()
            BufferedReader(FileReader(file.path)).use { statsReader ->
                val lines = statsReader.lines().collect(Collectors.toList<String>())
                if (lines.size >= 2) {
                    val tipStats = lines[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    sentStats.put("tips", Integer.parseInt(tipStats[0]))
                    sentStats.put("karma", Integer.parseInt(lines[1]))

                    receivedStats
                            .put("tips", if (tipStats.size > 1) Integer.parseInt(tipStats[1]) else 0)

                    lines.stream().skip(2).forEach { line ->
                        val stats = line.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (stats[1] != "0") {
                            sentStats.put(stats[0], Integer.parseInt(stats[1]))
                        }
                        if (stats.size > 2 && stats[2] != "0") {
                            receivedStats.put(stats[0], Integer.parseInt(stats[2]))
                        }
                    }
                }
            }
            return Arrays.asList<Map<String, Int>>(sentStats, receivedStats)
        } catch (e: IOException) {
            e.printStackTrace()
            return Arrays.asList(emptyMap<String, Int>(), emptyMap<String, Int>())
        }

    }

    private fun format(number: Int): String {
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        return formatter.format(number.toLong())
    }

}