package splash.dev.util;

import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import splash.dev.PVPStatsPlus;

public class PotionUtils {
    public static RegistryEntry<Potion> getPotion(String name) {
        for (RegistryEntry<Potion> potion : PVPStatsPlus.potions) {
            if (potion.getIdAsString().contains(name)) return potion;
        }
        return null;
    }

    public static String getContentAfter(String input) {
        String[] parts = input.split("::");
        if (parts.length > 1) {
            return parts[1].trim();
        }
        return "";
    }

    public static String getContentBefore(String input) {
        String[] parts = input.split("::");
        if (parts.length > 0) {
            return parts[0].trim();
        }
        return "";
    }

}
