package splash.dev.recording;

public class DamageInfo {
    int dealtDamage;
    int damageTaken;

    public DamageInfo(int totalDamage, int damageTaken) {
        this.dealtDamage = totalDamage;
        this.damageTaken = damageTaken;
    }

    public int getDealtDamage() {
        return dealtDamage;
    }

    public int getDamageTaken() {
        return damageTaken;
    }
}
