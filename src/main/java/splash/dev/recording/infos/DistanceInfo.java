package splash.dev.recording.infos;

import com.google.gson.JsonObject;

public class DistanceInfo {
    int distanceSprinted;
    int distanceCrouched;
    int distanceWalked;

    public DistanceInfo(int distanceSprinted, int distanceCrouched, int distanceWalked) {
        this.distanceSprinted = distanceSprinted;
        this.distanceCrouched = distanceCrouched;
        this.distanceWalked = distanceWalked;
    }

    public static DistanceInfo fromJson(JsonObject distanceInfo) {
        if (distanceInfo == null) {
            throw new IllegalArgumentException("distanceInfo is null");
        }

        int sprinted = distanceInfo.has("distanceSprinted") ? distanceInfo.get("distanceSprinted").getAsInt() : 0;
        int crouched = distanceInfo.has("distanceCrouched") ? distanceInfo.get("distanceCrouched").getAsInt() : 0;
        int walked = distanceInfo.has("distanceWalked") ? distanceInfo.get("distanceWalked").getAsInt() : 0;

        return new DistanceInfo(sprinted, crouched, walked);
    }

    public int getDistanceSprinted() {
        return distanceSprinted;
    }

    public int getDistanceCrouched() {
        return distanceCrouched;
    }

    public int getDistanceWalked() {
        return distanceWalked;
    }

    public JsonObject getJson() {
        JsonObject distanceInfo = new JsonObject();
        distanceInfo.addProperty("distanceSprinted", this.distanceSprinted);
        distanceInfo.addProperty("distanceCrouched", this.distanceCrouched);
        distanceInfo.addProperty("distanceWalked", this.distanceWalked);
        return distanceInfo;
    }
}
