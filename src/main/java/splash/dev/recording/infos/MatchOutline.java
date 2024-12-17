package splash.dev.recording.infos;

import com.google.gson.JsonObject;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import static splash.dev.PVPStatsPlus.mc;

public class MatchOutline {
    AbstractClientPlayerEntity target;
    boolean won;
    int usedItems;
    float time;
    int id;

    public MatchOutline(AbstractClientPlayerEntity target, boolean won, int usedItems, float time, int id) {
        this.target = target;
        this.won = won;
        this.usedItems = usedItems;
        this.time = time;
        this.id = id;
    }

    public static MatchOutline fromJson(JsonObject outlineJson) {
        boolean won = outlineJson.get("won").getAsBoolean();
        int usedItems = outlineJson.get("usedItems").getAsInt();
        float time = outlineJson.get("time").getAsFloat();
        int id = outlineJson.has("id") ? outlineJson.get("id").getAsInt() : 0;


        return new MatchOutline(mc.player, won, usedItems, time, id);
    }


    public boolean isWon() {
        return won;
    }

    public AbstractClientPlayerEntity getTarget() {
        return target == null ? mc.player : target;
    }

    public int usedItems() {
        return usedItems;
    }

    public float getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public JsonObject getJson() {
        JsonObject matchOutline = new JsonObject();
        matchOutline.addProperty("won", this.won);
        matchOutline.addProperty("usedItems", this.usedItems);
        matchOutline.addProperty("time", this.time);

        if (this.target != null) matchOutline.addProperty("target", this.target.getGameProfile().getName());
        else matchOutline.addProperty("target", "unknown");

        return matchOutline;
    }

}
