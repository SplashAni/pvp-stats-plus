package splash.dev.ui.gui.menus;

import net.minecraft.client.gui.DrawContext;
import splash.dev.data.Gamemode;
import splash.dev.data.StoredMatchData;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static splash.dev.PVPStatsPlus.mc;

public class PlayerStatsMenu {
    private final int y;
    private final int width;
    private final int height;
    private final int kills;
    private final int deaths;
    private final Gamemode mostPlayed;
    private int scrollOffset;

    public PlayerStatsMenu(String username, int y, int width, int height) {
        this.y = y;
        this.width = width;
        this.height = height;

        Map<Gamemode, Integer> categoryCountMap = new HashMap<>();

        StoredMatchData.getMatches()
                .stream()
                .filter(match -> Objects.equals(match.getMatchOutline().getUsername(), username))
                .forEach(match -> {
                    Gamemode gamemode = match.getCategory();
                    categoryCountMap.put(gamemode, categoryCountMap.getOrDefault(gamemode, 0) + 1);
                });

        mostPlayed = categoryCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        int[] kd = StoredMatchData.getKD(username);
        kills = kd[0];
        deaths = kd[1];

    }

    private String calculateTier() {
        double kdRatio = (deaths == 0) ? kills : (double) kills / deaths;


        double normalizedKd = Math.min(5.0, kdRatio);
        normalizedKd = Math.max(0.0, normalizedKd);

        int tier = (int) Math.ceil(normalizedKd);

        if (tier <= 2) {
            return "LT" + tier;
        } else {
            return "HT" + (tier - 2);
        }
    }

    private void renderStatBox(DrawContext context, String label, String value, int yPosition, int color, int textColor) {
        int x = (context.getScaledWindowWidth() - width) / 2;

        int statBoxHeight = 30;
        context.fill(x, yPosition, x + width, yPosition + statBoxHeight, color);
        context.drawTextWithShadow(mc.textRenderer, label, x + 10, yPosition + 10, textColor);
        context.drawTextWithShadow(mc.textRenderer, value, x + width - 10 - mc.textRenderer.getWidth(value), yPosition + 10, 0xFFFFFF);
    }

    public void render(DrawContext context) {
        int x = (context.getScaledWindowWidth() - width) / 2;
        int currentY = this.y + scrollOffset;

        int color = new Color(255, 160, 64, 255).getRGB();
        int color1 = new Color(86, 86, 86, 100).getRGB();
        int color2 = new Color(48, 48, 48, 100).getRGB();

        int headingHeight = 30;
        context.fill(x, currentY, x + width, currentY + headingHeight, color1);
        context.drawTextWithShadow(mc.textRenderer, "Player Stats", x + (width / 2) - (mc.textRenderer.getWidth("Player Stats") / 2), currentY + 10, 0xFFFFFF);
        currentY += headingHeight + 5;

        String tier = calculateTier();
        renderStatBox(context, "Tier", tier, currentY, color2, color);
        currentY += 35;

        renderStatBox(context, "K/D", String.format("%d/%d", kills, deaths), currentY, color2, color1);
        currentY += 35;

        renderStatBox(context, "Ratio", String.format("%.2f", (deaths == 0) ? kills : (double) kills / deaths), currentY, color2, color);
        currentY += 35;

        renderStatBox(context, "Most Played", mostPlayed.name(), currentY, color2, new Color(25, 236, 255, 255).getRGB());
    }

    public void mouseScrolled(double verticalAmount) {
        scrollOffset += (int) (verticalAmount * 10);
        scrollOffset = Math.min(scrollOffset, 0);
        int contentHeight = 170;
        scrollOffset = Math.max(scrollOffset, height - contentHeight);
    }
}
