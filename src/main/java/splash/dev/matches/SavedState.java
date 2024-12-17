package splash.dev.matches;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import splash.dev.PVPStatsPlus;
import splash.dev.data.Gamemode;
import splash.dev.data.MatchStatsMenu;
import splash.dev.data.StoredMatchData;
import splash.dev.recording.infos.AttackInfo;
import splash.dev.recording.infos.DamageInfo;
import splash.dev.recording.infos.ItemUsed;
import splash.dev.recording.infos.MatchOutline;
import splash.dev.util.ItemHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedState implements Match {
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

            match.addProperty("gamemode", matchStats.getCategory().toString());
            match.add("outline", matchStats.getMatchOutline().getJson());


            JsonArray items = new JsonArray();

            matchStats.getItemUsed().forEach(itemUsed -> {
                JsonObject item = new JsonObject();
                item.addProperty("item", itemUsed.item().getItem().toString());
                item.addProperty("count", itemUsed.count());
                items.add(item);
            });

            match.add("items", items);


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

        if (!matchesFolder.exists()) return;

        Gson gson = new GsonBuilder().create();
        File[] matchFiles = matchesFolder.listFiles((dir, name) -> name.endsWith(".json"));

        if (matchFiles == null) return;

        for (File matchFile : matchFiles) {
            try {
                String fileContent = new String(java.nio.file.Files.readAllBytes(matchFile.toPath()));

                JsonObject matchJson = gson.fromJson(fileContent, JsonObject.class);

                String gamemode = matchJson.get("gamemode").getAsString();
                Gamemode category = Gamemode.valueOf(gamemode);

                JsonObject outlineJson = matchJson.getAsJsonObject("outline");
                MatchOutline matchOutline = MatchOutline.fromJson(outlineJson);

                JsonArray itemsJson = matchJson.getAsJsonArray("items");
                List<ItemUsed> itemUsedList = new ArrayList<>();
                itemsJson.forEach(itemJson -> {
                    String item = itemJson.getAsJsonObject().get("item").getAsString();
                    int count = itemJson.getAsJsonObject().get("count").getAsInt();

                    ItemStack itemStack = ItemHelper.getItem(item).getDefaultStack();

                    if (itemStack == null) return;
                    ItemUsed itemUsed = new ItemUsed(itemStack, count);
                    itemUsedList.add(itemUsed);
                });

                JsonObject attackJson = matchJson.getAsJsonObject("attack");
                AttackInfo attackInfo = AttackInfo.fromJson(attackJson);

                JsonObject damageJson = matchJson.getAsJsonObject("damage");
                DamageInfo damageInfo = DamageInfo.fromJson(damageJson);

                MatchStatsMenu matchStatsMenu = new MatchStatsMenu(category, matchOutline, itemUsedList, damageInfo, attackInfo);

                StoredMatchData.addMatch(matchStatsMenu);

            } catch (IOException e) {
                PVPStatsPlus.LOGGER.error("Error reading match file {}", matchFile.getName(), e);
            } catch (Exception e) {
                PVPStatsPlus.LOGGER.error("Error parsing match file {}", matchFile.getName(), e);
            }
        }
    }


}
