package splash.dev.data;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import splash.dev.data.gamemode.Gamemode;
import splash.dev.ui.gui.MainGui;
import splash.dev.ui.gui.menus.MatchesMenu;
import splash.dev.ui.gui.menus.PlayerStatsMenu;

import java.util.List;
import java.util.Objects;

import static splash.dev.PVPStatsPlus.mc;

public class MenuRenderer {
    Gamemode gamemode;
    int width, height, x, y, mouseY;
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
        this.mouseY = mouseY;
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

    public void keyRelease(int key) {
        if (key == GLFW.GLFW_KEY_DELETE && menu == Menu.Matches) {
            int offset = y + 10 + scrollOffset;

            for (MatchStatsMenu matchStatsMenu :
                    Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode))) {

                int matchInfoTop = offset;
                int matchInfoBottom = offset + 40;


                if (mouseY >= matchInfoTop && mouseY <= matchInfoBottom) {
                    mc.setScreen(new ConfirmScreen(result -> {
                        if (result) StoredMatchData.removeMatchID(matchStatsMenu.getMatchOutline().getId());
                        mc.setScreen(null);
                    }, Text.literal("Game " + matchStatsMenu.gamemode.name() + " confirm deletion."),
                            Text.literal("Are you sure you want to delete game #" + matchStatsMenu.getMatchOutline().getId())));
                    return;
                }


                offset += 40;
            }
        }
    }

    public void mouseRelease(int button, int mouseX, int mouseY) {
        if (button == 0) {


            int offset = y + 10 + scrollOffset;

            for (MatchStatsMenu matchStatsMenu :
                    Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode))) {
                if (matchStatsMenu.headHovered && matchStatsMenu.getMatchOutline().getSkin() != null && menu == Menu.Matches) {
                    menu = Menu.PlayerStats;
                    playerStats = new PlayerStatsMenu(matchStatsMenu
                            .matchOutline.getUsername(), y, width, height);
                    return;
                }


                int matchInfoTop = offset;
                int matchInfoBottom = offset + 40;


                if (mouseY >= matchInfoTop && mouseY <= matchInfoBottom && menu == Menu.Matches) {
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
