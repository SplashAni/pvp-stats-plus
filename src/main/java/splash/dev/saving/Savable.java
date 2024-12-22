package splash.dev.saving;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public interface Savable {
    File mainFolder = FabricLoader.getInstance().getGameDir().resolve("pvp-stats-plus").toFile();
    File matchesFolder = new File(mainFolder.getAbsoluteFile() + File.separator + "matches");
    File skinsFolder = new File(mainFolder.getAbsoluteFile() + File.separator + "skins");
    File hudFile = new File(mainFolder.getAbsoluteFile() + File.separator + "hud.json");
    File bindFile = new File(mainFolder.getAbsoluteFile() + File.separator + "binds.json");

    void saveMatches();

    void loadMatches();

    void saveBinds();

    void loadBinds();

    void saveHud();

    void loadHud();

    void initialize();

}
