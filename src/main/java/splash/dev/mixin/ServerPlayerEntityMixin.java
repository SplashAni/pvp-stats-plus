package splash.dev.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static splash.dev.PVPStatsPlus.getRecorder;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {


    @Redirect(remap = false , method = "increaseStat", at = @At(value = "INVOKE", target
            = "Lnet/minecraft/stat/ServerStatHandler;increaseStat(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/stat/Stat;I)V"))
    public void yes(ServerStatHandler instance, PlayerEntity entity, Stat<?> stat, int i) {
        instance.increaseStat(entity, stat, i);
        System.out.println("value "+stat.toString() + " val "+i);
    }

    public boolean canUpdate() {
        if (getRecorder() == null) return false;
        return getRecorder().isRecording();
    }
}
