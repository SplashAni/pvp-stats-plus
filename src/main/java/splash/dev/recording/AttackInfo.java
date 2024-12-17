package splash.dev.recording;

import com.google.gson.JsonObject;

public class AttackInfo {
    int longestCombo, misses, crits;

    public AttackInfo(int longestCombo, int misses, int crits) {
        this.longestCombo = longestCombo;
        this.misses = misses;
        this.crits = crits;
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
