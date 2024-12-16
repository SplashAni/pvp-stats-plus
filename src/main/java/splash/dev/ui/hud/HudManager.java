package splash.dev.ui.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import splash.dev.ui.hud.elements.ScoreElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HudManager {

    private final List<HudElement> elements = new ArrayList<>();
    private int dragX, dragY;

    public HudManager() {
        elements.add(new ScoreElement());
        int offset = 0;

        for (HudElement element : elements) {

            element.setCoords(1, offset);
            offset += 10;
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        elements.stream().filter(element -> element.visible).forEach(element -> {
            MatrixStack matrices = context.getMatrices();

            matrices.push();

            matrices.translate(element.x, element.y, 0);
            matrices.scale(element.scale, element.scale, 1.0f);

            if (element.isHovered(mouseX, mouseY)) {
                context.fill(0, 0, element.width, element.height, new Color(255, 255, 255, 132).getRGB());
                context.fill(-1, -1, element.width + 1, element.height + 1, new Color(255, 255, 255).getRGB());
            }

            element.render(context, mouseX - element.x, mouseY - element.y, delta);


            if (element.dragging) {
                element.setCoords(mouseX - dragX, mouseY - dragY);
            }

            matrices.pop();
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


    public void toggleVisibility(HudElement element) {
        for (HudElement element1 : elements) {
            if (element1 == element) element1.toggle();
        }
    }
}