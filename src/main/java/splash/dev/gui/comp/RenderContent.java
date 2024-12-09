package splash.dev.gui.comp;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import splash.dev.data.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RenderContent {
    Category category;
    List<Item> trackItems;
    int x, y, width, height;

    public RenderContent() {
        trackItems = new ArrayList<>();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {

    }

    public void mouseRelease(int mouseX, int mouseY) {

    }

    public void setTrackItems(Item... item) {
        Collections.addAll(trackItems, item);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setCoords(int contentX, int contentY) {
        this.x = contentX;
        this.y = contentY;
    }

    public void setBounds(int width, int height) {
        this.width = width;
        this.height = height;
    }
}