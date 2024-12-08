package splash.dev.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.data.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.BetterCpvp;
import splash.dev.data.ItemData;
import splash.dev.data.ItemInteractData;
import splash.dev.util.ItemRenderer;

import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow public abstract void render(DrawContext context, RenderTickCounter tickCounter);

    @Inject(method = "render",at = @At("HEAD"))
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        List<ItemData> itemData = BetterCpvp.getInstance().getItemInteractData().getAllItems();
        ItemRenderer renderer = new ItemRenderer(itemData);
        renderer.render(context);
    }
}
