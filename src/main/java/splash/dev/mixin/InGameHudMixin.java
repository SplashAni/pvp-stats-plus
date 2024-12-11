package splash.dev.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.BetterCpvp;

import java.awt.*;

import static splash.dev.BetterCpvp.mc;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow public abstract void render(DrawContext context, RenderTickCounter tickCounter);

    @Inject(method = "render",at = @At("HEAD"))
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if (BetterCpvp.recorder == null || !BetterCpvp.recorder.isRecording()) {
            context.drawText(mc.textRenderer,"not recording ",1,1,new Color(57, 0, 255,255).getRGB(),false);
        } else {
            context.drawText(mc.textRenderer,"recording",1,1,new Color(255, 0, 0,255).getRGB(),false);
        }
    }
}
