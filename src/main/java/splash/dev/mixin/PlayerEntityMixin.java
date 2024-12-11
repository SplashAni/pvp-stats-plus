package splash.dev.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.BetterCpvp;

import static splash.dev.BetterCpvp.mc;

@Mixin(LivingEntity.class)
public abstract class PlayerEntityMixin {



    @Inject(method = "applyDamage", at = @At("HEAD"))
    public void applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {



        if (!canupdate()) return;
        if (source.getAttacker() == null || source.getSource() == null) return;

        if (source.getAttacker() != null && source.getAttacker().getName().equals(mc.player.getName())) {
            BetterCpvp.getRecorder().updateDamageDealt(amount);
        }
    }

    @Unique
    public boolean canupdate() {
        if (BetterCpvp.getRecorder() == null) return false;
        return (BetterCpvp.recorder.isRecording());
    }
}

