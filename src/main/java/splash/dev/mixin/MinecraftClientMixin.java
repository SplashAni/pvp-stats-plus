package splash.dev.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import splash.dev.BetterCpvp;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void onDeath(Screen screen, CallbackInfo ci) {
        if (screen instanceof DeathScreen && BetterCpvp.getRecorder() != null) {
            if (BetterCpvp.recorder.isRecording()) BetterCpvp.getRecorder().stopRecording();
        }
    }
    @Shadow protected abstract void render(boolean tick);

    @Inject(method = "doItemUse",at = @At("HEAD"))
    public void doItemUse(CallbackInfo ci){
        if(BetterCpvp.recorder == null) return;
        if(BetterCpvp.getRecorder().isRecording()){
            BetterCpvp.getRecorder().onItemUse();
        }
    }

    @Inject(method = "doAttack",at = @At("HEAD"))
    public void doAttack(CallbackInfoReturnable<Boolean> cir){
        if(BetterCpvp.recorder == null) return;
        if(BetterCpvp.getRecorder().isRecording()){
            BetterCpvp.getRecorder().onAttack();
        }
    }
}
