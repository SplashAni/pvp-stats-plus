package splash.dev.ui.gui.menus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.StatFormatter;
import splash.dev.data.MatchesMenu;
import splash.dev.data.StoredMatchData;
import splash.dev.recording.infos.ItemUsed;
import splash.dev.util.ItemHelper;
import splash.dev.util.Renderer2D;

import java.awt.*;

import static splash.dev.PVPStatsPlus.mc;

public class MatchResultMenu {
    int id, y, width, height;
    MatchesMenu match;
    private int scrollOffset;

    public MatchResultMenu(int id, int y, int width, int height) {
        this.id = id;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scrollOffset = 0;
        match = StoredMatchData.getMatchId(id);
    }

    public static void drawUsedItem(DrawContext context, ItemUsed item, int x, int y, float size) {
        ItemStack damaged = item.item();

        damaged.setDamage(item.count());

        drawItem(context, damaged, x, y, size, "");
    }

    public static void drawItem(DrawContext drawContext, ItemStack itemStack, int x, int y, float size, String count) {
        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();
        matrices.scale(size, size, 1f);
        int scaledX = (int) (x / size);
        int scaledY = (int) (y / size);


        drawContext.drawItem(itemStack, scaledX, scaledY);

        drawContext.drawItemInSlot(mc.textRenderer, itemStack, scaledX, scaledY, count);
        matrices.pop();
    }

    private void renderText(DrawContext context, String label, String value, int x, int y, int width) {
        int rightEdgeX = x + width - mc.textRenderer.getWidth(value) - 28;
        context.drawText(mc.textRenderer, label, x + 10, y, Color.WHITE.getRGB(), false);
        context.drawText(mc.textRenderer, value, rightEdgeX, y, Color.WHITE.getRGB(), false);
    }

    private void renderHeading(DrawContext context, String headingText, int x, int y, int width) {
        int textWidth = mc.textRenderer.getWidth(headingText);
        int centeredX = x + (width - textWidth) / 2;
        int gradientY = y + mc.textRenderer.fontHeight / 2;
        Renderer2D.fillHorizontalGradient(context,
                x, gradientY, centeredX, gradientY + 1,
                new Color(255, 255, 255, 0).getRGB(), new Color(255, 255, 255, 255).getRGB()
        );
        Renderer2D.fillHorizontalGradient(context,
                centeredX + textWidth, gradientY, x + width, gradientY + 1,
                new Color(255, 255, 255, 255).getRGB(), new Color(255, 255, 255, 0).getRGB()
        );
        context.drawText(mc.textRenderer, headingText, centeredX, y, Color.WHITE.getRGB(), false);
    }

    public void render(DrawContext context) {
        int y = this.y - scrollOffset;
        int x = (context.getScaledWindowWidth() - width) / 2;
        int topMargin = 9;
        if (match == null) {
            String missingText = "Match info missing / corrupt?";
            int textWidth = mc.textRenderer.getWidth(missingText);
            int centeredX = x + (width - textWidth) / 2;
            context.drawText(mc.textRenderer, missingText, centeredX, y + topMargin, -1, false);
            return;
        }
        int currentY = y + topMargin;
        renderHeading(context, "Items used", x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;
        int itemCount = match.getItemUsed().size();
        int index = 0;

        for (ItemUsed itemUsed : match.getItemUsed()) {
            ItemStack itemStack = itemUsed.item();
            String itemName = itemStack.getName().getString();
            context.drawText(mc.textRenderer, itemName, x + 10, currentY, -1, false);
            int itemIconX = x + width - 40;
            if (ItemHelper.isWeapon(itemStack)) drawUsedItem(context, itemUsed, itemIconX, currentY, 1.0f);
            else
                drawItem(context, itemStack, itemIconX, currentY, 1.0f, String.valueOf(itemUsed.count()));

            currentY += (index == itemCount - 1) ? 10 : 20;
            index++;
        }

        currentY += topMargin;
        renderHeading(context, "Damage", x, currentY, width);

        String dealtDamage = String.valueOf(match.getDamageInfo().getDealtDamage() / 100);

        String damageTaken = String.valueOf(match.getDamageInfo().getDamageTaken() / 100);

        String damageBlocked = String.valueOf(match.getDamageInfo().getDamageBlocked() / 100);

        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Damage Dealt: ", dealtDamage, x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Damage Taken: ", damageTaken, x, currentY, width);

        currentY += mc.textRenderer.fontHeight + topMargin;


        renderText(context, "Damage Blocked: ", damageBlocked, x, currentY, width);
        currentY += topMargin;

        renderHeading(context, "Attack", x, currentY, width);
        String crits = String.valueOf(match.getAttackInfo().getCrits());
        String misses = String.valueOf(match.getAttackInfo().getMisses());

        String longestCombo = String.valueOf(match.getAttackInfo().getLongestCombo());
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Crits: ", crits, x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Misses: ", misses, x, currentY, width);

        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Longest Combo: ", longestCombo, x, currentY, width);
        currentY += topMargin;

        renderHeading(context, "Distance", x, currentY, width);

        currentY += mc.textRenderer.fontHeight + topMargin;


        String sprinted = StatFormatter.DISTANCE.format(match.getDistanceInfo().getDistanceSprinted());

        String crouched = StatFormatter.DISTANCE.format(match.getDistanceInfo().getDistanceCrouched());

        String walked = StatFormatter.DISTANCE.format(match.getDistanceInfo().getDistanceWalked());


        renderText(context, "Distance Walked: ", walked, x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Distance Sprinted: ", sprinted, x, currentY, width);

        currentY += mc.textRenderer.fontHeight + topMargin;


        renderText(context, "Distance Crouched: ", crouched, x, currentY, width);
        currentY += topMargin;

        renderHeading(context, "Arrow", x, currentY, width);

        String shot = String.valueOf(match.getArrowInfo().getArrowsShot());

        String longest = StatFormatter.DISTANCE.format(match.getArrowInfo().getLongestArrowShot());

        String accuracy = String.valueOf(match.getArrowInfo().getAccuracy()).concat("%");

        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Arrows Shot: ", shot, x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Longest Shot Distance: ", longest, x, currentY, width);

        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Accuracy: ", accuracy, x, currentY, width);

        currentY += mc.textRenderer.fontHeight + topMargin;

        renderHeading(context, "Match", x, currentY, width);

        String formattedTime = getMinutes(match.getMatchOutline().getTime());

        String matchId = String.valueOf(match.getMatchOutline().getId());
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Time: ", formattedTime, x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Match ID: ", matchId, x, currentY, width);
        currentY += topMargin;

        renderHeading(context, "", x, currentY, width);
    }

    public String getMinutes(float seconds) {
        double minutes = seconds / 60.0;
        return String.format("%.1f minutes", minutes);
    }

    public void mouseScrolled(double verticalAmount) {
        int contentHeight = calculateContentHeight();

        scrollOffset -= (int) (verticalAmount * 10);
        scrollOffset = Math.max(0, scrollOffset);
        scrollOffset = Math.min(scrollOffset, contentHeight - height);
    }

    private int calculateContentHeight() {
        int contentHeight = 0;
        int topMargin = 9;

        if (match != null) {
            int itemCount = match.getItemUsed().size();
            contentHeight += (mc.textRenderer.fontHeight + topMargin) * (itemCount + 1);
            contentHeight += mc.textRenderer.fontHeight * 3 + topMargin * 4;
            contentHeight += mc.textRenderer.fontHeight * 8 + topMargin * 9;
            contentHeight += mc.textRenderer.fontHeight * 4 + topMargin * 5;
            contentHeight += mc.textRenderer.fontHeight * 2 + topMargin * 3;

        }

        return contentHeight - 13;
    }

}