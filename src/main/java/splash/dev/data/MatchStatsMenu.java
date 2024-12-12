package splash.dev.data;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.text.Text;
import splash.dev.recording.AttackInfo;
import splash.dev.recording.DamageInfo;
import splash.dev.recording.ItemUsed;
import splash.dev.recording.MatchOutline;

import java.awt.*;
import java.util.List;

import static splash.dev.PVPStatsPlus.mc;

public class MatchStatsMenu {
    Category category;
    MatchOutline matchOutline;
    List<ItemUsed> itemUsed;
    DamageInfo damageInfo;
    AttackInfo attackInfo;
    boolean headHovered;

    public MatchStatsMenu(Category category, MatchOutline matchOutline, List<ItemUsed> itemUsed, DamageInfo damageInfo, AttackInfo attackInfo) {
        this.category = category;
        this.matchOutline = matchOutline;
        this.itemUsed = itemUsed;
        this.damageInfo = damageInfo;
        this.attackInfo = attackInfo;
    }

    public java.util.List<ItemUsed> getItemUsed() {
        return itemUsed;
    }

    public MatchOutline getMatchOutline() {
        return matchOutline;
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



        PlayerSkinDrawer.draw(context, getMatchOutline().getTarget().getSkinTextures(), skinX, centerY, skinWidth);
        int textX = skinX + skinWidth + 5;
        int textY = centerY + 5;
        String name = matchOutline.getTarget().getGameProfile().getName();
        context.drawText(mc.textRenderer, name, textX, textY, -1, true);
        int playerNameWidth = mc.textRenderer.getWidth(name);
        textX += playerNameWidth + 20;
        String totalDamage = "Used items: " + matchOutline.usedItems();
        context.drawText(mc.textRenderer, totalDamage, textX, textY, new Color(200, 200, 200).getRGB(), false);


        headHovered = mouseX >= skinX && mouseX <= skinX + skinWidth && mouseY >= centerY && mouseY <= centerY + skinWidth;
        if (headHovered) {
            context.drawTooltip(mc.textRenderer, Text.of("Click to view player"), mouseX, mouseY);
        }

        textX = centerX + width - 50;
        context.drawText(mc.textRenderer, matchOutline.isWon() ? "Won" : "Lost",
                textX, textY, matchOutline.isWon() ? new Color(64, 187, 86, 255).getRGB() : new Color(255, 100, 100).getRGB(), true);
    }


    public Category getCategory() {
        return category;
    }

    public DamageInfo getDamageInfo() {
        return damageInfo;
    }

    public AttackInfo getAttackInfo() {
        return attackInfo;
    }

    public boolean isHeadHover() {
        return headHovered;
    }
}
