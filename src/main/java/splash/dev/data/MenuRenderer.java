package splash.dev.data;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import splash.dev.PVPStatsPlus;
import splash.dev.data.gamemode.Gamemode;
import splash.dev.ui.gui.menus.MatchResultMenu;
import splash.dev.ui.gui.menus.PlayerStatsMenu;

import java.util.List;
import java.util.Objects;

import static splash.dev.PVPStatsPlus.mc;

public class MenuRenderer {
    Gamemode gamemode;
    int width, height, x, y, mouseY;
    int scrollOffset = 0;
    MatchResultMenu matchesStats;
    PlayerStatsMenu playerStats;
    ButtonWidget sortButton;
    MatchSortType sortType;
    Menu menu;

    public MenuRenderer(Gamemode gamemode) {
        this.gamemode = gamemode;
        menu = Menu.Matches;
        sortButton = ButtonWidget.builder(Text.literal(""), b -> {
        }).size(23, 15).position(1, 1).build();
        sortType = PVPStatsPlus.getMatchSortType();
    }

    public void setBounds(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        if (StoredMatchData.getMatchDataInCategory(gamemode, sortType).isEmpty()) return;
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
                for (MatchesMenu matchesMenu : Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode, sortType))) {
                    matchesMenu.render(context, offset, width, mouseX, mouseY);
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

        if (menu == Menu.Matches) {
            renderButton(context, mouseX, mouseY);
        }
    }

    public void renderButton(DrawContext context, int mouseX, int mouseY) {
        Identifier HIGHLIGHTED = sortType == MatchSortType.LATEST ? Identifier.ofVanilla("server_list/move_down_highlighted") : Identifier.ofVanilla("server_list/move_up_highlighted");
        Identifier NORMAL = sortType == MatchSortType.LATEST ? Identifier.ofVanilla("server_list/move_down") : Identifier.ofVanilla("server_list/move_up");

        sortButton.render(context, mouseX, mouseY, mc.getRenderTickCounter().getTickDelta(true));
        int y = sortType == MatchSortType.LATEST ? -14 : 0;
        context.drawGuiTexture(sortButton.isMouseOver(mouseX, mouseY) ? HIGHLIGHTED : NORMAL, 4, y, 32, 32);

    }


    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        List<MatchesMenu> stats = StoredMatchData.getMatchDataInCategory(gamemode, sortType);

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

            for (MatchesMenu matchesMenu :
                    Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode, sortType))) {

                int matchInfoTop = offset;
                int matchInfoBottom = offset + 40;


                if (mouseY >= matchInfoTop && mouseY <= matchInfoBottom) {
                    mc.setScreen(new ConfirmScreen(result -> {
                        if (result) StoredMatchData.removeMatchID(matchesMenu.getMatchOutline().getId());
                        mc.setScreen(null);
                    }, Text.literal("Game " + matchesMenu.gamemode.getName() + " confirm deletion."),
                            Text.literal("Are you sure you want to delete game #" + matchesMenu.getMatchOutline().getId())));
                    return;
                }


                offset += 40;
            }
        }
    }

    public void mouseRelease(int button, int mouseX, int mouseY) {
        if (button == 0) {
            if (sortButton.isMouseOver(mouseX, mouseY)) {
                sortType = (sortType == MatchSortType.LATEST) ? MatchSortType.OLDEST : MatchSortType.LATEST;

            }

            int offset = y + 10 + scrollOffset;

            for (MatchesMenu matchesMenu :
                    Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode, sortType))) {
                if (matchesMenu.headHovered && matchesMenu.getMatchOutline().getSkin() != null && menu == Menu.Matches) {
                    menu = Menu.PlayerStats;
                    playerStats = new PlayerStatsMenu(matchesMenu
                            .matchOutline.getUsername(), y, width, height);
                    return;
                }


                int matchInfoTop = offset;
                int matchInfoBottom = offset + 40;


                if (mouseY >= matchInfoTop && mouseY <= matchInfoBottom && menu == Menu.Matches) {
                    menu = Menu.MatchStats;
                    matchesStats = new MatchResultMenu(matchesMenu.matchOutline.getId(), y, width, height);
                    return;
                }


                offset += 40;
            }
        }
    }

    private int getTotalContentHeight() {
        return 40 * Objects.requireNonNull(StoredMatchData.getMatchDataInCategory(gamemode, sortType)).size();
    }

    private enum Menu {
        Matches,
        MatchStats,
        PlayerStats
    }
}
