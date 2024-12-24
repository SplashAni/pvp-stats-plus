package splash.dev.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Redirect(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V"))
    public void increaseTravelMotionStats(ServerPlayerEntity instance, Identifier id, int i) {
        if (instance.equals(mc.player) && canUpdate())
            if (id.equals(Stats.WALK_ONE_CM)) getRecorder().updateDistanceWalked(i);
            else if (id.equals(Stats.CROUCH_ONE_CM)) getRecorder().updateDistanceCrouched(i);
            else if (id.equals(Stats.SPRINT_ONE_CM)) getRecorder().updateDistanceSprinted(i);
            // these bitches lova sosa <3
    }

    public boolean canUpdate() {
        if (getRecorder() == null) return false;
        return getRecorder().isRecording();
    }
}
