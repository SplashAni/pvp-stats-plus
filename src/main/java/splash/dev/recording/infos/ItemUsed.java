package splash.dev.recording.infos;

import net.minecraft.item.ItemStack;

import java.util.Objects;

public class ItemUsed {
    private final ItemStack item;
    private int count;

    public ItemUsed(ItemStack item, int count) {
        this.item = item;
        this.count = count;
    }

    public ItemStack item() {
        return item;
    }

    public int count() {
        return count;
    }

    public void increment() {
        count++;
    }


    @Override
    public int hashCode() {
        return Objects.hash(item, count);
    }


}
