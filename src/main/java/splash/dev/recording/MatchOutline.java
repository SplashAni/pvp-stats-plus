package splash.dev.recording;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class MatchOutline {
    AbstractClientPlayerEntity target;
    boolean won;
    int usedItems;
    float time;
    int id;

    public MatchOutline(AbstractClientPlayerEntity target, boolean won, int usedItems, float time, int id) {
        this.target = target;
        this.won = won;
        this.usedItems = usedItems;
        this.time = time;
        this.id = id;
    }

    public boolean isWon() {
        return won;
    }

    public AbstractClientPlayerEntity getTarget() {
        return target;
    }

    public int usedItems() {
        return usedItems;
    }

    public float getTime() {
        return time;
    }

    public int getId() {
        return id;
    }
}
