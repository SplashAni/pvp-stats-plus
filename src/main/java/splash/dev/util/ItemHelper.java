package splash.dev.util;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemHelper {
    public static boolean isWeapon(ItemStack item) {
        return item.getItem() instanceof SwordItem ||
                item.getItem() instanceof AxeItem ||
                item.getItem() == Items.MACE ||
                item.getItem() == Items.TRIDENT;
    }

    public static boolean isPostState(Item itemStack) {
        return  (itemStack instanceof PotionItem) ||
                (itemStack instanceof SplashPotionItem) ||
                itemStack == Items.LAVA_BUCKET
                || itemStack == Items.WATER_BUCKET;

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
