package splash.dev.recording.calculations;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import splash.dev.recording.infos.ArrowInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static splash.dev.PVPStatsPlus.mc;

public class ArrowCalculator implements Calculation {

    private int arrowShots, longestDistance, arrowsHit;
    private float accuracy;
    private Map<ArrowEntity, Vec3d> arrowStartPositions;

    @Override
    public void onTick() {
        if (mc.world != null) {
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ArrowEntity arrow) {
                    if (arrow.getOwner() != null && arrow.getOwner().getUuid().equals(mc.player.getUuid()) && !arrow.getVelocity().equals(Vec3d.ZERO)) {
                        if (!arrowStartPositions.containsKey(arrow)) {
                            arrowStartPositions.put(arrow, arrow.getPos());
                            arrowShots++;
                        }
                    }
                }
            }
        }

        List<ArrowEntity> arrowsToRemove = new ArrayList<>();

        arrowStartPositions.forEach((arrow, startPos) -> {

            if (arrow.getVelocity().lengthSquared() > 0) {
                double distanceInBlocks = arrow.getPos().distanceTo(startPos);
                if (distanceInBlocks > longestDistance) {
                    longestDistance = (int) (distanceInBlocks * 100);
                }

                Vec3d predictedPos = arrow.getPos();
                boolean arrowHit = false;

                int maxSteps = 10;  // too much will crash monos pc )=

                for (int step = 0; step < maxSteps; step++) {
                    predictedPos = predictedPos.add(arrow.getVelocity().multiply(0.1));

                    for (Entity entity : mc.world.getEntities()) {
                        if (entity == mc.player || !(entity instanceof PlayerEntity)) continue;

                        Box entityBox = entity.getBoundingBox();
                        if (entityBox.contains(predictedPos) || entityBox.intersects(new Box(predictedPos, predictedPos.add(arrow.getVelocity())))) {
                            arrowsHit++;
                            arrowHit = true;
                            arrowsToRemove.add(arrow);
                            break;
                        }
                    }

                    if (arrowHit) {
                        break;
                    }
                }
            } else {
                arrowsToRemove.add(arrow);
            }
        });

        arrowsToRemove.forEach(arrow -> arrowStartPositions.remove(arrow));

        Calculation.super.onTick();
    }

    public ArrowInfo onEnd() {
        accuracy = arrowShots > 0 ? (float) arrowsHit / arrowShots * 100 : 0;

        return new ArrowInfo(arrowShots, longestDistance, accuracy);
    }


    @Override
    public void onStart() {
        arrowShots = 0;
        longestDistance = 0;
        arrowsHit = 0;
        accuracy = 0;
        arrowStartPositions = new HashMap<>();
    }
}
