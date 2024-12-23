package splash.dev.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import splash.dev.PVPStatsPlus;
import splash.dev.util.ItemHelper;


@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow
    private ItemStack selectedStack;

    @Shadow
    public abstract ActionResult interactItem(PlayerEntity player, Hand hand);

    @Inject(at = @At("TAIL"), method = "interactBlock")
    private void interact(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue() == ActionResult.SUCCESS) {
            updateRecorder(hand);
        }
    }

    @Inject(method = "interactItem", at = @At("TAIL"))
    public void interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ItemHelper.isPostState(player.getStackInHand(hand).getItem())) {
            updateRecorder(player.getStackInHand(hand));
        }
    }

    @Unique
    public void updateRecorder(Hand hand) {
        if (PVPStatsPlus.getRecorder() == null || !PVPStatsPlus.getRecorder().isRecording()) return;
        PVPStatsPlus.getRecorder().updateItem(hand);
    }

    @Unique
    public void updateRecorder(ItemStack hand) {
        if (PVPStatsPlus.getRecorder() == null || !PVPStatsPlus.getRecorder().isRecording()) return;
        PVPStatsPlus.getRecorder().updateItem(hand);
    }


}