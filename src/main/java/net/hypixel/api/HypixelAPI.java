/*******************************************************************************
 * HudPixelReloaded
 *
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses under forge-docs/. These parts can be downloaded at files.minecraftforge.net.
 *
 * This project contains a unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 *
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously intended for usage in this kind of application. By default, all rights are reserved.
 *
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license. The majority of code left from palechip's creations is the component implementation.
 *
 * The ported version to Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license (to be changed to the new license as detailed below in the next minor update).
 *
 * For the rest of the code and for the build the following license applies:
 *
 * alt-tag
 *
 * HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions. Based on a work at HudPixelExtended & HudPixel.
 *
 * Restrictions:
 *
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow the following license terms and the license terms given by the listed above Creative Commons License, however in extreme cases the authors reserve the right to revoke all rights for usage of the codebase.
 *
 * PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their code, but only when it is used separately from HudPixel and any license header must indicate that.
 * You shall not claim ownership over this project and repost it in any case, without written permission from at least two of the authors.
 * You shall not make money with the provided material. This project is 100% non commercial and will always stay that way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed code is merged to the release branch you cannot revoke the given freedoms by this license.
 * If your own project contains a part of the licensed material you have to give the authors full access to all project related files.
 * You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors reserve the right to take down any infringing project.
 ******************************************************************************/
package net.hypixel.api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hypixel.api.adapters.*;
import net.hypixel.api.exceptions.APIThrottleException;
import net.hypixel.api.exceptions.HypixelAPIException;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.reply.GuildReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.util.Callback;
import net.hypixel.api.util.GameType;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unused")
public class HypixelAPI {

    private static HypixelAPI instance;
    private final Gson gson;
    private final ReentrantReadWriteLock lock;
    private final ExecutorService exService = Executors.newCachedThreadPool();
    private final HttpClient httpClient;
    private UUID apiKey;

    private HypixelAPI() {
        gson = new GsonBuilder()
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeAdapter(GameType.class, new GameTypeTypeAdapter())
                .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())

                // guilds
                .registerTypeAdapter(GuildReply.Guild.GuildCoinHistory.class, new GuildCoinHistoryAdapter())
                .registerTypeAdapterFactory(new GuildCoinHistoryHoldingTypeAdapterFactory<>(GuildReply.Guild.class))
                .registerTypeAdapterFactory(new GuildCoinHistoryHoldingTypeAdapterFactory<>(GuildReply.Guild.Member.class))

                .create();
        lock = new ReentrantReadWriteLock();
        httpClient = HttpClientBuilder.create().build();
    }

    /**
     * Gets the existing HypixelAPI, or constructs a new one
     *
     * @return The HypixelAPI
     */
    public static HypixelAPI getInstance() {
        if (instance == null) {
            instance = new HypixelAPI();
        }
        return instance;
    }



    private <T extends AbstractReply> void getResponse(Callback<T> callback, Request request) {
        new Thread( () -> {
            try {
                URL u = new URL(request.getURL(HypixelAPI.getInstance()));
                HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuilder sBuilder = new StringBuilder();
                String buff = "";

                while ((buff = br.readLine()) != null) {
                    sBuilder.append(buff);
                }

                T value = gson.fromJson(sBuilder.toString(), callback.getClazz());
                checkReply(value);
                callback.callback(null, value);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
        T value;
        try {

        } catch (Throwable t) {
            callback.callback(t, null);
        }
    }

    /**
     * Call this method when you're finished requesting anything from the API.
     * The API maintains it's own internal threadpool, so if you don't call this
     * the application will never exit.
     */
    public void finish() {
        try {
            exService.shutdown();
            instance = null;
        } catch (Exception e) {
            throw new HypixelAPIException(e);
        }
    }

    /**
     * @return currently set API key
     */
    public UUID getApiKey() {
        return apiKey;
    }

    /**
     * Call this method to set the API key
     *
     * @param apiKey The API key to set
     */
    public void setApiKey(UUID apiKey) {
        Preconditions.checkNotNull(apiKey);
        lock.writeLock().lock();
        try {
            this.apiKey = apiKey;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Use this method to pull data off of the Public API
     *
     * @param request Request object to pull from API
     * @param <R>     The class of the reply
     * @throws HypixelAPIException
     */
    public <R extends AbstractReply> R getSync(Request request) throws HypixelAPIException {
        lock.readLock().lock();
        SyncCallback<R> callback = new SyncCallback<>(request.getRequestType().getReplyClass());
        try {
            if (doKeyCheck(callback)) {
                Future<HttpResponse> future = get(request, callback);
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            callback.callback(e, null);
        } finally {
            lock.readLock().unlock();
        }
        if (callback.failCause != null) {
            throw new HypixelAPIException(callback.failCause);
        } else {
            return callback.result;
        }
    }

    /**
     * Execute Request asynchronously, executes Callback when finished
     *
     * @param request  Request to get
     * @param callback Callback to execute
     * @param <R>      Class of the reply
     */
    public <R extends AbstractReply> void getAsync(Request request, Callback<R> callback) {
        lock.readLock().lock();
        try {
            if (doKeyCheck(callback)) {
                getResponse(callback, request);
                //get(request, callback); hey jo @Plancke fix your stuff :3
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Internal method
     *
     * @param callback The callback to fail to
     * @return True if we should continue
     */
    private boolean doKeyCheck(Callback<?> callback) {
        if (apiKey == null) {
            callback.callback(new HypixelAPIException("API key hasn't been set yet!"), null);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Internal method
     *
     * @param callback The callback to execute
     * @param <T>      The class of the callback
     * @return The ResponseHandler that wraps the callback
     */
    private <T extends AbstractReply> ResponseHandler<HttpResponse> buildResponseHandler(Callback<T> callback) {
        return obj -> {
            T value;
            try {
                String content = EntityUtils.toString(obj.getEntity(), "UTF-8");
                value = gson.fromJson(content, callback.getClazz());

                checkReply(value);
            } catch (Throwable t) {
                callback.callback(t, null);
                return obj;
            }
            callback.callback(null, value);
            return obj;
        };
    }

    /**
     * Checks reply and throws appropriate exceptions based on it's content
     *
     * @param reply The reply to check
     * @param <T>   The class of the reply
     */
    public <T extends AbstractReply> void checkReply(T reply) {
        if (reply != null) {
            if (reply.isThrottle()) {
                throw new APIThrottleException();
            }
        }
    }

    /**
     * Internal method
     *
     * @param request  The request to get
     * @param callback The callback to execute
     */
    private Future<HttpResponse> get(Request request, Callback<?> callback) {
        return get(request.getURL(this), callback);
    }

    /**
     * Internal method
     *
     * @param url      The URL to send the request to
     * @param callback The callback to execute
     */
    private Future<HttpResponse> get(String url, Callback<?> callback) {
        return exService.submit(() ->
                httpClient.execute(new HttpGet(url), buildResponseHandler(callback))
        );
    }

    private class SyncCallback<T extends AbstractReply> extends Callback<T> {
        private Throwable failCause;
        private T result;

        private SyncCallback(Class<? extends AbstractReply> clazz) {
            //noinspection unchecked
            super((Class<T>) clazz);
        }

        @Override
        public void callback(Throwable failCause, T result) {
            this.failCause = failCause;
            this.result = result;
        }
    }

}