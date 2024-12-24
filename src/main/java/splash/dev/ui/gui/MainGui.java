package splash.dev.ui.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import splash.dev.PVPStatsPlus;
import splash.dev.data.gamemode.Gamemode;
import splash.dev.data.MenuRenderer;

public class MainGui extends Screen {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.ofVanilla("social_interactions/background");
    int tabWidth, tabHeight, tabOffset;
    int boxX, boxY, boxWidth, boxHeight;
    int activeTabIndex = 0;
    MenuRenderer renderContent;

    public MainGui() {
        super(Text.literal("main.gui"));
        PVPStatsPlus.setGui(this);
    }

    @Override
    protected void init() {
        super.init();

        tabWidth = 28;
        tabHeight = 32;
        tabOffset = 2;

        int tabCount = Gamemode.values().length;
        boxWidth = (tabWidth * tabCount) + (tabOffset * (tabCount - 1)) + 20;
        boxHeight = 150;
        boxX = (this.width - boxWidth) / 2;
        boxY = (this.height - boxHeight) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        boxX = (this.width - boxWidth) / 2;
        boxY = (this.height - boxHeight) / 2;
        renderTabs(context, mouseX, mouseY);
        context.drawGuiTexture(BACKGROUND_TEXTURE, boxX, boxY, boxWidth, boxHeight);
        renderActiveTabContent(context, mouseX, mouseY);
    }

    private void renderTabs(DrawContext context, int mouseX, int mouseY) {
        Gamemode[] categories = Gamemode.values();
        int tabCount = categories.length;
        int totalTabWidth = (tabWidth * tabCount) + (tabOffset * (tabCount - 1));
        int xStart = boxX + (boxWidth - totalTabWidth) / 2;
        int yPosition = boxY - tabHeight + 3;
        for (int i = 0; i < tabCount; i++) {
            int xPosition = xStart + i * (tabWidth + tabOffset);
            boolean isActive = i == activeTabIndex;
            renderTabIcon(context, categories[i], xPosition, yPosition, isActive);
        }
        for (int i = 0; i < tabCount; i++) {
            int xPosition = xStart + i * (tabWidth + tabOffset);
            if (renderTabTooltipIfHovered(context, categories[i], mouseX, mouseY, xPosition, yPosition)) {
                break;
            }
        }
    }

    protected void renderTabIcon(DrawContext context, Gamemode gamemode, int x, int y, boolean active) {
        Identifier[] TAB_TOP_UNSELECTED_TEXTURES = new Identifier[]{
                Identifier.ofVanilla("container/creative_inventory/tab_top_unselected_1"),
                Identifier.ofVanilla("container/creative_inventory/tab_top_unselected_2"),
                Identifier.ofVanilla("container/creative_inventory/tab_top_unselected_3")
        };
        Identifier[] TAB_TOP_SELECTED_TEXTURES = new Identifier[]{
                Identifier.ofVanilla("container/creative_inventory/tab_top_selected_1"),
                Identifier.ofVanilla("container/creative_inventory/tab_top_selected_2"),
                Identifier.ofVanilla("container/creative_inventory/tab_top_selected_3")
        };
        Identifier[] identifiers = active ? TAB_TOP_SELECTED_TEXTURES : TAB_TOP_UNSELECTED_TEXTURES;
        context.drawGuiTexture(identifiers[0], x, y, tabWidth, tabHeight);
        context.getMatrices().push();
        context.getMatrices().translate(0.0F, 0.0F, 100.0F);
        int iconX = x + 6;
        int iconY = y + 8;
        ItemStack itemStack = gamemode.getItemStack();
        context.drawItem(itemStack, iconX, iconY);
        context.drawItemInSlot(this.textRenderer, itemStack, iconX, iconY);
        context.getMatrices().pop();
    }

    private void renderActiveTabContent(DrawContext context, int mouseX, int mouseY) {
        Gamemode activeGamemode = Gamemode.values()[activeTabIndex];
        int contentX = boxX + 7;
        int contentY = boxY + 7;


        if (renderContent == null) {
            renderContent = new MenuRenderer(activeGamemode);
        }
        renderContent.setBounds(boxWidth - 15, boxHeight - 15, contentX, contentY);

        renderContent.render(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        renderContent.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        renderContent.keyRelease(keyCode);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        renderContent.mouseRelease(button, (int) mouseX, (int) mouseY);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    protected boolean renderTabTooltipIfHovered(DrawContext context, Gamemode gamemode, int mouseX, int mouseY, int tabX, int tabY) {
        if (mouseX >= tabX && mouseX <= tabX + tabWidth &&
                mouseY >= tabY && mouseY <= tabY + tabHeight) {
            context.drawTooltip(this.textRenderer, Text.of(gamemode.name()), mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isClickInTabs(mouseX, mouseY)) {
            handleTabClick(mouseX, mouseY);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isClickInTabs(double mouseX, double mouseY) {
        Gamemode[] categories = Gamemode.values();
        int xStart = boxX + (boxWidth - ((tabWidth * categories.length) + (tabOffset * (categories.length - 1)))) / 2;
        int yPosition = boxY - tabHeight;
        for (int i = 0; i < categories.length; i++) {
            int xPosition = xStart + i * (tabWidth + tabOffset);
            if (mouseX >= xPosition && mouseX <= xPosition + tabWidth &&
                    mouseY >= yPosition && mouseY <= yPosition + tabHeight) {
                return true;
            }
        }
        return false;
    }

    private void handleTabClick(double mouseX, double mouseY) {
        Gamemode[] categories = Gamemode.values();
        int xStart = boxX + (boxWidth - ((tabWidth * categories.length) + (tabOffset * (categories.length - 1)))) / 2;
        int yPosition = boxY - tabHeight + 8;
        for (int i = 0; i < categories.length; i++) {
            int xPosition = xStart + i * (tabWidth + tabOffset);
            if (mouseX >= xPosition && mouseX <= xPosition + tabWidth &&
                    mouseY >= yPosition && mouseY <= yPosition + tabHeight) {
                activeTabIndex = i;
                renderContent = null;
                break;
            }
        }
    }

}