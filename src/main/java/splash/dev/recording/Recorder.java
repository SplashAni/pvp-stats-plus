package splash.dev.recording;

import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import splash.dev.data.Category;
import splash.dev.data.MatchInfo;
import splash.dev.data.StoredMatchData;

import java.util.ArrayList;
import java.util.List;

import static splash.dev.BetterCpvp.mc;

public class Recorder {
    public boolean recording;
    List<ItemUsed> itemUsed;
    int usedItems;
    float time;
    int damageDealt, damageTaken, maxCombo, crits, mises;
    int currentCombo = 0;
    long lastHitTime = 0;
    long startTime = 0;

    public void startRecording() {
        if (recording) return;
        recording = true;
        itemUsed = new ArrayList<>();
        startTime = System.currentTimeMillis();
        System.out.println("Recording started.");
    }

    public void stopRecording() {
        recording = false;
        StoredMatchData.addInfo(new MatchInfo(
                Category.Cartpvp,
                new MatchOutline("test", usedItems, time, StoredMatchData.getMatches().size() + 1),
                itemUsed,
                new DamageInfo(damageDealt, damageTaken),
                new AttackInfo(maxCombo, mises, crits)
        ));
    }

    public boolean isRecording() {
        return recording;
    }

    public void tick() {
        if (recording) {
            time = (System.currentTimeMillis() - startTime) / 1000.0f;
        }

        if (System.currentTimeMillis() - lastHitTime > 3000) {
            currentCombo = 0;
        }
    }

    public void onItemUse() {
        updateItem();
    }

    public void onAttack() {
        updateItem();

        if (mc.crosshairTarget instanceof EntityHitResult) {

            if (mc.options.jumpKey.wasPressed()) crits++;
            mc.getNetworkHandler().sendChatMessage("hit entity lol");

            if (mc.player.hurtTime <= 0) {

                currentCombo++;
                lastHitTime = System.currentTimeMillis();
                mc.getNetworkHandler().sendChatMessage("current combo "+currentCombo);
                maxCombo = Math.max(maxCombo, currentCombo);  //
            }
        } else if(mc.crosshairTarget.getType().equals(HitResult.Type.MISS)){
            mc.getNetworkHandler().sendChatMessage("messed up the combo cuz of missing");

            mises++;
            currentCombo = 0;
        }
    }

    public void updateItem() {
        usedItems++;
        boolean found = false;

        for (ItemUsed used : itemUsed) {
            if (used.item() == mc.player.getMainHandStack()) {
                used.increment();
                found = true;
                break;
            }
        }

        if (!found) {
            ItemUsed newItem = new ItemUsed(mc.player.getMainHandStack(), 1);
            itemUsed.add(newItem);
        }
    }

    public void updateDamageDealt(float value) {
        this.damageDealt += (int) value;
    }

    public void updateSelfDamageDealt(float amount) {
        this.damageTaken += (int) amount;
    }
}
