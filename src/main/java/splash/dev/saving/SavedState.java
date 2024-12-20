package splash.dev.saving;

import com.google.gson.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import splash.dev.PVPStatsPlus;
import splash.dev.data.Gamemode;
import splash.dev.data.MatchStatsMenu;
import splash.dev.data.StoredMatchData;
import splash.dev.recording.infos.AttackInfo;
import splash.dev.recording.infos.DamageInfo;
import splash.dev.recording.infos.ItemUsed;
import splash.dev.recording.infos.MatchOutline;
import splash.dev.ui.hud.HudElement;
import splash.dev.ui.hud.HudManager;
import splash.dev.ui.hud.elements.IndicatorElement;
import splash.dev.ui.hud.elements.ScoreElement;
import splash.dev.util.ItemHelper;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static splash.dev.PVPStatsPlus.LOGGER;

@SuppressWarnings("ALL")
public class SavedState implements Savable {


    @Override
    public void initialize() {
        createDirs(mainFolder, matchesFolder, skinsFolder);
        loadMatches();
        loadHud();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveMatches();
            saveHud();
        }));
    }

    public void createDirs(File... files) {
        for (File file : Arrays.stream(files).parallel().toList()) {
            if (!file.exists()) file.mkdir();
        }
    }

    @Override
    public void saveMatches() {
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

                String itemData = itemUsed.item().getItem().toString();

                if (itemUsed.item().getTranslationKey().contains("potion")) {
                    String translated = itemUsed.item().getTranslationKey();
                    int lasst = translated.lastIndexOf('.');
                    if (lasst != -1) {
                        String lastPart = translated.substring(lasst + 1);
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

            String fileName = matchesFolder + "\\" + matchStats.getMatchOutline().getId() + ".json";
            File matchFile = new File(fileName);

            if (matchFile.exists()) {
                continue;
            }

            try {
                matchFile.createNewFile();

                try (PrintWriter writer = new PrintWriter(fileName)) {
                    writer.println(gson.toJson(match));
                    PVPStatsPlus.LOGGER.info("Saved game data to {}", fileName);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Failed to save match data to " + fileName, e);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to create file for " + fileName, e);
            }
        }
    }


    @Override
    public void loadMatches() {

        if (!matchesFolder.exists()) return;

        Gson gson = new GsonBuilder().create();
        File[] matchFiles = matchesFolder.listFiles((dir, name) -> name.endsWith(".json"));

        if (matchFiles == null) return;

        for (File matchFile : matchFiles) {
            try {
                String fileContent = new String(Files.readAllBytes(matchFile.toPath()));

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
                    ItemStack itemStack = null;


                    if (item.contains("::")) {

                        itemStack = ItemHelper.getItem(getNameBefore(item));

                        if (get(getName(item)) != null) {

                            itemStack.set(DataComponentTypes.POTION_CONTENTS,
                                    new PotionContentsComponent(get(getName(item))));


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

                MatchStatsMenu matchStatsMenu = new MatchStatsMenu(category, matchOutline, itemUsedList, damageInfo, attackInfo);

                LOGGER.info("loaded match " + outlineJson.get("id"));
                StoredMatchData.addMatch(matchStatsMenu);

            } catch (IOException e) {
                PVPStatsPlus.LOGGER.error("Error reading match file {}", matchFile.getName(), e);
            } catch (Exception e) {
                PVPStatsPlus.LOGGER.error("Error parsing match file {}", matchFile.getName(), e);
            }
        }
    }

    public RegistryEntry<Potion> get(String name) {
        for (RegistryEntry<Potion> potion : PVPStatsPlus.potions) {
            if (potion.getIdAsString().contains(name)) return potion;
        }
        return null;
    }

    public String getName(String input) {
        String[] parts = input.split("::");
        if (parts.length > 1) {
            return parts[1].trim();
        }
        return "";
    }

    public String getNameBefore(String input) {
        String[] parts = input.split("::");
        if (parts.length > 0) {
            return parts[0].trim(); 
        }
        return "";
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
                        "indicator", IndicatorElement::new
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
