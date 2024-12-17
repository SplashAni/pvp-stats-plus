package splash.dev.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.PVPStatsPlus;

import static splash.dev.PVPStatsPlus.mc;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract float getAbsorptionAmount(); /*todo thos pls*/

    @Inject(method = "setHealth", at = @At("HEAD"))
    public void onHealthSet(float health, CallbackInfo ci) {

        if (PVPStatsPlus.getRecorder() != null && PVPStatsPlus.getRecorder().isRecording())
            if ((Object) this == mc.player) {

                PVPStatsPlus.getRecorder().updateSelfDamageDealt(Math.abs(getHealth() - health));

            }
    }
}
