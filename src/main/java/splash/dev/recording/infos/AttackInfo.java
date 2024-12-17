package splash.dev.recording.infos;

import com.google.gson.JsonObject;

public class AttackInfo {
    int longestCombo, misses, crits;

    public AttackInfo(int longestCombo, int misses, int crits) {
        this.longestCombo = longestCombo;
        this.misses = misses;
        this.crits = crits;
    }

    public static AttackInfo fromJson(JsonObject attackJson) {

        if (attackJson == null) throw new IllegalArgumentException("attackJson is null");

        int longestCombo = attackJson.has("longestCombo") ? attackJson.get("longestCombo").getAsInt() : 0;
        int misses = attackJson.has("misses") ? attackJson.get("misses").getAsInt() : 0;
        int crits = attackJson.has("crits") ? attackJson.get("crits").getAsInt() : 0;

        return new AttackInfo(longestCombo, misses, crits);
    }


    public int getLongestCombo() {
        return longestCombo;
    }

    public int getMisses() {
        return misses;
    }

    public int getCrits() {
        return crits;
    }

    public JsonObject getJson() {
        JsonObject attackInfo = new JsonObject();
        attackInfo.addProperty("longestCombo", this.longestCombo);
        attackInfo.addProperty("misses", this.misses);
        attackInfo.addProperty("crits", this.crits);
        return attackInfo;
    }
}
