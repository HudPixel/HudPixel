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
package net.hypixel.example;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.BoostersReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;

import java.util.UUID;

public class GetBoostersExample {
    public static void main(String[] args) {
        HypixelAPI.getInstance().setApiKey(UUID.fromString("fd08635f-5848-4bd7-a9f6-771f5d93b1de"));

        Request request = RequestBuilder.newBuilder(RequestType.BOOSTERS).createRequest();
        System.out.println(request.getURL(HypixelAPI.getInstance()));
        HypixelAPI.getInstance().getAsync(request, new Callback<BoostersReply>(BoostersReply.class) {
            @Override
            public void callback(Throwable failCause, BoostersReply result) {
                if (failCause != null) {
                    failCause.printStackTrace();
                } else {
                    System.out.println(result);
                }
                HypixelAPI.getInstance().finish();
                System.exit(0);
            }
        });
        ExampleUtil.await(); // This is required because the API is asynchronous, so without this the program will exit.
    }
}