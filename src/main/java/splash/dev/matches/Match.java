package splash.dev.matches;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public interface Match {
    File mainFolder = FabricLoader.getInstance().getGameDir().resolve("pvp-stats-plus").toFile();
    File matchesFolder = new File(mainFolder.getAbsoluteFile() + File.separator + "matches");

    void save();

    void load();

    void initialize();
}
