package splash.dev.ui.recorder;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import splash.dev.PVPStatsPlus;
import splash.dev.data.Gamemode;

import java.util.stream.IntStream;

import static splash.dev.PVPStatsPlus.mc;

public class RecorderGui extends Screen {
    GameModeBox[] boxes = new GameModeBox[9];
    int xSpacing = 5, ySpacing = 20;
    int columns = 3;
    int rows = 3;

    public RecorderGui() {
        super(Text.of("recorder.gui"));
        if (PVPStatsPlus.recorder != null && PVPStatsPlus.getRecorder().isRecording()) {
            PVPStatsPlus.getRecorder().stopRecording(true);
            mc.setScreen(null);
        }

        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new GameModeBox(Gamemode.values()[i], 0, 0);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
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

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GameModeBox box : boxes) {
            box.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
