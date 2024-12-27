package splash.dev.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import splash.dev.PVPStatsPlus;

import static splash.dev.PVPStatsPlus.mc;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Unique
    private ItemStack lastUsed = Items.AIR.getDefaultStack();

    @Inject(at = @At("HEAD"), method = "interactBlock")
    private void interactHead(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (mc.player != null) {
            ItemStack currentItem = mc.player.getStackInHand(hand);
        }
    }

    @Inject(at = @At("TAIL"), method = "interactBlock")
    private void interact(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue() == ActionResult.SUCCESS) {
            ItemStack used = mc.player.getStackInHand(hand);
            if (used.getItem() == Items.AIR) {
                used = lastUsed;
            } else {
                lastUsed = used;
            }

            updateRecorder(used);
        }
    }

    @Inject(method = "interactItem", at = @At("TAIL"))
    public void interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue() == ActionResult.SUCCESS) {
            ItemStack currentItem = player.getStackInHand(hand);
            if (currentItem.getItem() == Items.AIR) {
                currentItem = lastUsed;
            } else {
                lastUsed = currentItem;
            }

            updateRecorder(currentItem);
        }
    }

    @Unique
    private void updateRecorder(ItemStack item) {
        if (PVPStatsPlus.getRecorder() == null || !PVPStatsPlus.getRecorder().isRecording()) return;
        PVPStatsPlus.getRecorder().updateItem(item);
    }
}
