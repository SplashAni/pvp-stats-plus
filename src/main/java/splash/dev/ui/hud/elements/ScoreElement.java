package splash.dev.ui.hud.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.util.math.MatrixStack;
import splash.dev.data.StoredMatchData;
import splash.dev.ui.hud.HudElement;

import static splash.dev.PVPStatsPlus.getRecorder;
import static splash.dev.PVPStatsPlus.mc;

public class ScoreElement extends HudElement {


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (!isInEditor()) if (getRecorder() == null || !getRecorder().isRecording()) return;

        String text = isInEditor() ? "1-1" : StoredMatchData.getKDString(getRecorder().getTarget());

        int baseSize = 16;
        int headSize = (int) (baseSize * scale);
        int padding = (int) (2 * scale);

        int textWidth = (int) (mc.textRenderer.getWidth(text) * scale);
        int totalWidth = headSize + textWidth + headSize + (2 * padding);

        int offsetX = x;

        PlayerSkinDrawer.draw(context, mc.player.getSkinTextures(), offsetX, getY(), headSize);
        offsetX += headSize + padding;

        int textX = offsetX;
        int textY = getY() + (headSize / 2 - (int) (mc.textRenderer.fontHeight * scale) / 2);

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(scale, scale, 1);

        context.drawTextWithShadow(mc.textRenderer, text,
                (int) (textX / scale),
                (int) (textY / scale),
                -1);

        matrices.pop();
        offsetX += textWidth + padding;

        PlayerSkinDrawer.draw(context, mc.player.getSkinTextures(), offsetX, getY(), headSize);

        setSize(totalWidth, headSize);
    }

}
