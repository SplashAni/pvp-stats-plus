package splash.dev.recording;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import splash.dev.PVPStatsPlus;
import splash.dev.data.Category;
import splash.dev.data.MatchStatsMenu;
import splash.dev.data.StoredMatchData;

import java.util.ArrayList;
import java.util.List;

import static splash.dev.PVPStatsPlus.mc;

public class Recorder {
    public boolean recording;
    List<ItemUsed> itemUsed;
    int usedItems;
    float time;
    int damageDealt, damageTaken, maxCombo, crits, mises;
    Category category;
    int currentCombo = 0;
    long lastHitTime = 0;
    long startTime = 0;


    AbstractClientPlayerEntity target;

    public void startRecording(Category category) {
        if (recording) return;
        recording = true;
        itemUsed = new ArrayList<>();
        startTime = System.currentTimeMillis();
        this.category = category;
    }

    public void stopRecording(boolean won) {
        recording = false;
        StoredMatchData.addInfo(new MatchStatsMenu(
                category,
                new MatchOutline(target == null ? mc.player : target, won, usedItems, time,
                        StoredMatchData.getMatches().size() + 1),
                itemUsed,
                new DamageInfo(damageDealt, damageTaken),
                new AttackInfo(maxCombo, mises, crits)
        ));
        PVPStatsPlus.recorder = null;

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

        if (mc.crosshairTarget instanceof EntityHitResult hitResult) {

            if(target == null && hitResult.getEntity() instanceof AbstractClientPlayerEntity entity) {
                target = entity;
            }

            if (mc.options.jumpKey.wasPressed()) crits++;

            if (mc.player != null && mc.player.hurtTime <= 0) {

                currentCombo++;
                lastHitTime = System.currentTimeMillis();
                maxCombo = Math.max(maxCombo, currentCombo);
            }
        } else {
            assert mc.crosshairTarget != null;
            if (mc.crosshairTarget.getType().equals(HitResult.Type.MISS)) {
                mises++;
                currentCombo = 0;
            }
        }
    }

    public void updateItem() {
        if(mc.player.getMainHandStack().getItem() == Items.AIR) return;
        usedItems++;
        boolean found = false;

        for (ItemUsed used : itemUsed) {
            assert mc.player != null;
            if (used.item().getItem() == mc.player.getMainHandStack().getItem()) {
                used.increment();
                found = true;
                break;
            }
        }

        if (!found) {
            assert mc.player != null;
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


    public AbstractClientPlayerEntity getTarget() {
        return target;
    }
}
