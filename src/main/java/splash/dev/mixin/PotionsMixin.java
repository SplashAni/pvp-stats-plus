package splash.dev.mixin;

import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import splash.dev.PVPStatsPlus;

@Mixin(Potions.class)
public class PotionsMixin {
    @Inject(method = "register",at = @At("RETURN"))
    private static void registerAndGetDefault(String name, Potion potion, CallbackInfoReturnable<RegistryEntry<Potion>> cir){
     PVPStatsPlus.potions.add(cir.getReturnValue());
    }
}
