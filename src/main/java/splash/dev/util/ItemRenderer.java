package splash.dev.util;

import net.minecraft.client.gui.DrawContext;
import splash.dev.data.ItemData;

import java.util.List;

import static splash.dev.BetterCpvp.mc;

public class ItemRenderer {
    List<ItemData> items;

    public ItemRenderer(List<ItemData> items) {
        this.items = items;
    }

    public void render(DrawContext context) {
        int yOffset = 5;
        int itemHeight = 13;

        for (ItemData itemData : items) {
            context.drawItem(itemData.item().getDefaultStack(), 5, yOffset);

            int textHeight = mc.textRenderer.fontHeight;
            int centeredY = yOffset + (itemHeight - textHeight) / 2;

            context.drawTextWithShadow(mc.textRenderer, String.valueOf(itemData.count()), 20, centeredY, -1);

            yOffset += itemHeight + 2;
        }
    }
}
