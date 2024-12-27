package splash.dev;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import splash.dev.data.MatchSortType;
import splash.dev.data.gamemode.BindManager;
import splash.dev.data.gamemode.Gamemode;
import splash.dev.recording.Recorder;
import splash.dev.saving.SavedState;
import splash.dev.ui.gui.MainGui;
import splash.dev.ui.hud.HudEditor;
import splash.dev.ui.hud.HudManager;
import splash.dev.ui.recorder.RecorderGui;

import java.util.ArrayList;
import java.util.List;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class PVPStatsPlus implements ModInitializer {
    public static final String MOD_ID = "pvpstatsplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static MinecraftClient mc;
    private static BindManager bindManager;
    private static HudManager hudManager;
    private static Recorder recorder;
    private static MatchSortType matchSortType;

    public static Recorder getRecorder() {
        return recorder;
    }

    public static MatchSortType getMatchSortType() {
        return matchSortType;
    }

    public static BindManager getBindManager() {
        return bindManager;
    }

    public static void resetRecorder(boolean invalidate) {
        if (invalidate) recorder = null;
        else recorder = new Recorder();
    }


    public static HudManager getHudManager() {
        return hudManager;
    }

    public static void setHudManager(HudManager hudManager) {
        PVPStatsPlus.hudManager = hudManager;
    }

    private static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "name");

        ItemStack item = ItemStackArgumentType.getItemStackArgument(ctx, "icon").createStack(1, false);

        boolean registered = Gamemode.register(name, item.getItem());

        Text prefix = Text.empty()
                .setStyle(Style.EMPTY.withFormatting(Formatting.BLACK))
                .append("[")
                .append(Text.literal("pvp-stats-plus")
                        .setStyle(Style.EMPTY.withFormatting(Formatting.GOLD).withFormatting(Formatting.BOLD)))
                .append("]").setStyle(Style.EMPTY);

        if (registered) {
            mc.inGameHud.getChatHud().addMessage(prefix.copy().append(" successfully created gamemode " + name));
        } else {
            mc.inGameHud.getChatHud().addMessage(prefix.copy().append(" failed to create gamemode " + name));
        }

        return SINGLE_SUCCESS;
    }


    @Override
    public void onInitialize() {

        recorder = null;
        mc = MinecraftClient.getInstance();
        Gamemode.register();
        bindManager = new BindManager();
        matchSortType = MatchSortType.LATEST;

        SavedState savedState = new SavedState();
        savedState.initialize();

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

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(CommandManager.literal("creategamemode")
                    .then(CommandManager.argument("icon", ItemStackArgumentType.itemStack(commandRegistryAccess))
                            .then(CommandManager.argument("name", StringArgumentType.string())
                                    .executes(PVPStatsPlus::run)
                            )
                    )
            );
        });

        LOGGER.info("Thanks for using PVP-Stats+");
    }
}