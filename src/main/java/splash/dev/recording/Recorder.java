package splash.dev.recording;

import splash.dev.data.Category;
import splash.dev.data.MatchData;
import splash.dev.data.MatchInfo;

import java.util.ArrayList;
import java.util.List;

import static splash.dev.BetterCpvp.mc;

public class Recorder {
    public boolean recording;
    List<ItemUsed> itemUsed;
    int usedItems;
    float time;

    public void startRecording() {
        if (recording) return;
        recording = true;
        itemUsed = new ArrayList<>();
        System.out.println("Recording started.");
    }

    public void stopRecording() {
        recording = false;
        MatchData.addInfo(new MatchInfo(Category.Cartpvp,
                new MatchOutline("test", usedItems, time)));
    }

    public boolean isRecording() {
        return recording;
    }


    public void tick() {
        time += 0.1f;
    }

    public void onItemUse() {
        updateItem();
    }

    public void onAttack() {
        updateItem();
    }

    public void updateItem() {
        usedItems++;
        boolean found = false;

        for (ItemUsed used : itemUsed)
            if (used.item() == mc.player.getMainHandStack()) {
                used.increment();
                found = true;
                break;
            }

        if (!found) {
            ItemUsed newItem = new ItemUsed(mc.player.getMainHandStack(), 0);
            itemUsed.add(newItem);
        }
    }
}
