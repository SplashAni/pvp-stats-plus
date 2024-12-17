package splash.dev.recording.infos;

import com.google.gson.JsonObject;

public class DamageInfo {
    int dealtDamage;
    int damageTaken;

    public DamageInfo(int dealtDamage, int damageTaken) {
        this.dealtDamage = dealtDamage;
        this.damageTaken = damageTaken;
    }


    public static DamageInfo fromJson(JsonObject damageJson) {
        if (damageJson == null) {
            throw new IllegalArgumentException("damageJson is null");
        }

        int dealtDamage = damageJson.has("dealtDamage") ? damageJson.get("dealtDamage").getAsInt() : 0;
        int damageTaken = damageJson.has("damageTaken") ? damageJson.get("damageTaken").getAsInt() : 0;

        return new DamageInfo(dealtDamage, damageTaken);
    }

    public int getDealtDamage() {
        return dealtDamage;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public JsonObject getJson() {
        JsonObject damageInfo = new JsonObject();
        damageInfo.addProperty("dealtDamage", this.dealtDamage);
        damageInfo.addProperty("damageTaken", this.damageTaken);
        return damageInfo;
    }
}
