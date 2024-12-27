package splash.dev.data.gamemode;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.LinkedHashMap;
import java.util.Map;

public class Gamemode {

    private static final Map<String, Gamemode> REGISTRY = new LinkedHashMap<>();  /*
        i love mojang clases to to get inspired by these amazing devs 😍😍😍
    */

    public final String name;
    private final Item item;

    private Gamemode(String name, Item item) {
        this.name = name;
        this.item = item;
        register(this);
    }

    private static void register(Gamemode gamemode) {
        REGISTRY.put(gamemode.name, gamemode);
    }

    public static boolean register(String name, Item item) {
        if (REGISTRY.containsKey(name)) return false;
        REGISTRY.put(name, new Gamemode(name, item));
        return true;
    }


    public static Gamemode valueOf(String name) {
        return REGISTRY.values().stream()
                .filter(g -> g.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Gamemode not found: " + name));
    }

    public static Gamemode[] values() {
        return REGISTRY.values().toArray(new Gamemode[0]);
    }

    public static void register() {
        register("Cpvp", Items.END_CRYSTAL);
        register("Cartpvp", Items.TNT_MINECART);
        register("Nethpot", Items.NETHERITE_HELMET);
        register("Diapot", Items.DIAMOND_HELMET);
        register("SMP", Items.ENDER_PEARL);
        register("DiaSMP", Items.CHORUS_FRUIT);
        register("UHC", Items.LAVA_BUCKET);
        register("Axe", Items.DIAMOND_AXE);
        register("Sword", Items.DIAMOND_SWORD);
    }


    public ItemStack getItemStack() {
        return item.getDefaultStack();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
