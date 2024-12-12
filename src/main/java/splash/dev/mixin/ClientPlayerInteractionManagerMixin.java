package splash.dev.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static splash.dev.PVPStatsPlus.mc;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(at = @At("HEAD"), method = "interactItem")
    private void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (player == mc.player) {
 /*           ItemStack stack = hand == Hand.OFF_HAND ? mc.player.getOffHandStack() : mc.player.getMainHandStack();
            BetterCpvp.getInstance().getItemInteractData().updateItem(stack.getItem());*/
        }
    }
}