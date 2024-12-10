package splash.dev.recording;

public class DamageInfo {
    int dealtDamage;
    int totalHealing;

    public DamageInfo(int totalDamage, int dealtDamage) {
        this.dealtDamage = totalDamage;
        this.totalHealing = dealtDamage;
    }

    public int getDealtDamage() {
        return dealtDamage;
    }

    public int getTotalHealing() {
        return totalHealing;
    }
}
