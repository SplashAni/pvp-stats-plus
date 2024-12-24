package splash.dev.ui.recorder;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import splash.dev.PVPStatsPlus;
import splash.dev.data.gamemode.Gamemode;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.IntStream;

import static splash.dev.PVPStatsPlus.mc;

public class RecorderGui extends Screen {
    GameModeBox[] boxes = new GameModeBox[9];

    int xSpacing = 5, ySpacing = 20;
    int columns = 3;
    int rows = 3;

    private int tickCounter = 0;

    public RecorderGui() {
        super(Text.of("recorder.gui"));
        if (PVPStatsPlus.getRecorder() != null && PVPStatsPlus.getRecorder().isRecording()) {
            PVPStatsPlus.getRecorder().stopRecording(true);
            mc.setScreen(null);
        }

        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new GameModeBox(Gamemode.values()[i], 0, 0);
        }


        if(mc.gameRenderer.getPostProcessor() != null){
            mc.gameRenderer.disablePostProcessor();
        }


    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int boxWidth = boxes[0].getWidth();
        int boxHeight = boxes[0].getHeight();

        int totalGridWidth = (boxWidth + xSpacing) * columns - xSpacing;
        int totalGridHeight = (boxHeight + ySpacing) * rows - ySpacing;

        int startX = mc.getWindow().getScaledWidth() / 2 - totalGridWidth / 2;
        int startY = mc.getWindow().getScaledHeight() / 2 - totalGridHeight / 2;

        IntStream.range(0, boxes.length).forEachOrdered(i -> {
            int row = i / columns;
            int column = i % columns;
            int x = startX + column * (boxWidth + xSpacing);
            int y = startY + row * (boxHeight + ySpacing);
            boxes[i].render(context, x, y, mouseX, mouseY);
        });

        for (GameModeBox box : boxes) {
            if (box.hovered) {
                int key = PVPStatsPlus.getBindManager().getKey(box.gamemode);
                String text = key == -1 ? "Key: None" : "Key: " + InputUtil.fromKeyCode(key, -1).getLocalizedText().getString();

                int width = 100;
                int height = 20;
                int centerX = (context.getScaledWindowWidth() - width) / 2;
                int boxY = 5;
                context.fill(centerX, boxY, centerX + width, boxY + height, new Color(52, 52, 52, 250).getRGB());

                context.drawBorder(centerX, boxY, width, height, Color.WHITE.getRGB());

                int textWidth = mc.textRenderer.getWidth(text);
                int textHeight = mc.textRenderer.fontHeight;
                int textX = centerX + (width - textWidth) / 2;
                int textY = boxY + (height - textHeight) / 2 + 1;

                context.drawText(mc.textRenderer, text, textX, textY, -1, true);
            }
        }


    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GameModeBox box : boxes) {
            box.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        tickCounter++;

        if (tickCounter >= 2 && keyCode != GLFW.GLFW_KEY_ESCAPE) {
            Arrays.stream(boxes)
                    .filter(box -> box.hovered)
                    .forEachOrdered(box -> PVPStatsPlus.getBindManager().setKey(box.gamemode, keyCode));
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        for (GameModeBox box : boxes) {
            if (box.hovered && PVPStatsPlus.getBindManager().getKey(box.gamemode) != -1) {
                PVPStatsPlus.getBindManager().setKey(box.gamemode, -1);
                return false;
            }
        }
        return true;
    }


}
