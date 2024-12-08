package splash.dev.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.BetterCpvp;
import splash.dev.data.ItemInteractData;

import java.io.File;

import static splash.dev.BetterCpvp.mc;

@Mixin(MinecraftClient.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "setScreen", at = @At("HEAD"))
    public void onDeath(Screen screen, CallbackInfo ci) {
        if (screen instanceof DeathScreen) BetterCpvp.getInstance().setItemInteractData(new ItemInteractData());
    }
}
