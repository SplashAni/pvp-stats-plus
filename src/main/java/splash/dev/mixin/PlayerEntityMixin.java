package splash.dev.mixin;

import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Redirect(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ConsumeItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;)V"))
    public void eatFood(ConsumeItemCriterion instance, ServerPlayerEntity player, ItemStack stack) {
        if (player.getGameProfile().equals(Objects.requireNonNull(mc.player).getGameProfile())) {
            if (getRecorder() != null && getRecorder().isRecording())
                getRecorder().updateItem(player.getMainHandStack().isOf(stack.getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND);
        }
    }
}
