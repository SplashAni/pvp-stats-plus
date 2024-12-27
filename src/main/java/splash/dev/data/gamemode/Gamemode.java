package splash.dev.data.gamemode;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import splash.dev.util.IdFilter;
import splash.dev.util.ItemHelper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Gamemode {

    private static final Map<String, Gamemode> REGISTRY = new LinkedHashMap<>();

    public final String name;
    public final boolean custom;
    private final Item item;

    private Gamemode(String name, Item item, boolean custom) {
        this.name = name;
        this.item = item;
        this.custom = custom;
        register(this);
    }

    private static void register(Gamemode gamemode) {
        REGISTRY.put(gamemode.name, gamemode);
    }

    public static boolean register(String name, Item item, boolean custom) {
        if (REGISTRY.containsKey(name)) return false;
        REGISTRY.put(name, new Gamemode(name, item, custom));
        return true;
    }

    private static void register(String name, Item item) {
        register(name, item, false);
    }

    public static Gamemode valueOf(String name) {

        if (name.contains("::")) {
            String after = IdFilter.getContentAfter(name);
            Item item = Objects.requireNonNull(ItemHelper.getItem(after)).getItem();

            register(name, item, true);
        }

        return REGISTRY.values().stream()
                .filter(g -> g.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> {
                    System.out.println("Gamemode not found: " + name); // Debugging line
                    return new IllegalArgumentException("Gamemode not found: " + name);
                });
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
        return custom ? IdFilter.getContentBefore(name) : name;
    }

    @Override
    public String toString() {
        return custom ? getName().concat("::").concat(getItemStack().getItem().toString()) : getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Gamemode gamemode)) return false;
        return gamemode.toString().equals(this.toString());
    }
}
