package splash.dev.data.gamemode;

public class GamemodeBind {
    Gamemode gamemode;
    int key;

    public GamemodeBind(Gamemode gamemode, int key) {
        this.gamemode = gamemode;
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }
}
