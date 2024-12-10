package splash.dev.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.BetterCpvp;

import static splash.dev.BetterCpvp.mc;

@Mixin(LivingEntity.class)
public abstract class PlayerEntityMixin {


    @Inject(method = "applyDamage", at = @At("HEAD"))
    public void applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
        if (BetterCpvp.recorder == null) return;
        if (BetterCpvp.getRecorder().isRecording()) {

            if (source.getAttacker() == mc.player) {
                BetterCpvp.getRecorder().updateDamageDealt(amount);
            }

            if((Object) this  == mc.player){
                BetterCpvp.getRecorder().updateSelfDamageDealt(amount);
            }
        }
    }
}
