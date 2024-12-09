package splash.dev.gui.comp;

import splash.dev.data.Category;
import splash.dev.gui.content.*;

public class CategoryWindow {
    public RenderContent renderCategory(Category category) {
        return switch (category) {
            case Cpvp -> new CpvpContent();
            case Cartpvp -> new CartpvpContent();
            case Nethpot -> new NetpotContent();
            case Diapot -> new DiapotContent();
            case SMP -> new SmpContent();
            case DiaSMP -> new DiasmpContent();
            case UHC -> new UHCConent();
            case AXE -> new AxeContent();
            case Sword -> new SwordContent();
        };
    }
}
