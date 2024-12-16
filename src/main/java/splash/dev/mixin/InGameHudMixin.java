package splash.dev.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.PVPStatsPlus;
import splash.dev.data.StoredMatchData;

import java.awt.*;

import static splash.dev.PVPStatsPlus.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    private int renderHealthValue;

    @Shadow
    public abstract void render(DrawContext context, RenderTickCounter tickCounter);

    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {


        if (recorder != null && PVPStatsPlus.getRecorder().getTarget() != null && PVPStatsPlus.isRenderScore()) {

        }



        if (PVPStatsPlus.recorder == null || !PVPStatsPlus.recorder.isRecording()) {
            context.drawText(mc.textRenderer, ".", 1, 1, new Color(57, 0, 255, 255).getRGB(), false);
        } else {
            context.drawText(mc.textRenderer, ".", 1, 1, new Color(255, 0, 0, 255).getRGB(), false);
        }
    }
}
