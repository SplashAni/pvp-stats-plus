package splash.dev.data;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.text.Text;
import splash.dev.recording.MatchOutline;

import java.awt.*;

import static splash.dev.BetterCpvp.mc;

public class MatchInfo {
    Category category;
    MatchOutline matchOutline;

    public MatchInfo(Category category, MatchOutline matchOutline) {
        this.category = category;
        this.matchOutline = matchOutline;
    }

    public void render(DrawContext context, int y, int w, int mouseX, int mouseY) {
        int width = w - 20;
        int height = 30;
        int screenWidth = context.getScaledWindowWidth();
        int outline = new Color(89, 89, 89, 255).getRGB();
        int centerX = (screenWidth - width) / 2;
        int centerY = y + (height - 20) / 2;

        boolean isHovered = mouseX >= centerX && mouseX <= centerX + width &&
                mouseY >= y && mouseY <= y + height;

        int backgroundColor = isHovered ? new Color(120, 120, 120, 255).getRGB() : new Color(103, 103, 103, 255).getRGB();

        context.fill(centerX, y, centerX + width, y + height, backgroundColor);

        int outlineColor = isHovered ? new Color(150, 150, 150, 255).getRGB() : outline;
        int outlineThickness = 1;

        context.fill(centerX - outlineThickness, y - outlineThickness,
                centerX + width + outlineThickness, y + height + outlineThickness, 0, outlineColor);
        context.fillGradient(centerX, y, centerX + width, y + height,
                new Color(50, 50, 50, 255).getRGB(), new Color(65, 63, 63, 255).getRGB());

        int skinX = centerX + 5;
        int skinWidth = 20;

        assert mc.player != null;
        PlayerSkinDrawer.draw(context, mc.player.getSkinTextures(), skinX, centerY, skinWidth);
        int textX = skinX + skinWidth + 5;
        int textY = centerY + 5;
        String playerName = matchOutline.getName();
        context.drawText(mc.textRenderer, playerName, textX, textY, -1, true);
        int playerNameWidth = mc.textRenderer.getWidth(playerName);
        textX += playerNameWidth + 20;
        String totalDamage = "Used items: " + matchOutline.usedItems();
        context.drawText(mc.textRenderer, totalDamage, textX, textY, new Color(200, 200, 200).getRGB(), false);


        if (mouseX >= skinX && mouseX <= skinX + skinWidth && mouseY >= centerY && mouseY <= centerY + skinWidth) {
            context.drawTooltip(mc.textRenderer, Text.of("Duration: " + String.format("%.2f", matchOutline.getTime()) + "s"), mouseX, mouseY);
        }

        textX = centerX + width - 50;
        context.drawText(mc.textRenderer, "Lost", textX, textY, new Color(255, 100, 100).getRGB(), true);
    }

}
