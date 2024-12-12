package splash.dev.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import splash.dev.PVPStatsPlus;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void onDeath(Screen screen, CallbackInfo ci) {
        if (screen instanceof DeathScreen && PVPStatsPlus.getRecorder() != null) {
            if (PVPStatsPlus.recorder.isRecording()) PVPStatsPlus.getRecorder().stopRecording(false);
        }

    }

    @Inject(method = "doItemUse",at = @At("HEAD"))
    public void doItemUse(CallbackInfo ci){
        if(PVPStatsPlus.recorder == null) return;
        if(PVPStatsPlus.getRecorder().isRecording()){
            PVPStatsPlus.getRecorder().onItemUse();
        }
    }

    @Inject(method = "doAttack",at = @At("HEAD"))
    public void doAttack(CallbackInfoReturnable<Boolean> cir){
        if(PVPStatsPlus.recorder == null) return;
        if(PVPStatsPlus.getRecorder().isRecording()){
            PVPStatsPlus.getRecorder().onAttack();
        }
    }
}
