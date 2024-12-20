package splash.dev.mixin;

import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.PVPStatsPlus;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Shadow
    public float strideDistance;

    @Shadow
    public void increaseStat(Stat<?> stat, int amount) {

    }

    @Shadow
    public abstract boolean shouldFilterText();

    @Shadow
    public abstract void increaseStat(Identifier stat, int amount);

    @Shadow
    public abstract void incrementStat(Stat<?> stat);

    @Shadow public abstract void remove(Entity.RemovalReason reason);

    @Redirect(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ConsumeItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;)V"))
    public void eatFood(ConsumeItemCriterion instance, ServerPlayerEntity player, ItemStack stack) {
        if ((Object) this == mc.player) {
            if (getRecorder() != null && getRecorder().isRecording())
                getRecorder().updateItem(player.getMainHandStack().isOf(stack.getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND);
        }
    }


    @Inject(method = "incrementStat(Lnet/minecraft/stat/Stat;)V", at = @At("HEAD"))
    public void incrementStat(Stat<?> stat, CallbackInfo ci) {
        if (stat.getType().equals(Stats.USED) && (Object) this == mc.player) {
            if (stat.getValue() instanceof Item item) {
                updateRecorder(item); // todo potion types
            }
        }
    }

    @Unique
    public void updateRecorder(Item hand) {
        if (PVPStatsPlus.getRecorder() == null || !PVPStatsPlus.getRecorder().isRecording()) return;
        PVPStatsPlus.getRecorder().updateItem(hand);
    }

    @Redirect(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V"))
    public void increase(PlayerEntity instance, Identifier stat, int amount) {
        if (canUpdate(instance)) {
            increaseStat(stat, amount);

            PVPStatsPlus.getRecorder().updateSelfDamageDealt((float) amount / 10);
        }


    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V"))
    public void attack(PlayerEntity instance, Identifier stat, int amount) {

        if (canUpdate(instance)) {
            PVPStatsPlus.getRecorder().updateDamageDealt((float) amount / 10);
        }
    }

    public boolean canUpdate(PlayerEntity instance) {
        if(getRecorder() == null) return false;
        return instance == mc.player || PVPStatsPlus.getRecorder().isRecording();
    }
}
