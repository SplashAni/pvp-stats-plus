package splash.dev;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import splash.dev.gui.MainGui;
import splash.dev.recording.Recorder;

public class BetterCpvp implements ModInitializer {
    public static final String MOD_ID = "bettercpvp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static Recorder recorder;
    private static MainGui gui;
    private static BetterCpvp INSTANCE;

    public static BetterCpvp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;
        recorder = null;
        ClientTickEvents.START_CLIENT_TICK.register(minecraftClient -> {
            if(mc.world != null && mc.player != null && recorder != null && recorder.recording){
                recorder.tick();
            }
        });
        LOGGER.info("Hello Fabric world!");
    }

    public static Recorder getRecorder() {
        return recorder;
    }

    public static BetterCpvp get() {
        return INSTANCE;
    }

    public static MainGui getGui() {
        return gui;
    }

    public static void setGui(MainGui gui) {
        BetterCpvp.gui = gui;
    }
}