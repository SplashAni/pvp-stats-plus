package splash.dev.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.PVPStatsPlus;
import splash.dev.ui.hud.HudEditor;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;


@Mixin(InGameHud.class)
public abstract class InGameHudMixin {


    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        if (mc.currentScreen instanceof HudEditor || getRecorder() == null) return;

        if (getRecorder().isRecording())
            PVPStatsPlus.getHudManager().render(context, 0, 0, tickCounter.getTickDelta(false));
    }
}
