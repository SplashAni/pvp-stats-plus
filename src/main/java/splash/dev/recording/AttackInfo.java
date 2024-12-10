package splash.dev.recording;

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
}