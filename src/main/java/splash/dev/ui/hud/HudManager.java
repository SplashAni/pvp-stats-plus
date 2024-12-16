package splash.dev.ui.hud;

import splash.dev.ui.hud.elements.ScoreElement;

import java.util.ArrayList;
import java.util.List;

public class HudManager {

    private final List<HudElement> elements = new ArrayList<>();

    public void init() {
        elements.add(new ScoreElement());
        int offset = 0;

        for (HudElement element : elements) {
            element.setY(offset);
            element.setX(1);
            offset += yOffset(element);
        }

    }

    public int yOffset(HudElement element) {
        return 9;
    }

    public List<HudElement> getElements() {
        return elements;
    }

    public void toggleVisibility(HudElement element) {
        for (HudElement element1 : elements) {
            if (element1 == element) element1.toggle();
        }
    }
}