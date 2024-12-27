package splash.dev.mixin;

import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.PVPStatsPlus;
import splash.dev.data.gamemode.GamemodeBind;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {


        if (mc.world == null) return;

        if (action == GLFW.GLFW_RELEASE && mc.currentScreen == null) {
            for (GamemodeBind gamemode : PVPStatsPlus.getBindManager().getGamemodes()) {
                if (gamemode.getKey() == key) {

                    if (getRecorder() == null) {
                        PVPStatsPlus.resetRecorder(false);
                        getRecorder().startRecording(gamemode.getGamemode());
                    } else {
                        if (getRecorder().isRecording()) {
                            getRecorder().stopRecording(false);
                            PVPStatsPlus.resetRecorder(true);
                        }
                    }

                }
            }
        }
    }
}
