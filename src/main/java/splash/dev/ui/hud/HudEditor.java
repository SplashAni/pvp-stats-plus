package splash.dev.ui.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import splash.dev.ui.hud.elements.IndicatorElement;
import splash.dev.ui.hud.elements.ScoreElement;
import splash.dev.ui.hud.elements.TimerElement;

import java.awt.*;

import static splash.dev.PVPStatsPlus.getHudManager;
import static splash.dev.PVPStatsPlus.mc;

public class HudEditor extends Screen {
    CheckboxWidget scoreButton;
    CheckboxWidget recordButton;
    CheckboxWidget timerButton;
    ButtonWidget scoreResetButton;
    ButtonWidget recordResetButton;
    ButtonWidget timerResetButton;
    HudManager hudManager = getHudManager();

    public HudEditor() {
        super(Text.of("gui.screen"));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        hudManager.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    protected void init() {
        super.init();
        scoreButton = CheckboxWidget.builder(Text.of("Score"), mc.textRenderer)
                .checked(getHudManager().isVisible(ScoreElement.class))
                .callback((checkbox, checked) -> {
                    getHudManager().toggleVisibility(ScoreElement.class);
                })
                .build();

        recordButton = CheckboxWidget.builder(Text.of("Indicator"), mc.textRenderer)
                .checked(getHudManager().isVisible(IndicatorElement.class))
                .callback((checkbox, checked) -> {
                    getHudManager().toggleVisibility(IndicatorElement.class);
                })
                .build();

        timerButton = CheckboxWidget.builder(Text.of("Timer"), mc.textRenderer)
                .checked(getHudManager().isVisible(TimerElement.class))
                .callback((checkbox, checked) -> getHudManager().toggleVisibility(TimerElement.class))
                .build();

        scoreResetButton = new ButtonWidget.Builder(Text.of("Reset"), button -> {
            getHudManager().reset(ScoreElement.class);
        })
                .position(scoreButton.getX() + scoreButton.getWidth() - 50, scoreButton.getY())
                .size(50, 20)
                .build();

        recordResetButton = new ButtonWidget.Builder(Text.of("Reset"), button -> {
            getHudManager().reset(IndicatorElement.class);
        })
                .position(scoreButton.getX() + scoreButton.getWidth() - 50, recordButton.getY())
                .size(50, 20)
                .build();

        timerResetButton = new ButtonWidget.Builder(Text.of("Reset"), button -> {
            getHudManager().reset(TimerElement.class);
        })
                .position(timerButton.getX() + timerButton.getWidth() - 50, timerButton.getY())
                .size(50, 20)
                .build();

        addDrawable(scoreButton);
        addDrawable(recordButton);
        addDrawable(timerButton);
        addDrawable(scoreResetButton);
        addDrawable(recordResetButton);
        addDrawable(timerResetButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        renderOptions(context, mouseX, mouseY, delta);

        hudManager.render(context, mouseX, mouseY, delta);
    }

    public void renderOptions(DrawContext context, int mouseX, int mouseY, float delta) {

        int windowWidth = context.getScaledWindowWidth();
        int windowHeight = context.getScaledWindowHeight();
        int boxWidth = 250;
        int boxHeight = 100;
        int x1 = (windowWidth - boxWidth) / 2;
        int y1 = (windowHeight - boxHeight) / 2;
        int x2 = x1 + boxWidth;
        int y2 = y1 + boxHeight;
        int topBoxHeight = 15;
        int topY1 = y1 - topBoxHeight;
        int outlineThickness = 1;

        int outlineColor = new Color(190, 189, 189, 180).getRGB();
        context.fill(
                x1 - outlineThickness,
                topY1 - outlineThickness,
                x2 + outlineThickness,
                y2 + outlineThickness,
                outlineColor
        );

        context.fill(x1, y1, x2, y2, new Color(30, 30, 30, 220).getRGB());
        context.fill(x1, topY1, x2, y1, new Color(54, 54, 54, 255).getRGB());
        String text = "Hud Editor";
        int textWidth = mc.textRenderer.getWidth(text);
        int textX = x1 + (boxWidth / 2) - (textWidth / 2);
        int textY = topY1 + (topBoxHeight / 2) - (mc.textRenderer.fontHeight / 2) + 1;
        context.drawText(mc.textRenderer, text, textX, textY, -1, true);

        int buttonY = y1 + 20;

        scoreButton.setPosition(x1 + 10, buttonY);
        scoreButton.render(context, mouseX, mouseY, delta);

        recordButton.setPosition(x1 + 10, buttonY + scoreButton.getHeight() + 5);
        recordButton.render(context, mouseX, mouseY, delta);

        timerButton.setPosition(x1 + 10, buttonY + scoreButton.getHeight() + recordButton.getHeight() + 10);
        timerButton.render(context, mouseX, mouseY, delta);

        scoreResetButton.setPosition(scoreButton.getX() + scoreButton.getWidth() + 120, scoreButton.getY());
        scoreResetButton.render(context, mouseX, mouseY, delta);

        recordResetButton.setPosition(scoreButton.getX() + scoreButton.getWidth() + 120, recordButton.getY());
        recordResetButton.render(context, mouseX, mouseY, delta);

        timerResetButton.setPosition(scoreButton.getX() + scoreButton.getWidth() + 120, timerButton.getY());
        timerResetButton.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (scoreButton.isMouseOver(mouseX, mouseY)) {
            scoreButton.onPress();
            return true;
        }
        if (recordButton.isMouseOver(mouseX, mouseY)) {
            recordButton.onPress();
            return true;
        }
        if (timerButton.isMouseOver(mouseX, mouseY)) {
            timerButton.onPress();
            return true;
        }
        if (scoreResetButton.isMouseOver(mouseX, mouseY)) {
            scoreResetButton.onPress();
            return true;
        }
        if (recordResetButton.isMouseOver(mouseX, mouseY)) {
            recordResetButton.onPress();
            return true;
        }
        if (timerResetButton.isMouseOver(mouseX, mouseY)) {
            timerResetButton.onPress();
            return true;
        }

        hudManager.mouseClicked(mouseX, mouseY, button);

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        hudManager.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
