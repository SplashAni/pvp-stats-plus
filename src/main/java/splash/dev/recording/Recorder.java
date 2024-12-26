package splash.dev.recording;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Hand;
import splash.dev.data.MatchStatsMenu;
import splash.dev.data.StoredMatchData;
import splash.dev.data.gamemode.Gamemode;
import splash.dev.recording.calculations.ArrowCalculator;
import splash.dev.recording.calculations.Calculation;
import splash.dev.recording.calculations.PostStatCalculator;
import splash.dev.recording.infos.*;
import splash.dev.recording.other.RatioManager;
import splash.dev.util.ItemHelper;
import splash.dev.util.SkinHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static splash.dev.PVPStatsPlus.mc;

public class Recorder {
    public boolean recording;
    List<ItemUsed> itemUsed;
    List<Calculation> calculations;
    AbstractClientPlayerEntity target;
    int usedItems;
    float time;
    int maxCombo, crits, mises;
    Gamemode gamemode;
    int currentCombo = 0;
    long lastHitTime = 0;
    long startTime = 0;


    public void startRecording(Gamemode gamemode) {
        if (recording) return;
        recording = true;
        itemUsed = new ArrayList<>();
        calculations = new ArrayList<>();
        startTime = System.currentTimeMillis();
        this.gamemode = gamemode;
        calculations.add(new PostStatCalculator());
        calculations.add(new ArrowCalculator());

        calculations.forEach(Calculation::onStart);
    }

    public void stopRecording(boolean won) {
        recording = false;

        if (target != null) {
            SkinHelper.saveSkin(target);


            if (target.isDead()) {
                RatioManager.update(target, won);
            }
        }

        Pair<DistanceInfo, DamageInfo> results = null;
        ArrowInfo arrowInfo = null;

        for (Calculation calculation : calculations) {
            if (calculation instanceof PostStatCalculator) results = ((PostStatCalculator) calculation).onEnd();
            if (calculation instanceof ArrowCalculator) arrowInfo = ((ArrowCalculator) calculation).onEnd();
        }
        StoredMatchData.addInfo(new MatchStatsMenu(
                gamemode,
                new MatchOutline(target == null ? "unknown" : target.getGameProfile().getName(), target == null ? mc.player.getSkinTextures() : target.getSkinTextures(), won, usedItems, time,
                        StoredMatchData.getMatches().size() + 1),
                itemUsed,
                Objects.requireNonNull(results).getSecond(),
                new AttackInfo(maxCombo, mises, crits),
                results.getFirst(),
                arrowInfo
        ));


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
        calculations.forEach(Calculation::onTick);
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
        if (mc.world == null) return;
        if (packet instanceof EntityStatusS2CPacket entityStatusS2CPacket) {
            if (entityStatusS2CPacket.getEntity(mc.world) == mc.player
                    && entityStatusS2CPacket.getStatus() == 35) {
                Hand hand = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING ? Hand.OFF_HAND : Hand.MAIN_HAND;
                updateItem(hand);
            }
        }
    }

    public void updateItem(Hand hand) {
        ItemStack stack = hand == Hand.MAIN_HAND ? mc.player.getMainHandStack() : mc.player.getOffHandStack();

        if (!stack.getItem().equals(Items.AIR)) {
            updateItem(stack);
        }

    }

    public void updateItem(ItemStack stack) {
        if (stack == null) return;
        usedItems++;
        boolean found = false;

        for (ItemUsed used : itemUsed) {
            assert mc.player != null;
            if (Objects.equals(used.item().getTranslationKey(), stack.getTranslationKey())) {
                used.increment();
                found = true;
                break;
            }
        }

        if (!found) {
            assert mc.player != null;
            ItemUsed newItem = new ItemUsed(stack, 1);
            itemUsed.add(newItem);
        }
    }


    public String getFormattedTime() {
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);

        return String.format("%d:%d", minutes, seconds);
    }


    public AbstractClientPlayerEntity getTarget() {
        return target;
    }
}
