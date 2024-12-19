package splash.dev.data;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import splash.dev.ui.gui.menus.MatchesMenu;
import splash.dev.ui.gui.menus.PlayerStatsMenu;

import java.util.List;
import java.util.Objects;

public class MenuRenderer {
    Gamemode gamemode;
    int width, height, x, y;
    int scrollOffset = 0;
    MatchesMenu matchesStats;
    PlayerStatsMenu playerStats;
    Menu menu;

    public MenuRenderer(Gamemode gamemode) {
        this.gamemode = gamemode;
        menu = Menu.Matches;
    }

    public void setBounds(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        if (StoredMatchData.getMatchDataInCategory(gamemode).isEmpty()) return;

        int scissorsWidth = width;
        int scissorsHeight = height;
        int scissorsX = x;
        int scissorsY = y;

        int offset = y + 10 + scrollOffset;
        MatrixStack matrices = context.getMatrices();
        matrices.push();

        context.enableScissor(scissorsX, scissorsY, scissorsX + scissorsWidth, scissorsY + scissorsHeight);

        switch (menu) {
            case Matches -> {
                for (MatchStatsMenu matchStatsMenu : Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode))) {
                    matchStatsMenu.render(context, offset, width, mouseX, mouseY);
                    offset += 40;
                }

            }
            case MatchStats -> {
                if (matchesStats != null) {
                    matchesStats.render(context);
                }
            }
            case PlayerStats -> {
                if (playerStats != null) {
                    playerStats.render(context);
                }
            }
        }


        context.disableScissor();


        matrices.pop();
    }


    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        List<MatchStatsMenu> stats = StoredMatchData.getMatchDataInCategory(gamemode);

        if (stats.size() >= 4) {
            int scrollSpeed = 30;
            if (verticalAmount > 0) scrollOffset += scrollSpeed;
            else scrollOffset -= scrollSpeed;

            int maxScroll = getTotalContentHeight() - height;
            if (scrollOffset > 0) scrollOffset = 0;
            else if (scrollOffset < -maxScroll) scrollOffset = -maxScroll;
        }


        if (matchesStats != null) {
            matchesStats.mouseScrolled(verticalAmount);
        }
        if (playerStats != null) {
            playerStats.mouseScrolled(verticalAmount);
        }
    }

    public void mouseRelease(int button, int mouseX, int mouseY) {
        if (button == 0) {


            int offset = y + 10 + scrollOffset;

            for (MatchStatsMenu matchStatsMenu : Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode))) {
                if (matchStatsMenu.headHovered && matchStatsMenu.getMatchOutline().getSkin() != null) {
                    menu = Menu.PlayerStats;
                    playerStats = new PlayerStatsMenu(matchStatsMenu
                            .matchOutline.getUsername(), y, width, height);
                    return;
                }


                int matchInfoTop = offset;
                int matchInfoBottom = offset + 40;


                if (mouseY >= matchInfoTop && mouseY <= matchInfoBottom) {
                    menu = Menu.MatchStats;
                    matchesStats = new MatchesMenu(matchStatsMenu.matchOutline.getId(), y, width, height);
                    return;
                }

                offset += 40;
            }
        }
    }

    private int getTotalContentHeight() {
        return 40 * Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode)).size();
    }

    private enum Menu {
        Matches,
        MatchStats,
        PlayerStats
    }
}
