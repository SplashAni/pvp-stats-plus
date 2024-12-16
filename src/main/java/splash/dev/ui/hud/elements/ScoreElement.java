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

        int offsetX = x;

        PlayerSkinDrawer.draw(context, mc.player.getSkinTextures(), offsetX, getY(), headSize);
        offsetX += headSize + padding;

        int textY = getY() + (headSize / 2 - mc.textRenderer.fontHeight / 2);
        context.drawTextWithShadow(mc.textRenderer, text, offsetX, textY, -1);
        offsetX += textWidth + padding;

        PlayerSkinDrawer.draw(context, mc.player.getSkinTextures(), offsetX, getY(), headSize);

        setSize(totalWidth, 16);
    }
}
