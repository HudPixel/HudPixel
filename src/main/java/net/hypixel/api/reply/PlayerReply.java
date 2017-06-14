package net.hypixel.api.reply;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.ILeveling;
import net.minecraft.util.EnumChatFormatting;
import net.unaussprechlich.hudpixelextended.util.LoggerHelper;
import net.unaussprechlich.hypixel.helper.HypixelRank;
import net.unaussprechlich.managedgui.lib.databases.player.data.Rank;

import static net.unaussprechlich.hudpixelextended.util.McColorHelper.*;

@SuppressWarnings("unused")
public class PlayerReply extends AbstractReply {
    private JsonElement player;

    public JsonObject getPlayer() {
        if (player == null || player.isJsonNull()) {
            return null;
        } else {
            return player.getAsJsonObject();
        }
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYER;
    }

    @Override
    public String toString() {
        return "PlayerReply{" +
                "player=" + player +
                ", super=" + super.toString() + "}";
    }

    public Rank getRank() {
        Rank rank = HypixelRank.DEFAULT.get();
        if (getPlayer() != null) {
            if (getPlayer().has("newPackageRank"))
                // Post-EULA.
                rank = HypixelRank.getRankByName(getPlayer().get("newPackageRank").getAsString());
            else if (getPlayer().has("packageRank"))
                // Pre-EULA and nondons.
                rank = HypixelRank.getRankByName(getPlayer().get("packageRank").getAsString());
        }
        return rank;
    }

    private String getRankColor() {
        String rankColor = "RES";
        if (getPlayer() != null) {
            if (getPlayer().has("rankPlusColor")) {
                rankColor = getPlayer().get("rankPlusColor").getAsString();
            }
        } else
            rankColor = null;
        return rankColor;
    }

    private String getDisplayName() {
        if (getPlayer() != null)
            return getPlayer().get("displayname").getAsString();
        else
            return null;
    }

    public String getFormattedDisplayName() {
        String formattedName = null;
        if (getPlayer() != null) {
            switch (getRank().getRankApiName()) {
                case "MVP_PLUS":
                    formattedName = AQUA + "[MVP" + EnumChatFormatting.valueOf(getRankColor()) + "+" + AQUA + "] " + getDisplayName();
                    break;
                case "MVP":
                    formattedName = AQUA + "[MVP] " + getDisplayName();
                    break;
                case "VIP_PLUS":
                    formattedName = GREEN + "[VIP" + GOLD + "+" + GREEN + "] " + getDisplayName();
                    break;
                case "VIP":
                    formattedName = GREEN + "[VIP] " + getDisplayName();
                    break;
                case "NONE":
                    formattedName = GRAY + getDisplayName();
                    break;
            }
        }
        return formattedName;
    }

    public double getLevel() {
        // Get the old level, convert that to exp. Add to current exp and calculate level.
        return ILeveling.getExactLevel(getPlayer().get(ILeveling.EXP_FIELD).getAsDouble() + ILeveling.getTotalExpToLevel(getPlayer().get(ILeveling.LVL_FIELD).getAsDouble()));
    }
}
