package splash.dev.data;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Objects;

public class MatchsDataRenderer {
    Category category;
    int width, height, x, y;
    int scrollOffset = 0;

    public MatchsDataRenderer(Category category) {
        this.category = category;
    }

    public void setBounds(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void render(DrawContext context,int mouseX, int mouseY) {
        if(MatchData.getMatchDataInCategory(category).isEmpty()) return;

        int scissorsWidth = width;
        int scissorsHeight = height;
        int scissorsX = x;
        int scissorsY = y;

        int offset = y + 5 + scrollOffset;
        MatrixStack matrices = context.getMatrices();
        matrices.push();

        context.enableScissor(scissorsX, scissorsY, scissorsX + scissorsWidth, scissorsY + scissorsHeight);

        for (MatchInfo matchInfo : Objects.requireNonNull(MatchData.getMatchDataInCategory(category))) {
            matchInfo.render(context, offset,width, mouseX, mouseY);
            offset += 40;
        }

        context.disableScissor();

        matrices.pop();
    }


    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if(MatchData.getMatchDataInCategory(category).isEmpty()) return;


        int scrollSpeed = 30;
        if (verticalAmount > 0) {
            scrollOffset += scrollSpeed;
        } else {
            scrollOffset -= scrollSpeed;
        }

        int maxScroll = getTotalContentHeight() - height;
        if (scrollOffset > 0) {
            scrollOffset = 0;
        } else if (scrollOffset < -maxScroll) {
            scrollOffset = -maxScroll;
        }


    }

    private int getTotalContentHeight() {
        return 40 * Objects.requireNonNull(MatchData.getMatchDataInCategory(category)).size();
    }
}
