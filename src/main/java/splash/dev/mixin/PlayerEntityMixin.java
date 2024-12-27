package splash.dev.mixin;

import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {


    @Redirect(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ConsumeItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;)V"))
    public void eatFood(ConsumeItemCriterion instance, ServerPlayerEntity player, ItemStack stack) {
        Criteria.CONSUME_ITEM.trigger(player, stack);

        if (player.getGameProfile().equals(mc.player.getGameProfile())) {
            if (getRecorder() != null && getRecorder().isRecording())
                getRecorder().updateItem(stack);
            mc.inGameHud.getChatHud().addMessage(Text.of("ate " + stack.toString()));
        }
    }

    @Inject(method = "incrementStat(Lnet/minecraft/stat/Stat;)V", at = @At("HEAD"))
    public void incrementStat(Stat<?> stat, CallbackInfo ci) {
        if (stat.getType().equals(Stats.USED) && (Object) this == mc.player) {
            if (stat.getValue() instanceof ItemStack item) {
                updateRecorder(item); // todo potion types, done
            }
        }
    }

    @Unique
    public void updateRecorder(ItemStack hand) {
        if (getRecorder() == null || !getRecorder().isRecording()) return;
        getRecorder().updateItem(hand);
    }

}
