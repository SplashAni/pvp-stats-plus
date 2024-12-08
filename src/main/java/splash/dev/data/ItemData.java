package splash.dev.data;

import net.minecraft.item.Item;

import java.util.Objects;

public class ItemData {
    private Item item;
    private int count;

    public ItemData(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public ItemData(Item item) {
        this.item = item;
        this.count = 0;
    }

    public Item item() {
        return item;
    }

    public int count() {
        return count;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ItemData) obj;
        return Objects.equals(this.item, that.item) &&
                this.count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, count);
    }

    public void increment() {
        this.count += 1;
        System.out.println("incrmeented");
    }


}
