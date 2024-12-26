package splash.dev.recording.infos;

import com.google.gson.JsonObject;

public class ArrowInfo {
    int arrowsShot, longestArrowShot;
    float accuracy;

    public ArrowInfo(int arrowsShot, int longestArrowShot, float accuracy) {
        this.arrowsShot = arrowsShot;
        this.longestArrowShot = longestArrowShot;
        this.accuracy = accuracy;
    }

    public static ArrowInfo fromJson(JsonObject attackJson) {

        if (attackJson == null) throw new IllegalArgumentException("arrowInfo is null");

        int shot = attackJson.has("arrowsShot") ? attackJson.get("arrowsShot").getAsInt() : 0;
        int longest = attackJson.has("longestArrowShot") ? attackJson.get("longestArrowShot").getAsInt() : 0;
        float accuracy = attackJson.has("accuracy") ? attackJson.get("accuracy").getAsFloat() : 0;

        return new ArrowInfo(shot, longest, accuracy);
    }


    public int getArrowsShot() {
        return arrowsShot;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public int getLongestArrowShot() {
        return longestArrowShot;
    }

    public JsonObject getJson() {
        JsonObject attackInfo = new JsonObject();
        attackInfo.addProperty("arrowsShot", arrowsShot);
        attackInfo.addProperty("longestArrowShot", longestArrowShot);
        attackInfo.addProperty("accuracy", accuracy);
        return attackInfo;
    }
}
