package splash.dev.data.gamemode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BindManager {
    List<GamemodeBind> gamemodes;

    public BindManager() {
        gamemodes = new ArrayList<>();
        Arrays.stream(Gamemode.values())
                .forEachOrdered(value -> gamemodes.add(new GamemodeBind(value, -1)
                ));
    }

    public int getKey(Gamemode gamemode) {
        for (GamemodeBind gamemodeBind : gamemodes) {
            if (gamemodeBind.gamemode == gamemode) {
                return gamemodeBind.getKey();
            }
        }
        return -1;
    }

    public void setKey(Gamemode gamemode, int keyCode) {
        gamemodes.stream().filter(gamemodeBind -> gamemodeBind.gamemode == gamemode).findFirst().ifPresent(gamemodeBind -> gamemodeBind.setKey(keyCode));
    }

    public List<GamemodeBind> getGamemodes() {
        return gamemodes;
    }
}
