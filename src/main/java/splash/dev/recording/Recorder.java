package splash.dev.recording;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Hand;
import splash.dev.PVPStatsPlus;
import splash.dev.data.Gamemode;
import splash.dev.data.MatchStatsMenu;
import splash.dev.data.StoredMatchData;
import splash.dev.recording.infos.AttackInfo;
import splash.dev.recording.infos.DamageInfo;
import splash.dev.recording.infos.ItemUsed;
import splash.dev.recording.infos.MatchOutline;
import splash.dev.util.ItemHelper;
import splash.dev.util.SkinHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static splash.dev.PVPStatsPlus.mc;

public class Recorder {
    public boolean recording;
    List<ItemUsed> itemUsed;
    int usedItems;
    float time;
    int damageDealt, damageTaken, maxCombo, crits, mises;
    Gamemode gamemode;
    int currentCombo = 0;
    long lastHitTime = 0;
    long startTime = 0;


    AbstractClientPlayerEntity target;

    private float lastHeath, lastAbsorption;

    public void startRecording(Gamemode gamemode) {
        if (recording) return;
        recording = true;
        itemUsed = new ArrayList<>();
        startTime = System.currentTimeMillis();
        this.gamemode = gamemode;
        lastHeath = mc.player.getHealth();
        lastAbsorption = mc.player.getAbsorptionAmount();
    }

    public void stopRecording(boolean won) {
        recording = false;

        if (target != null) SkinHelper.saveSkin(target);

        StoredMatchData.addInfo(new MatchStatsMenu(
                gamemode,
                new MatchOutline(target == null ? "unknown" : target.getGameProfile().getName(), target == null ? mc.player.getSkinTextures() : target.getSkinTextures(), won, usedItems, time,
                        StoredMatchData.getMatches().size() + 1),
                itemUsed,
                new DamageInfo(damageDealt, damageTaken),
                new AttackInfo(maxCombo, mises, crits)
        ));
        PVPStatsPlus.resetRecorder(true);

    }

    public boolean isRecording() {
        return recording;
    }

    public void tick() {

        time = (System.currentTimeMillis() - startTime) / 1000.0f;

        Objects.requireNonNull(mc.world).getPlayers()
                .stream()
                .filter(player -> player.deathTime > 0 || player.getHealth() <= 0)
                .filter(player -> player == target).map(player -> true)
                .forEachOrdered(this::stopRecording);

        if (System.currentTimeMillis() - lastHitTime > 3000) {
            currentCombo = 0;
        }

        float currentHealth = mc.player.getHealth();
        float currentAbsorption = mc.player.getAbsorptionAmount();
        float lastHealth = this.lastHeath;

        if (lastHealth != -1.0f && (currentHealth + currentAbsorption) < lastHealth) {
            float damageTaken = lastHealth - currentHealth - currentAbsorption;

            if (damageTaken > 0) {
                System.out.println("Took damage: " + damageTaken);
                updateSelfDamageDealt(damageTaken);
            }
        }

        this.lastHeath = currentHealth;
        this.lastAbsorption = currentAbsorption;
    }


    public void onAttack(Entity entity, boolean success) {
        if (success) {
            if (target == null && entity instanceof AbstractClientPlayerEntity e) {
                target = e;
            }
            if (mc.options.jumpKey.wasPressed()) crits++;

            if (mc.player != null && mc.player.hurtTime <= 0) {

                currentCombo++;
                lastHitTime = System.currentTimeMillis();
                maxCombo = Math.max(maxCombo, currentCombo);
            }
            if (ItemHelper.isWeapon(mc.player.getMainHandStack())) {
                updateItem(Hand.MAIN_HAND);
            } else currentCombo = 0;
        } else {
            mises++;
            currentCombo = 0;
        }
    }

    public void onPacketReceive(Packet<?> packet) {
        if (packet instanceof EntityStatusS2CPacket entityStatusS2CPacket) {
            if (entityStatusS2CPacket.getEntity(mc.world) == mc.player
                    && entityStatusS2CPacket.getStatus() == 35) {
                Hand hand = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING ? Hand.OFF_HAND : Hand.MAIN_HAND;
                updateItem(hand);
            }
        }
    }

    public void updateItem(Hand hand) {
        Item stack = hand == Hand.MAIN_HAND ? mc.player.getMainHandStack().getItem() : mc.player.getOffHandStack().getItem();

        if (stack != Items.AIR) updateItem(stack);

    }

    public void updateItem(Item stack) {
        usedItems++;
        boolean found = false;

        for (ItemUsed used : itemUsed) {
            assert mc.player != null;
            if (used.item().getItem() == stack) {
                used.increment();
                found = true;
                break;
            }
        }

        if (!found) {
            assert mc.player != null;
            ItemUsed newItem = new ItemUsed(stack.getDefaultStack(), 1);
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
