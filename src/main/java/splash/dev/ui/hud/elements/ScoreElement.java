package splash.dev.ui.hud.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import splash.dev.ui.hud.HudElement;

import static splash.dev.PVPStatsPlus.mc;

public class ScoreElement extends HudElement {
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        String text = "1-0";

        int headSize = 16;
        int textWidth = mc.textRenderer.getWidth(text);
        int padding = 2;

        int totalWidth = headSize + textWidth + headSize + (2 * padding);
        int totalHeight = headSize;

        int offsetX = x;

        PlayerSkinDrawer.draw(context, mc.player.getSkinTextures(), offsetX, getY(), headSize);
        offsetX += headSize + padding;

        int textY = getY() + (headSize / 2 - mc.textRenderer.fontHeight / 2);
        context.drawTextWithShadow(mc.textRenderer, text, offsetX, textY, -1);
        offsetX += textWidth + padding;

        PlayerSkinDrawer.draw(context, mc.player.getSkinTextures(), offsetX, getY(), headSize);


        int rectX = x + totalWidth + 10;
        int rectY = getY();
        int rectWidth = 50;
        int rectHeight = 20;
        context.fill(rectX, rectY, rectX + rectWidth, rectY + rectHeight, 0x550000FF);

        String rectCoords = "Rect: " + rectX + ", " + rectY;
        context.drawTextWithShadow(mc.textRenderer, rectCoords, 5, 15, 0xFFFFFF);

        setSize(totalWidth, totalHeight);
    }
}
