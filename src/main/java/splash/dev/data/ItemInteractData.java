package splash.dev.data;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;

public class ItemInteractData {
    ItemData crystals, obsidian, anchor, glowstone, tntminecart, pearls;

    public ItemInteractData() {
        this.crystals = new ItemData(Items.END_CRYSTAL);
        this.obsidian = new ItemData(Items.OBSIDIAN);
        this.anchor = new ItemData(Items.RESPAWN_ANCHOR);
        this.glowstone = new ItemData(Items.GLOWSTONE);
        this.tntminecart = new ItemData(Items.TNT_MINECART);
        this.pearls = new ItemData(Items.ENDER_PEARL);
    }

    public List<ItemData> getAllItems() {
        return List.of(crystals, obsidian, anchor, glowstone, tntminecart, pearls);
    }

    public void updateItem(Item item) {
        getAllItems().forEach(itemData -> {
            if (itemData.item() == item) itemData.increment();
        });
    }
}