package splash.dev.data;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public enum Category {
    Cpvp(Items.END_CRYSTAL),

    Cartpvp(Items.TNT_MINECART),

    Nethpot(Items.NETHERITE_HELMET),

    Diapot(Items.DIAMOND_HELMET),

    SMP(Items.ENDER_PEARL),

    DiaSMP(Items.CHORUS_FRUIT),

    UHC(Items.LAVA_BUCKET),

    AXE(Items.DIAMOND_AXE),

    Sword(Items.DIAMOND_SWORD);

    final Item itemStack;

    Category(Item itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack.getDefaultStack();
    }
}
