package splash.dev.ui.hud.elements;

import net.minecraft.client.gui.DrawContext;
import splash.dev.ui.hud.HudElement;
import splash.dev.util.Renderer2D;

public class IndicatorElement extends HudElement {
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        int size = (int) (20 * scale);
        Renderer2D.renderIndicator(context, x, y, size);
        setSize(size, size);
        super.render(context, mouseX, mouseY, tickDelta);
    }
}
