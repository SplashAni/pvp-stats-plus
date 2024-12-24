package splash.dev.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import splash.dev.PVPStatsPlus;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {


    @Redirect(method = "damage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V"))
    public void modify(ServerPlayerEntity instance, Identifier identifier, int i) {
        instance.increaseStat(identifier, i);

        if (canUpdate(instance) && identifier == Stats.DAMAGE_BLOCKED_BY_SHIELD) {
            getRecorder().updateDamageBlocked((float) i / 10);
        }
    }

    @Unique
    public boolean canUpdate(PlayerEntity instance) {
        if (getRecorder() == null) return false;
        return instance == mc.player || PVPStatsPlus.getRecorder().isRecording();
    }
}
