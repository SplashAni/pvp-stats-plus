package splash.dev.recording;

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
        if (item.getMaxDamage() > 0) {
            ItemStack stack = this.item.copy();
            stack.setDamage(count);
            return item;
        }
        return item;
    }

    public int count() {
        return count;
    }

    public void increment() {
        count++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ItemUsed) obj;
        return Objects.equals(this.item, that.item) &&
                this.count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, count);
    }

    @Override
    public String toString() {
        return "ItemUsed[" +
                "item=" + item + ", " +
                "count=" + count + ']';
    }

}
