package splash.dev.ui.recorder;

import net.minecraft.client.gui.DrawContext;
import splash.dev.PVPStatsPlus;
import splash.dev.data.Gamemode;
import splash.dev.recording.Recorder;

import java.awt.*;

import static splash.dev.PVPStatsPlus.mc;
import static splash.dev.ui.gui.menus.MatchesMenu.drawItem;

public class GameModeBox {
    Gamemode gamemode;
    int width = 50, height = 30;
    boolean hovered = false;

    public GameModeBox(Gamemode gamemode, int x, int y) {
        this.gamemode = gamemode;
    }

    public void render(DrawContext context, int x, int y, int mouseX, int mouseY) {
        hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        context.fill(x - 1, y - 1, x + width + 1, y + height + 14, hovered ? new Color(204, 204, 204, 255).getRGB() : new Color(129, 129, 129, 255).getRGB()); // Top border

        context.fill(x, y, x + width, y + height, new Color(51, 51, 51, 255).getRGB());

        float scale = 1.5f;
        int itemSize = (int) (16 * scale);
        int itemX = x + (width - itemSize) / 2;
        int itemY = y + (height - itemSize) / 2;
        drawItem(context, gamemode.getItemStack(), itemX, itemY, scale, "");

        context.fillGradient(x, y + height, x + width, y + height + 13,
                new Color(51, 51, 51, 255).getRGB(), new Color(84, 84, 84, 255).getRGB());

        String text = gamemode.name();
        int textWidth = mc.textRenderer.getWidth(text);
        int textHeight = mc.textRenderer.fontHeight;

        int textX = x + (width - textWidth) / 2;
        int textY = y + height + (13 - textHeight) / 2;
        context.drawText(mc.textRenderer, text, textX, textY, -1, true);
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (hovered && button == 0) {
            PVPStatsPlus.resetRecorder(false);
            PVPStatsPlus.getRecorder().startRecording(gamemode);
            mc.setScreen(null);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
