package splash.dev.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

@Mixin(ItemStack.class)
public class ItemStackMixin {


    @Inject(method = "onStoppedUsing", at = @At("HEAD"))
    public void onStoppedUsing(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user.equals(mc.player)) if ((Object) this instanceof ItemStack i) updateRecorder(i);
    }

    @Inject(method = "decrement", at = @At("HEAD"))
    public void ye(int amount, CallbackInfo ci) {
        mc.inGameHud.getChatHud().addMessage(Text.of("used deceremnt " + this));
    }

    @Unique
    public void updateRecorder(ItemStack hand) {
        if (getRecorder() == null || !getRecorder().isRecording()) return;
        getRecorder().updateItem(hand);
    }
}
