package splash.dev.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import splash.dev.data.Category;
import splash.dev.data.MatchsDataRenderer;

public class MainGui extends Screen {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.ofVanilla("social_interactions/background");
    int tabWidth, tabHeight, tabOffset;
    int boxX, boxY, boxWidth, boxHeight;
    int activeTabIndex = 0;
    MatchsDataRenderer renderContent;

    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    public MainGui() {
        super(Text.literal("Main GUI"));
    }

    @Override
    protected void init() {
        super.init();

        tabWidth = 28;
        tabHeight = 32;
        tabOffset = 2;

        int tabCount = Category.values().length;
        boxWidth = (tabWidth * tabCount) + (tabOffset * (tabCount - 1)) + 20;
        boxHeight = 150;
        boxX = (this.width - boxWidth) / 2;
        boxY = (this.height - boxHeight) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        boxX = (this.width - boxWidth) / 2;
        boxY = (this.height - boxHeight) / 2;
        renderTabs(context, mouseX, mouseY);
        context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, boxX, boxY, boxWidth, boxHeight);
        renderActiveTabContent(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderTabs(DrawContext context, int mouseX, int mouseY) {
        Category[] categories = Category.values();
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

    protected void renderTabIcon(DrawContext context, Category category, int x, int y, boolean active) {
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
        context.drawGuiTexture(RenderLayer::getGuiTextured, identifiers[0], x, y, tabWidth, tabHeight);
        context.getMatrices().push();
        context.getMatrices().translate(0.0F, 0.0F, 100.0F);
        int iconX = x + 6;
        int iconY = y + 8;
        ItemStack itemStack = category.getItemStack();
        context.drawItem(itemStack, iconX, iconY);
        context.drawStackOverlay(this.textRenderer, itemStack, iconX, iconY);
        context.getMatrices().pop();
    }

    private void renderActiveTabContent(DrawContext context, int mouseX, int mouseY) {
        Category activeCategory = Category.values()[activeTabIndex];
        int contentX = boxX + 7;
        int contentY = boxY + 7;


        if (renderContent == null) {
            renderContent = new MatchsDataRenderer(activeCategory);
        }
        renderContent.setBounds(boxWidth-15,boxHeight-15,contentX, contentY);

        renderContent.render(context,mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        renderContent.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    protected boolean renderTabTooltipIfHovered(DrawContext context, Category category, int mouseX, int mouseY, int tabX, int tabY) {
        if (mouseX >= tabX && mouseX <= tabX + tabWidth &&
                mouseY >= tabY && mouseY <= tabY + tabHeight) {
            context.drawTooltip(this.textRenderer, Text.of(category.name()), mouseX, mouseY);
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
        Category[] categories = Category.values();
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
        Category[] categories = Category.values();
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