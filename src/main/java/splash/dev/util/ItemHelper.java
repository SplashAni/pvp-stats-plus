package splash.dev.util;

import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemHelper {
    public static List<RegistryEntry<Potion>> potions = new ArrayList<>();

    public static boolean isWeapon(ItemStack item) {
        return isItemOf(item, Items.MACE, Items.TRIDENT, Items.BOW, Items.CROSSBOW) ||
                item.getItem() instanceof SwordItem ||
                item.getItem() instanceof AxeItem;
    }

    public static boolean isItemOf(ItemStack item, Item... items) {
        return Arrays.stream(items)
                .anyMatch(i -> item.getItem() == i);
    }
    public static RegistryEntry<Potion> getPotion(String name) {
        for (RegistryEntry<Potion> potion : ItemHelper.potions) {
            if (potion.getIdAsString().contains(name)) return potion;
        }
        return null;
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
