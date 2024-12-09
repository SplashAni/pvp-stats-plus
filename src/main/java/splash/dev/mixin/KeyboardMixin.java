package splash.dev.mixin;

import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.SOFTBformatEx;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.gui.MainGui;

import static splash.dev.BetterCpvp.mc;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey",at = @At("HEAD"))
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci){
        if(key == GLFW.GLFW_KEY_RIGHT_SHIFT && action == GLFW.GLFW_RELEASE){
            System.out.println("setting key");
            mc.setScreen(new MainGui());
        }
    }
}
