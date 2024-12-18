package splash.dev;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import splash.dev.matches.SavedState;
import splash.dev.recording.Recorder;
import splash.dev.ui.gui.MainGui;
import splash.dev.ui.hud.HudEditor;
import splash.dev.ui.hud.HudManager;
import splash.dev.ui.recorder.RecorderGui;

public class PVPStatsPlus implements ModInitializer {
    public static final String MOD_ID = "pvpstatsplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    private static HudManager hudManager;
    private static Recorder recorder;
    private static MainGui gui;



    @Override
    public void onInitialize() {
        recorder = null;


        new SavedState().initialize();

        String[] bind = {"PVP Stats+", "Recorder Gui", "Stats Gui", "Hud Editor"};

        KeyBinding recordGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                bind[1],
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP,
                bind[0]
        ));
        KeyBinding mainGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                bind[2],
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                bind[0]
        ));
        KeyBinding hudEditor = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                bind[3],
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                bind[0]
        ));

        ClientTickEvents.START_CLIENT_TICK.register(minecraftClient -> {
            if (mc.currentScreen == null && mc.world != null) {


                if (recordGui.wasPressed() && !(mc.currentScreen instanceof RecorderGui)) {
                    mc.setScreen(new RecorderGui());
                }

                if (mainGui.wasPressed() && !(mc.currentScreen instanceof MainGui)) {
                    mc.setScreen(new MainGui());
                }
                if (hudEditor.wasPressed() && !(mc.currentScreen instanceof HudEditor)) {
                    mc.setScreen(new HudEditor());
                }
            }
            if (mc.world != null && mc.player != null && recorder != null && recorder.recording) {
                recorder.tick();
            }
        });


        LOGGER.info("Thanks for using PVP-Stats+");
    }
    public static Recorder getRecorder() {
        return recorder;
    }

    public static void resetRecorder(boolean invalidate) {
        if (invalidate) recorder = null;
        else recorder = new Recorder();
    }

    public static MainGui getGui() {
        return gui;
    }

    public static void setGui(MainGui gui) {
        PVPStatsPlus.gui = gui;
    }

    public static HudManager getHudManager() {
        return hudManager;
    }

    public static void setHudManager(HudManager hudManager) {
        PVPStatsPlus.hudManager = hudManager;
    }
}