package splash.dev.matches;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public interface Savable {
    File mainFolder = FabricLoader.getInstance().getGameDir().resolve("pvp-stats-plus").toFile();
    File matchesFolder = new File(mainFolder.getAbsoluteFile() + File.separator + "matches");
    File hudFile = new File(mainFolder.getAbsoluteFile() + File.separator + "hud.json");

    void saveMatches();

    void loadMatches();

    void saveHud();

    void loadHud();

    void initialize();

}
