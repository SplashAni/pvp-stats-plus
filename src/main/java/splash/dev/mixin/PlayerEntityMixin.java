package splash.dev.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.BetterCpvp;

import static splash.dev.BetterCpvp.mc;

@Mixin(LivingEntity.class)
public abstract class PlayerEntityMixin {


    @Inject(method = "applyDamage", at = @At("HEAD"))
    public void applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {


        if (BetterCpvp.getRecorder() == null) return;
        if (BetterCpvp.recorder.isRecording()) {

            if (source.getAttacker() == null || source.getSource() == null) return;

            if (source.getAttacker() == source.getSource()) return;


            if (source.getAttacker().getName().equals(mc.player.getName())) {
                BetterCpvp.getRecorder().updateDamageDealt(amount);

                mc.getNetworkHandler().sendChatMessage("did dmg " + source.getSource().getName()
                        + " amount : " + amount);
            }
            if (source.getSource().getName().equals(mc.player.getName())) {
                mc.getNetworkHandler().sendChatMessage("took dmg from" + source.getAttacker().getName()
                        + " amount : " + amount);
                BetterCpvp.getRecorder().updateSelfDamageDealt(amount);

            }
        }
    }
}
