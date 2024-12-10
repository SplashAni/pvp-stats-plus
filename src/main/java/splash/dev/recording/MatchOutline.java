package splash.dev.recording;

public class MatchOutline {
    String name;
    int usedItems;
    float time;

    public MatchOutline(String name, int usedItems, float time) {
        this.name = name;
        this.usedItems = usedItems;
        this.time = time;
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
}
