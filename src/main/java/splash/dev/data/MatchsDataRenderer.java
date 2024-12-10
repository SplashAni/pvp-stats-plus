package splash.dev.data;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import splash.dev.gui.MatchStatsGui;

import java.util.Objects;

public class MatchsDataRenderer {
    Category category;
    int width, height, x, y;
    int scrollOffset = 0;
    boolean renderingMatchStats;
    MatchStatsGui matchStatsGui;

    public MatchsDataRenderer(Category category) {
        this.category = category;
    }

    public void setBounds(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        if (StoredMatchData.getMatchDataInCategory(category).isEmpty()) return;

        int scissorsWidth = width;
        int scissorsHeight = height;
        int scissorsX = x;
        int scissorsY = y;

        int offset = y + 5 + scrollOffset;
        MatrixStack matrices = context.getMatrices();
        matrices.push();

        context.enableScissor(scissorsX, scissorsY, scissorsX + scissorsWidth, scissorsY + scissorsHeight);

        if (renderingMatchStats) {
            if (matchStatsGui != null) {
                matchStatsGui.render(context, mouseX, mouseY);
            }
        } else {
            for (MatchInfo matchInfo : Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(category))) {
                matchInfo.render(context, offset, width, mouseX, mouseY);
                offset += 40;
            }
        }

        context.disableScissor();

        matrices.pop();
    }


    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (StoredMatchData.getMatchDataInCategory(category).isEmpty()) return;


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
        if(matchStatsGui != null){
            matchStatsGui.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

    }

    public void mouseRelease(int button, int mouseX, int mouseY) {
        if (button == 0) {
            int offset = y + 5 + scrollOffset;

            for (MatchInfo matchInfo : Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(category))) {
                int matchInfoTop = offset;
                int matchInfoBottom = offset + 40;

                if (mouseY >= matchInfoTop && mouseY <= matchInfoBottom) {
                    renderingMatchStats = true;
                    matchStatsGui = new MatchStatsGui(matchInfo.matchOutline.getId(), y, width, height);
                    return;
                }

                offset += 40;
            }
        }
    }

    private int getTotalContentHeight() {
        return 40 * Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(category)).size();
    }
}
