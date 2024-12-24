package splash.dev.ui.hud;

import net.minecraft.client.gui.DrawContext;
import splash.dev.ui.hud.elements.IndicatorElement;
import splash.dev.ui.hud.elements.ScoreElement;
import splash.dev.ui.hud.elements.TimerElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static splash.dev.PVPStatsPlus.mc;

public class HudManager {

    private final List<HudElement> elements = new ArrayList<>();
    public boolean showHeads, blinking; // todo better system )=
    private int dragX, dragY;

    public HudManager(boolean addDefault) {

        if (addDefault) {
            elements.add(new ScoreElement());
            elements.add(new IndicatorElement());
            elements.add(new TimerElement());

            int offset = 0;

            for (HudElement element : elements) {
                element.setVisible(true);
                element.setCoords(1, offset);
                offset += 15;
            }
        }
        showHeads = true;
        blinking = false;
    }

    public void toggleBlinking() {
        this.blinking = !blinking;
    }

    public void toggleShowHeads() {
        this.showHeads = !showHeads;
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
        for (HudElement element : elements) {
            if (element.isVisible() && element.isHovered((int) mouseX, (int) mouseY)) {
                if (button == 0) {
                    element.setDragging(true);
                    dragX = (int) (mouseX - element.getX());
                    dragY = (int) (mouseY - element.getY());
                }
                if (button == 1) {
                    mc.setScreen(new ElementOptions(element));
                }
            }
        }
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

    public void toggleVisibility(Class<? extends HudElement> elementType) {
        for (HudElement element : elements) {
            if (elementType.isInstance(element)) {
                element.toggle();
            }
        }
    }

    public void reset(Class<? extends HudElement> elementType) {
        for (HudElement element : elements) {
            if (elementType.isInstance(element)) {
                element.setCoords(1, 0);
            }
        }
    }

    public boolean isVisible(Class<? extends HudElement> elementType) {
        for (HudElement element : elements) {
            if (elementType.isInstance(element)) {
                return element.isVisible();
            }
        }
        return false;
    }


    public void addElement(HudElement hudElement) {
        elements.add(hudElement);
    }
}