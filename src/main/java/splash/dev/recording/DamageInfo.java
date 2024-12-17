package splash.dev.recording;

import com.google.gson.JsonObject;

public class DamageInfo {
    int dealtDamage;
    int damageTaken;

    public DamageInfo(int dealtDamage, int damageTaken) {
        this.dealtDamage = dealtDamage;
        this.damageTaken = damageTaken;
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
