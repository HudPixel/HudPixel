package net.hypixel.api

import com.google.common.base.Preconditions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.hypixel.api.adapters.*
import net.hypixel.api.exceptions.APIThrottleException
import net.hypixel.api.exceptions.HypixelAPIException
import net.hypixel.api.reply.AbstractReply
import net.hypixel.api.reply.GuildReply
import net.hypixel.api.request.Request
import net.hypixel.api.util.Callback
import net.hypixel.api.util.GameType
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.joda.time.DateTime
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.net.ssl.HttpsURLConnection

@SuppressWarnings("unused")
object HypixelAPI {
    private val gson: Gson
    private val lock: ReentrantReadWriteLock
    private val exService = Executors.newCachedThreadPool()
    private val httpClient: HttpClient
    /**
     * @return currently set API key
     */
    /**
     * Call this method to set the API key

     * @param apiKey The API key to set
     */
    var apiKey0: UUID? = null
    fun setApiKey(uuid: UUID?) {
        Preconditions.checkNotNull<UUID>(uuid)
        lock.writeLock().lock()
        try {
            this.apiKey0 = apiKey0
        } finally {
            lock.writeLock().unlock()
        }
    }

    init {
        gson = GsonBuilder().registerTypeAdapter(UUID::class.java, UUIDTypeAdapter()).registerTypeAdapter(GameType::class.java, GameTypeTypeAdapter()).registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter()).registerTypeAdapter(GuildReply.Guild.GuildCoinHistory::class.java, GuildCoinHistoryAdapter())// guilds
                .registerTypeAdapterFactory(GuildCoinHistoryHoldingTypeAdapterFactory(GuildReply.Guild::class.java)).registerTypeAdapterFactory(GuildCoinHistoryHoldingTypeAdapterFactory(GuildReply.Guild.Member::class.java)).create()
        lock = ReentrantReadWriteLock()
        httpClient = HttpClientBuilder.create().build()
    }


    private fun <T : AbstractReply> getResponse(callback: Callback<T>, request: Request) {
        Thread {
            try {
                val u = URL(request.getURL(HypixelAPI))
                val con = u.openConnection() as HttpsURLConnection
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                val br = BufferedReader(InputStreamReader(con.inputStream))

                val sBuilder = StringBuilder()
                var buff: String? = ""
                buff = br.readLine()
                while (buff != null) {
                    sBuilder.append(buff)
                    buff = br.readLine()
                }

                val value = gson.fromJson(sBuilder.toString(), callback.clazz)
                checkReply(value)
                callback.callback(null, value)

            } catch (e: IOException) {
                e.printStackTrace()
            }


        }.start()
        val value: T
        try {

        } catch (t: Throwable) {
            callback.callback(t, null)
        }

    }

    /**
     * Call this method when you're finished requesting anything from the API.
     * The API maintains it's own internal threadpool, so if you don't call this
     * the application will never exit.
     */
    fun finish() {
        try {
            exService.shutdown()
        } catch (e: Exception) {
            throw HypixelAPIException(e)
        }

    }

    /**
     * Use this method to pull data off of the Public API

     * @param request Request object to pull from API
     * *
     * @param      The class of the reply
     * *
     * @throws HypixelAPIException
     */
    @Throws(HypixelAPIException::class)
    fun <R : AbstractReply> getSync(request: Request): R? {
        lock.readLock().lock()
        val callback = SyncCallback<R>(request.requestType?.replyClass)
        try {
            if (doKeyCheck(callback)) {
                val future = get(request, callback)
                future.get()
            }
        } catch (e: InterruptedException) {
            callback.callback(e, null)
        } catch (e: ExecutionException) {
            callback.callback(e, null)
        } finally {
            lock.readLock().unlock()
        }
        if (callback.failCause != null) {
            throw HypixelAPIException(callback.failCause!!)
        } else {
            return callback.result
        }
    }

    /**
     * Execute Request asynchronously, executes Callback when finished

     * @param request  Request to get
     * *
     * @param callback Callback to execute
     * *
     * @param       Class of the reply
     */
    fun <R : AbstractReply> getAsync(request: Request, callback: Callback<R>) {
        lock.readLock().lock()
        try {
            if (doKeyCheck(callback)) {
                getResponse(callback, request)
                //get(request, callback); hey jo @Plancke fix your stuff :3
            }
        } finally {
            lock.readLock().unlock()
        }
    }

    /**
     * Internal method

     * @param callback The callback to fail to
     * *
     * @return True if we should continue
     */
    private fun doKeyCheck(callback: Callback<*>): Boolean {
        if (apiKey0 == null) {
            callback.callback(HypixelAPIException("API key hasn't been set yet!"), null)
            return false
        } else {
            return true
        }
    }

    /**
     * Internal method

     * @param callback The callback to execute
     * *
     * @param       The class of the callback
     * *
     * @return The ResponseHandler that wraps the callback
     */
    private fun <T : AbstractReply> buildResponseHandler(callback: Callback<T>): ResponseHandler<HttpResponse> {
        return ResponseHandler<HttpResponse>{ obj ->
            val value: T
            try {
                val content = EntityUtils.toString(obj.getEntity(), "UTF-8")
                value = gson.fromJson(content, callback.clazz)

                checkReply(value)
            } catch (t: Throwable) {
                callback.callback(t, null)
                return@ResponseHandler  obj
            }

            callback.callback(null, value)
            obj
        }
    }

    /**
     * Checks reply and throws appropriate exceptions based on it's content

     * @param reply The reply to check
     * *
     * @param    The class of the reply
     */
    fun <T : AbstractReply> checkReply(reply: T?) {
        if (reply != null) {
            if (reply.isThrottle) {
                throw APIThrottleException()
            }
        }
    }

    /**
     * Internal method

     * @param request  The request to get
     * *
     * @param callback The callback to execute
     */
    private operator fun get(request: Request, callback: Callback<*>): Future<HttpResponse> {
        return get(request.getURL(this), callback)
    }

    /**
     * Internal method

     * @param url      The URL to send the request to
     * *
     * @param callback The callback to execute
     */
    private operator fun get(url: String, callback: Callback<*>): Future<HttpResponse> {
        return exService.submit<HttpResponse> { httpClient.execute(HttpGet(url), buildResponseHandler(callback)) }
    }

    private class SyncCallback<T : AbstractReply> constructor(clazz: Class<out AbstractReply>?)//noinspection unchecked
    : Callback<T>(clazz as Class<T>) {
        var failCause: Throwable? = null
        var result: T? = null

        override fun callback(failCause: Throwable?, result: T?) {
            this.failCause = failCause
            this.result = result
        }
    }

}