package splash.dev.recording.infos;

import com.google.gson.JsonObject;
import net.minecraft.client.util.SkinTextures;
import splash.dev.util.SkinHelper;

public class MatchOutline {
    String username;
    SkinTextures skin;
    boolean won;
    int usedItems;
    float time;
    int id;

    public MatchOutline(String username, SkinTextures skin, boolean won, int usedItems, float time, int id) {
        this.username = username;
        this.skin = skin;
        this.won = won;
        this.usedItems = usedItems;
        this.time = time;
        this.id = id;
    }

    public static MatchOutline fromJson(JsonObject outlineJson, int id) {
        String username = outlineJson.get("username").getAsString();
        boolean won = outlineJson.get("won").getAsBoolean();
        int usedItems = outlineJson.get("usedItems").getAsInt();
        float time = outlineJson.get("time").getAsFloat();



        return new MatchOutline(username, null, won, usedItems, time, id);
    }


    public boolean isWon() {
        return won;
    }

    public SkinTextures getSkin() {
        return skin == null ? SkinHelper.getSkinTarget(username).getSkinTextures() : skin;
    }

    public String getUsername() {
        return username;
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
        matchOutline.addProperty("username", username);
        matchOutline.addProperty("won", this.won);
        matchOutline.addProperty("usedItems", this.usedItems);
        matchOutline.addProperty("time", this.time);

        if (this.skin != null) matchOutline.addProperty("target", username);
        else matchOutline.addProperty("target", "unknown");

        return matchOutline;
    }

}
