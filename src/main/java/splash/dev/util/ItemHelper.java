package splash.dev.util;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class ItemHelper {
    public static boolean isWeapon(ItemStack item) {
        return isItemOf(item, Items.MACE, Items.TRIDENT, Items.BOW, Items.CROSSBOW) ||
                item.getItem() instanceof SwordItem ||
                item.getItem() instanceof AxeItem;
    }

    public static boolean isItemOf(ItemStack item, Item... items) {
        return Arrays.stream(items)
                .anyMatch(i -> item.getItem() == i);
    }


    public static ItemStack getItem(String name) { // rip 1 hour
        try {
            for (String value : name.split(",")) {
                value = value.trim();
                Identifier id = value.contains(":") ? Identifier.of(value) : Identifier.of("minecraft", value);

                if (Registries.ITEM.containsId(id)) {
                    return Registries.ITEM.get(id).getDefaultStack();
                }
            }
        } catch (Exception ignored) {
        }

        return null;
    }

}
