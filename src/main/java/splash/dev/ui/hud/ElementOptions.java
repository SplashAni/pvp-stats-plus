package splash.dev.ui.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import splash.dev.util.Renderer2D;

import java.util.List;

import static splash.dev.PVPStatsPlus.mc;

public class ElementOptions extends Screen {
    HudElement element;
    List<ButtonWidget> buttons;

    public ElementOptions(HudElement element) {
        super(Text.of(element.getName()));
        this.element = element;
        this.buttons = element.getButtons();
    }

    @Override
    protected void init() {
        buttons.forEach(this::addDrawable);
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        int[] options = Renderer2D.renderOptionsBox(context, element.getName().substring(0, 1).toUpperCase() + element.getName().substring(1) + " Element");

        int startY = options[1] + 5;

        for (int i = 0; i < buttons.size(); i++) {
            ButtonWidget button = buttons.get(i);
            button.setY(startY + (i * (button.getHeight() + 5)));
            button.setX(options[0] + 55);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (ButtonWidget widget : buttons) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                widget.onPress();
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) mc.setScreen(new HudEditor());
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

}
