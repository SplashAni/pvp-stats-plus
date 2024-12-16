package splash.dev.ui.hud;

import net.minecraft.client.gui.DrawContext;

public abstract class HudElement  {
    public int x,y;
    int width, height;
    boolean dragging;
    boolean visible = true;

    public HudElement() {
        this.x = 1;
        this.y = 1;
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setY(int y) {
        this.y = y;
    }


    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }


    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void render(DrawContext context, int mouseX,int mouseY,float tickDelta) {
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void toggle() {
        this.visible = !visible;
    }
    public void setSize(int width,int height){
        this.width = width;
        this.height = height;
    }
}