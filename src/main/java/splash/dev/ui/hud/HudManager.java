package splash.dev.ui.hud;

import net.minecraft.client.gui.DrawContext;
import splash.dev.ui.hud.elements.IndicatorElement;
import splash.dev.ui.hud.elements.ScoreElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HudManager {

    private final List<HudElement> elements = new ArrayList<>();
    private int dragX, dragY;

    public HudManager(boolean addDefault) {

        if (addDefault) {
            elements.add(new ScoreElement());
            elements.add(new IndicatorElement());

            int offset = 0;

            for (HudElement element : elements) {

                element.setCoords(1, offset);
                offset += 15;
            }
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        elements.stream().filter(element -> element.visible).forEach(element -> {

            if (element.isHovered(mouseX, mouseY)) {
                context.fill(element.x - 1, element.y - 1, element.x + element.width + 1, element.y + element.height + 1, new Color(255, 255, 255).getRGB());
                context.fill(element.x, element.y, element.x + element.width, element.y + element.height, new Color(255, 255, 255, 132).getRGB());

            }

            element.render(context, mouseX, mouseY, delta);


            if (element.dragging) {
                element.setCoords(mouseX - dragX, mouseY - dragY);
            }
        });

    }


    public void mouseClicked(double mouseX, double mouseY, int button) {
        elements.stream().filter(element -> element.isVisible() && element.isHovered((int) mouseX, (int) mouseY))
                .filter(element -> button == 0)
                .forEachOrdered(element -> {
                    element.setDragging(true);
                    dragX = (int) (mouseX - element.getX());
                    dragY = (int) (mouseY - element.getY());
                });
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        elements.forEach(hudElement -> hudElement.setDragging(false));
    }

    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        elements.forEach(hudElement -> {
            if (hudElement.isHovered((int) mouseX, (int) mouseY)) {
                if (verticalAmount > 0) {
                    hudElement.scale = Math.min(2.0f, hudElement.scale + 0.1f);
                } else if (verticalAmount < 0) {
                    hudElement.scale = Math.max(0.1f, hudElement.scale - 0.1f);
                }
            }
        });
    }


    public List<HudElement> getElements() {
        return elements;
    }

    public void toggleVisibility(HudElement element) {
        for (HudElement element1 : elements) {
            if (element1 == element) element1.toggle();
        }
    }

    public void addElement(HudElement hudElement) {
        elements.add(hudElement);
    }
}