package splash.dev.gui.content;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import splash.dev.gui.comp.RenderContent;

import static splash.dev.BetterCpvp.mc;

public class CpvpContent extends RenderContent {
    public CpvpContent() {

    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        context.drawTextWithShadow(mc.textRenderer, "nigga", getX(), getY(), -1);
        context.fill(getX(),getY(),getX()+getWidth(),getY()+getHeight(),-1);
        super.render(context, mouseX, mouseY);
    }

    @Override
    public void setTrackItems(Item... item) {
        super.setTrackItems(Items.ENDER_PEARL, Items.END_CRYSTAL);
    }
}
