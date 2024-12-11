package splash.dev.recording;

public class MatchOutline {
    String name;
    boolean won;
    int usedItems;
    float time;
    int id;

    public MatchOutline(String name, boolean won, int usedItems, float time, int id) {
        this.name = name;
        this.won = won;
        this.usedItems = usedItems;
        this.time = time;
        this.id = id;
    }

    public boolean isWon() {
        return won;
    }

    public String getName() {
        return name;
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
}
