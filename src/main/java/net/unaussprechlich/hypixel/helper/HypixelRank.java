package net.unaussprechlich.hypixel.helper;

import com.mojang.realmsclient.gui.ChatFormatting;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

/**
 * HypixelRank Created by Alexander on 28.02.2017.
 * Description:
 **/
public enum HypixelRank {

    DEFAULT("", "NONE", "", GRAY),
    VIP("[VIP]", "VIP", GREEN + "[VIP]", GREEN),
    VIP_PLUS("[VIP+]", "VIP_PLUS", GREEN + "[VIP" + GOLD + "+" + GREEN + "]", GREEN),
    MVP("[MVP]", "MVP", AQUA + "[MVP]", AQUA),
    MVP_PLUS("[MVP+]", "MVP_PLUS", AQUA + "[MVP" + RED + "+" + AQUA + "]", AQUA),
    // Not sure about these api names.
    HELPER("[HELPER]", "HELPER", BLUE + "[HELPER]", BLUE),
    MOD("[MOD]", "MODERATOR", DARK_GREEN + "[MOD]", DARK_GREEN),
    YT("[YT]", "YOUTUBER", GOLD + "[YT]", GOLD),
    ADMIN("[ADMIN]", "ADMINISTRATOR", RED + "[ADMIN]", RED);

    private final net.unaussprechlich.managedgui.lib.databases.player.data.Rank rank;



    HypixelRank(String name, String apiName, String nameFormatted, ChatFormatting color){
        this.rank = new net.unaussprechlich.managedgui.lib.databases.player.data.Rank(name, apiName, nameFormatted, color);
    }

    public static net.unaussprechlich.managedgui.lib.databases.player.data.Rank getRankByName(String name){

        for (HypixelRank hypixelRank : HypixelRank.values()) {
            if(hypixelRank.name().equals(name) || hypixelRank.rank.getRankApiName().equals(name)){
                System.out.print(hypixelRank.get().getRankName());
                return hypixelRank.get();
            }
        }
        return DEFAULT.get();
    }

    public net.unaussprechlich.managedgui.lib.databases.player.data.Rank get() {
        return rank;
    }
}
