package splash.dev.recording.infos;

import com.google.gson.JsonObject;

public class DamageInfo {
    int dealtDamage;
    int damageTaken;
    int damageBlocked;

    public DamageInfo(int dealtDamage, int damageTaken, int damageBlocked) {
        this.dealtDamage = dealtDamage;
        this.damageTaken = damageTaken;
        this.damageBlocked = damageBlocked;
    }

    public static DamageInfo fromJson(JsonObject damageJson) {
        if (damageJson == null) {
            throw new IllegalArgumentException("damageJson is null");
        }

        int dealtDamage = damageJson.has("dealtDamage") ? damageJson.get("dealtDamage").getAsInt() : 0;
        int damageTaken = damageJson.has("damageTaken") ? damageJson.get("damageTaken").getAsInt() : 0;
        int damageBlocked = damageJson.has("damageBlocked") ? damageJson.get("damageBlocked").getAsInt() : 0;

        return new DamageInfo(dealtDamage, damageTaken, damageBlocked);
    }

    public int getDealtDamage() {
        return dealtDamage;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public int getDamageBlocked() {
        return damageBlocked;
    }

    public JsonObject getJson() {
        JsonObject damageInfo = new JsonObject();
        damageInfo.addProperty("dealtDamage", this.dealtDamage);
        damageInfo.addProperty("damageTaken", this.damageTaken);
        damageInfo.addProperty("damageBlocked", this.damageBlocked);
        return damageInfo;
    }
}
