package splash.dev.mixin;

import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.BetterCpvp;
import splash.dev.gui.MainGui;
import splash.dev.recording.Recorder;

import static splash.dev.BetterCpvp.mc;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action != GLFW.GLFW_RELEASE) return;

        switch (key) {
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> mc.setScreen(new MainGui());
            case GLFW.GLFW_KEY_UP -> toggleRecording();
        }
    }

    @Unique
    private void toggleRecording() {
        if (BetterCpvp.getRecorder() == null) {
            BetterCpvp.recorder = new Recorder();
        }

        Recorder recorder = BetterCpvp.getRecorder();
        if (recorder.isRecording()) {
            recorder.stopRecording(true);
        } else {
            recorder.startRecording();
        }
    }
}
