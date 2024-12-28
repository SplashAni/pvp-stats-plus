package splash.dev.recording.other;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class
RatioManager {
    private static final Map<PlayerEntity, Ratio> ratios = new HashMap<>();

    public static void update(PlayerEntity player, boolean playerWon) {
        Ratio ratio = ratios.get(player);
        if (ratio == null) {

            ratio = new Ratio(player, playerWon ? 1 : 0, playerWon ? 1 : 0);
            ratios.put(player, ratio);
        } else {
            if (playerWon) {
                ratio.updatePlayer();
            } else {
                ratio.updateTarget();
            }
        }
    }

    public static void resetScore() {
        ratios.clear();
    }

    public static String getFormattedScore(PlayerEntity player) {
        Ratio ratio = ratios.get(player);
        if (ratio != null) {
            return ratio.getFormattedScore();
        }
        return "0-0";
    }

    public static class Ratio {
        PlayerEntity entity;
        int player, target;

        public Ratio(PlayerEntity entity, int player, int target) {
            this.entity = entity;
            this.player = player;
            this.target = target;
        }

        public void updatePlayer() {
            player++;
        }

        public void updateTarget() {
            target++;
        }

        public String getFormattedScore() {
            return player + "-" + target;
        }
    }
}