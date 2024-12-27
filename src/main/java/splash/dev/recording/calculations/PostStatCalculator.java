package splash.dev.recording.calculations;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import splash.dev.recording.infos.DamageInfo;
import splash.dev.recording.infos.DistanceInfo;

import java.util.Objects;

import static splash.dev.PVPStatsPlus.mc;

public class PostStatCalculator implements Calculation {

    private final StatHandler statHandler;

    private DistanceInfo initialStats = new DistanceInfo(0, 0, 0);
    private DamageInfo initialDamage = new DamageInfo(0, 0, 0);

    public PostStatCalculator() {
        statHandler = Objects.requireNonNull(mc.player).getStatHandler();
        Objects.requireNonNull(mc.getNetworkHandler()).
                sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.REQUEST_STATS));
    }


    public Pair<DistanceInfo, DamageInfo> onEnd() {
        return Pair.of(getFinalDistanceInfo(), getFinalDamageInfo());
    }

    public DistanceInfo getFinalDistanceInfo() {
        DistanceInfo finalStats = getDistanceStats();

        int walkedDifference = finalStats.getDistanceWalked() - initialStats.getDistanceWalked();
        int sprintedDifference = finalStats.getDistanceSprinted() - initialStats.getDistanceSprinted();
        int crouchedDifference = finalStats.getDistanceCrouched() - initialStats.getDistanceCrouched();

        return new DistanceInfo(walkedDifference, sprintedDifference, crouchedDifference);
    }

    public DamageInfo getFinalDamageInfo() {
        DamageInfo finalDamage = getDamageStats();

        int damageDealtDifference = finalDamage.getDealtDamage() - initialDamage.getDealtDamage();
        int damageTakenDifference = finalDamage.getDamageTaken() - initialDamage.getDamageTaken();
        int damageBlockedDifference = finalDamage.getDamageBlocked() - initialDamage.getDamageBlocked();

        return new DamageInfo(damageDealtDifference, damageTakenDifference, damageBlockedDifference);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private DistanceInfo getDistanceStats() {
        int walkDistance = 0, sprintDistance = 0, crouchDistance = 0;

        ObjectArrayList<Stat<Identifier>> objectArrayList = new ObjectArrayList<>(Stats.CUSTOM.iterator());

        for (Stat<Identifier> stat : objectArrayList) {
            if (stat.getValue() == Stats.WALK_ONE_CM) {
                walkDistance = statHandler.getStat(stat);
            } else if (stat.getValue() == Stats.SPRINT_ONE_CM) {
                sprintDistance = statHandler.getStat(stat);
            } else if (stat.getValue() == Stats.CROUCH_ONE_CM) {
                crouchDistance = statHandler.getStat(stat);
            }
        }

        return new DistanceInfo(walkDistance, sprintDistance, crouchDistance);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private DamageInfo getDamageStats() {
        int damageDealt = 0, damageTaken = 0, damageBlocked = 0;

        ObjectArrayList<Stat<Identifier>> objectArrayList = new ObjectArrayList<>(Stats.CUSTOM.iterator());

        for (Stat<Identifier> stat : objectArrayList) {
            if (stat.getValue() == Stats.DAMAGE_DEALT) {
                damageDealt = statHandler.getStat(stat);
            } else if (stat.getValue() == Stats.DAMAGE_TAKEN) {
                damageTaken = statHandler.getStat(stat);
            } else if (stat.getValue() == Stats.DAMAGE_BLOCKED_BY_SHIELD) {
                damageBlocked = statHandler.getStat(stat);
            }
        }

        return new DamageInfo(damageDealt, damageTaken, damageBlocked);
    }

    @Override
    public void onStart() {
        initialStats = getDistanceStats();
        initialDamage = getDamageStats();
    }
}
