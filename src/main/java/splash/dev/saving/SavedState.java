package splash.dev.saving;

import com.google.gson.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import splash.dev.PVPStatsPlus;
import splash.dev.data.MatchStatsMenu;
import splash.dev.data.StoredMatchData;
import splash.dev.data.gamemode.Gamemode;
import splash.dev.data.gamemode.GamemodeBind;
import splash.dev.recording.infos.*;
import splash.dev.ui.hud.HudElement;
import splash.dev.ui.hud.HudManager;
import splash.dev.ui.hud.elements.IndicatorElement;
import splash.dev.ui.hud.elements.ScoreElement;
import splash.dev.ui.hud.elements.TimerElement;
import splash.dev.util.ItemHelper;
import splash.dev.util.PotionUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static splash.dev.PVPStatsPlus.LOGGER;
import static splash.dev.util.PotionUtils.*;

public class SavedState implements Savable {


    @Override
    public void initialize() {
        createDirs(mainFolder, matchesFolder, skinsFolder);
        loadMatches();
        loadHud();
        loadBinds();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveMatches();
            saveHud();
            saveBinds();
        }));
    }

    public void createDirs(File... files) {
        for (File file : Arrays.stream(files).parallel().toList()) {
            if (!file.exists()) file.mkdir();
        }
    }

    @Override
    public void saveMatches() {
        System.out.println("Starting saveMatches method");

        if (StoredMatchData.getMatches().isEmpty()) {
            PVPStatsPlus.LOGGER.warn("No games found, saving nothing");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<MatchStatsMenu> matches = StoredMatchData.getMatches();

        matches.removeIf(matchStatsMenu -> matchStatsMenu.getMatchOutline().getId() == 0);

        for (MatchStatsMenu matchStats : StoredMatchData.getMatches()) {

            JsonObject match = new JsonObject();

            match.addProperty("gamemode", matchStats.getCategory().toString());

            match.add("outline", matchStats.getMatchOutline().getJson());

            JsonArray items = new JsonArray();

            matchStats.getItemUsed().forEach(itemUsed -> {
                JsonObject item = new JsonObject();

                String itemData = itemUsed.item().getItem().toString();

                if (itemUsed.item().getTranslationKey().contains("potion")) {
                    String translated = itemUsed.item().getTranslationKey();
                    int last = translated.lastIndexOf('.');
                    if (last != -1) {
                        String lastPart = translated.substring(last + 1);
                        itemData = itemData.concat("::" + lastPart);
                    }
                }

                item.addProperty("item", itemData);
                item.addProperty("count", itemUsed.count());
                items.add(item);
            });

            match.add("items", items);

            match.add("attack", matchStats.getAttackInfo().getJson());

            match.add("damage", matchStats.getDamageInfo().getJson());

            match.add("distance", matchStats.getDistanceInfo().getJson());

            String fileName = matchesFolder + "\\" + matchStats.getMatchOutline().getId() + ".json";
            File matchFile = new File(fileName);

            if (matchFile.exists()) {
                continue;
            }

            try {
                matchFile.createNewFile();

                try (PrintWriter writer = new PrintWriter(fileName)) {
                    writer.println(gson.toJson(match));
                    System.out.println("Saved game data to " + fileName);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Failed to save match data " + fileName, e);
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void loadMatches() {

        if (!matchesFolder.exists()) return;

        Gson gson = new GsonBuilder().create();
        File[] matchFiles = matchesFolder.listFiles((dir, name) -> name.endsWith(".json") && name.substring(0, name.length() - 5).matches("\\d+"));

        if (matchFiles == null) return;

        for (int i = 0; i < matchFiles.length; i++) {
            File matchFile = matchFiles[i];
            try {
                String fileContent = new String(Files.readAllBytes(matchFile.toPath()));

                JsonObject matchJson = gson.fromJson(fileContent, JsonObject.class);

                String gamemode = matchJson.get("gamemode").getAsString();
                Gamemode category = Gamemode.valueOf(gamemode);

                JsonObject outlineJson = matchJson.getAsJsonObject("outline");

                MatchOutline matchOutline = MatchOutline.fromJson(outlineJson, i);

                JsonArray itemsJson = matchJson.getAsJsonArray("items");
                List<ItemUsed> itemUsedList = new ArrayList<>();

                itemsJson.forEach(itemJson -> {
                    String item = itemJson.getAsJsonObject().get("item").getAsString();

                    int count = itemJson.getAsJsonObject().get("count").getAsInt();
                    ItemStack itemStack = null;

                    if (item.contains("::")) {

                        itemStack = ItemHelper.getItem(getContentBefore(item));

                        if (PotionUtils.getPotion(getContentAfter(item)) != null) {

                            itemStack.set(DataComponentTypes.POTION_CONTENTS,
                                    new PotionContentsComponent(getPotion(getContentAfter(item))));

                        }

                    } else {
                        itemStack = ItemHelper.getItem(item);
                    }

                    if (itemStack == null) return;

                    ItemUsed itemUsed = new ItemUsed(itemStack, count);
                    itemUsedList.add(itemUsed);
                });

                JsonObject attackJson = matchJson.getAsJsonObject("attack");
                AttackInfo attackInfo = AttackInfo.fromJson(attackJson);

                JsonObject damageJson = matchJson.getAsJsonObject("damage");
                DamageInfo damageInfo = DamageInfo.fromJson(damageJson);

                JsonObject distance = matchJson.getAsJsonObject("distance");
                DistanceInfo distanceInfo = DistanceInfo.fromJson(distance);

                MatchStatsMenu matchStatsMenu = new MatchStatsMenu(category,
                        matchOutline, itemUsedList, damageInfo, attackInfo,distanceInfo);

                LOGGER.info("loaded match " + matchFile.getName());

                StoredMatchData.addMatch(matchStatsMenu);

            } catch (IOException e) {
                PVPStatsPlus.LOGGER.error("Error reading match file {}", matchFile.getName(), e);
            } catch (Exception e) {
                PVPStatsPlus.LOGGER.error("Error parsing match file {}", matchFile.getName(), e);
            }
        }
    }

    @Override
    public void saveBinds() {
        if (!bindFile.exists()) {
            try {
                if (bindFile.createNewFile()) {
                    PVPStatsPlus.LOGGER.info("Successfully created a bind file.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        JsonObject json = new JsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (Gamemode value : Gamemode.values()) {
            JsonObject gamemode = new JsonObject();
            gamemode.addProperty("key", PVPStatsPlus.getBindManager().getKey(value));

            json.add(value.toString(), gamemode);
        }

        try (PrintWriter writer = new PrintWriter(bindFile)) {
            writer.println(gson.toJson(json));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadBinds() {
        if (!bindFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(bindFile))) {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            if (json != null) {
                for (GamemodeBind gamemode : PVPStatsPlus.getBindManager().getGamemodes()) {
                    String gamemodeName = gamemode.getGamemode().toString();

                    if (json.has(gamemodeName)) {
                        JsonObject gamemodeJson = json.getAsJsonObject(gamemodeName);

                        if (gamemodeJson.has("key")) {
                            int foundKey = gamemodeJson.get("key").getAsInt();
                            gamemode.setKey(foundKey);
                        }
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }


    @Override
    public void saveHud() {
        if (!hudFile.exists()) {
            try {
                if (hudFile.createNewFile()) {
                    PVPStatsPlus.LOGGER.info("Successfully created a hud file.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        HudManager hudManager = PVPStatsPlus.getHudManager();
        JsonObject json = new JsonObject();
        JsonArray elements = new JsonArray();

        hudManager.getElements().forEach(hudElement -> {
            JsonObject elementObject = new JsonObject();
            elementObject.addProperty("name", hudElement.getName());
            elementObject.addProperty("x", hudElement.getX());
            elementObject.addProperty("y", hudElement.getY());
            elementObject.addProperty("scale", hudElement.getScale());
            elementObject.addProperty("visible", hudElement.isVisible());
            elements.add(elementObject);
        });

        json.add("elements", elements);

        try (PrintWriter writer = new PrintWriter(hudFile)) {
            writer.println(gson.toJson(json));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadHud() {
        if (!hudFile.exists()) {
            PVPStatsPlus.setHudManager(new HudManager(true));
            return;
        }

        try (FileReader reader = new FileReader(hudFile)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray elements = json.getAsJsonArray("elements");

            HudManager hudManager = new HudManager(false);

            elements.forEach(element -> {
                JsonObject elementObject = element.getAsJsonObject();
                String name = elementObject.get("name").getAsString();


                Map<String, Supplier<HudElement>> elementSuppliers = Map.of(
                        "score", ScoreElement::new,
                        "indicator", IndicatorElement::new,
                        "timer", TimerElement::new
                );

                Supplier<HudElement> supplier = elementSuppliers.get(name);
                if (supplier == null) return;

                HudElement hudElement = supplier.get();

                if (hudElement == null) return;

                hudElement.setCoords(elementObject.get("x").getAsInt(), elementObject.get("y").getAsInt());
                hudElement.setScale(elementObject.get("scale").getAsFloat());
                hudElement.setVisible(elementObject.get("visible").getAsBoolean());

                hudManager.addElement(hudElement);
            });


            PVPStatsPlus.setHudManager(hudManager);

            PVPStatsPlus.LOGGER.info("HUD loaded successfully.");
        } catch (IOException e) {
            PVPStatsPlus.LOGGER.error("Error loading HUD file", e);
        }
    }


}
