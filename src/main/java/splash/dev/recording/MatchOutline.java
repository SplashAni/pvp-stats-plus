package splash.dev.recording;

public class MatchOutline {
    String name;
    int usedItems;
    float time;
    int id;

    public MatchOutline(String name, int usedItems, float time, int id) {
        this.name = name;
        this.usedItems = usedItems;
        this.time = time;
        this.id = id;
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
