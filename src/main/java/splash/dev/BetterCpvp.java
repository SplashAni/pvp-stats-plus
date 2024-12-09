package splash.dev;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterCpvp implements ModInitializer {
    public static final String MOD_ID = "bettercpvp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    private static BetterCpvp INSTANCE;

    public static BetterCpvp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;
        LOGGER.info("Hello Fabric world!");
    }


}