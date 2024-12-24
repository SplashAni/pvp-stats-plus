package splash.dev.ui.hud.elements;

import net.minecraft.client.gui.DrawContext;
import splash.dev.PVPStatsPlus;
import splash.dev.ui.hud.HudElement;
import splash.dev.util.Renderer2D;

public class IndicatorElement extends HudElement {

    private boolean isVisible = true;
    private long lastToggleTime = 0;

    public IndicatorElement() {
        super("indicator");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        boolean blinking = PVPStatsPlus.getHudManager().blinking;

        if (blinking) {
            long currentTime = System.currentTimeMillis();
            int blinkInterval = 600;
            if (currentTime - lastToggleTime > blinkInterval) {
                isVisible = !isVisible;
                lastToggleTime = currentTime;
            }
        } else {
            isVisible = true;
        }

        if (!isVisible) return;

        Renderer2D.renderIndicator(context, x, y, (int) (20 * scale));
        setSize((int) (20 * scale), (int) (20 * scale));

        super.render(context, mouseX, mouseY, tickDelta);
    }
}
