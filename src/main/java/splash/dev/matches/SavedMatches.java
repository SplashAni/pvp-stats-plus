package splash.dev.matches;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import splash.dev.PVPStatsPlus;
import splash.dev.data.MatchStatsMenu;
import splash.dev.data.StoredMatchData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class SavedMatches implements Match {
    @Override
    public void initialize() {
        createDirs(mainFolder, matchesFolder);
        load();
        Runtime.getRuntime().addShutdownHook((new Thread(this::save)));
    }

    public void createDirs(File... files) {
        for (File file : Arrays.stream(files).parallel().toList()) {
            if (!file.exists()) file.mkdir();
        }
    }


    @Override
    public void save() {
        if (StoredMatchData.getMatches().isEmpty()) {
            PVPStatsPlus.LOGGER.warn("No games found, saving nothing");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        for (MatchStatsMenu matchStats : StoredMatchData.getMatches()) {
            JsonObject match = new JsonObject();

            match.addProperty("id", matchStats.getMatchOutline().getId());
            match.add("outline", matchStats.getMatchOutline().getJson());

            JsonObject usedItems = new JsonObject();
            matchStats.getItemUsed().forEach(itemUsed -> {
                usedItems.addProperty("item", itemUsed.item().toString());
                usedItems.addProperty("count", itemUsed.count());
            });
            match.add("items", usedItems);

            match.add("attack", matchStats.getAttackInfo().getJson());
            match.add("damage", matchStats.getDamageInfo().getJson());

            String fileName = matchesFolder + "/" + matchStats.getMatchOutline().getId() + ".json";

            if (!new File(fileName).exists()) {
                try {
                    new File(fileName).createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try (PrintWriter writer = new PrintWriter(fileName)) {
                writer.println(gson.toJson(match));
                PVPStatsPlus.LOGGER.info("Saved game data to " + fileName);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Failed to save match data to " + fileName, e);
            }
        }
    }


    @Override
    public void load() {

    }


}
