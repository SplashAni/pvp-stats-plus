package splash.dev.gui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import splash.dev.data.MatchInfo;
import splash.dev.data.StoredMatchData;
import splash.dev.recording.ItemUsed;
import splash.dev.util.Renderer2D;
import java.awt.*;
import static splash.dev.BetterCpvp.mc;
public class MatchStatsGui {
    int id, y, width, height;
    MatchInfo match;
    public MatchStatsGui(int id, int y, int width, int height) {
        this.id = id;
        this.y = y;
        this.width = width;
        this.height = height;
        match = StoredMatchData.getMatchId(id);
    }
    public static void drawItem(DrawContext drawContext, ItemStack itemStack, int x, int y, float scale, boolean overlay, String countOverride) {
        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();
        matrices.scale(scale, scale, 1f);
        int scaledX = (int) (x / scale);
        int scaledY = (int) (y / scale);
        drawContext.drawItem(itemStack, scaledX, scaledY);
        if (overlay) drawContext.drawStackOverlay(mc.textRenderer, itemStack, scaledX, scaledY, countOverride);
        matrices.pop();
    }
    public void render(DrawContext context, int mouseX, int mouseY) {
        int x = (context.getScaledWindowWidth() - width) / 2;
        if (match == null) {
            String missingText = "Match info missing / corrupt?";
            int textWidth = mc.textRenderer.getWidth(missingText);
            int centeredX = x + (width - textWidth) / 2;
            context.drawText(mc.textRenderer, missingText, centeredX, y, -1, false);
            return;
        }
        String matchText = "Items used";
        int textWidth = mc.textRenderer.getWidth(matchText);
        int centeredX = x + (width - textWidth) / 2;
        int textY = y + 4;
        int gradientY = textY + mc.textRenderer.fontHeight / 2;
        Renderer2D.fillGradient(context,
                x, gradientY, centeredX, gradientY + 1,
                new Color(255, 255, 255, 0).getRGB(), new Color(255, 255, 255, 255).getRGB()
        );
        Renderer2D.fillGradient(context,
                centeredX + textWidth, gradientY, x + width, gradientY + 1,
                new Color(255, 255, 255, 255).getRGB(), new Color(255, 255, 255, 0).getRGB()
        );
        context.drawText(mc.textRenderer, matchText, centeredX, textY, Color.WHITE.getRGB(), false);
        int offsetY = textY + 20;
        for (ItemUsed itemUsed : match.getItemUsed()) {
            ItemStack itemStack = itemUsed.item();
            String itemName = itemStack.getName().getString();
            context.drawText(mc.textRenderer, itemName, x + 10, offsetY, -1, false);
            int itemIconX = x + width - 40;
            drawItem(context, itemStack, itemIconX, offsetY, 1.0f, true, String.valueOf(itemUsed.count()));
            offsetY += 20;
        }
    }
    public void updateY(int y) {
        this.y = y;
    }
    public void mouseScroll() {
    }
}