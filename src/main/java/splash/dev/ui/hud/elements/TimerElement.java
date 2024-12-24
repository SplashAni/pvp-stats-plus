package splash.dev.ui.hud.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import splash.dev.ui.hud.HudElement;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

public class TimerElement extends HudElement {
    public TimerElement() {
        super("timer");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        String text = isInEditor() ? getRecorder().getFormattedTime() : "4:2";
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(scale, scale, 1);

        context.drawTextWithShadow(mc.textRenderer, text,
                (int) (x / scale),
                (int) (y / scale),
                -1);

        matrices.pop();

        setSize((int) (mc.textRenderer.getWidth(text) * scale), (int) (mc.textRenderer.fontHeight * scale));

        super.render(context, mouseX, mouseY, tickDelta);
    }
}
