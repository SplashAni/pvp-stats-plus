package splash.dev.ui.hud.elements;

import net.minecraft.client.gui.DrawContext;
import splash.dev.ui.hud.HudElement;
import splash.dev.util.Renderer2D;

public class IndicatorElement extends HudElement {

    public IndicatorElement() {
        super("indicator");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {


        Renderer2D.renderIndicator(context, x, y, (int) (20 * scale));
        setSize((int) (20 * scale), (int) (20 * scale));

        super.render(context, mouseX, mouseY, tickDelta);
    }
}
