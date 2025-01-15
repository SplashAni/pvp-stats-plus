package splash.dev.data;

import splash.dev.PVPStatsPlus;
import splash.dev.data.gamemode.Gamemode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static splash.dev.saving.Savable.matchesFolder;

public class StoredMatchData {
    private static final List<MatchesMenu> matches;

    static {
        matches = new ArrayList<>();
    }


    private static int compareMatchIds(MatchesMenu j, MatchesMenu k, boolean invert) {
        long l = j.getMatchOutline().getId();
        long m = k.getMatchOutline().getId();

        return invert ? Long.compare(m, l) : Long.compare(l, m);
    }


    public static List<MatchesMenu> getMatchDataInCategory(Gamemode gamemode, MatchSortType sortType) {
        List<MatchesMenu> info = new ArrayList<>();

        matches.forEach(matchStatsMenu -> {
            if (matchStatsMenu.gamemode.equals(gamemode)) {
                info.add(matchStatsMenu);
            }
        });

        info.sort((j, k) -> compareMatchIds(j, k, sortType == MatchSortType.LATEST));

        return info;
    }


    public static int[] getKD(String name) {
        int enemyKills = 0;
        int kills = 0;

        for (MatchesMenu match : getMatches()) {
            if (Objects.equals(match.getMatchOutline().getUsername(), name)) {
                if (match.getMatchOutline().isWon()) kills++;
                else enemyKills++;
            }
        }
        return new int[]{kills, enemyKills};
    }

    public static MatchesMenu getMatchId(int id) {
        for (MatchesMenu matchesMenu : matches) {
            if (matchesMenu.matchOutline.getId() == id) return matchesMenu;
        }
        return null;
    }

    public static List<MatchesMenu> getMatches() {
        return matches;
    }

    public static void addInfo(MatchesMenu matchesMenu) {
        matches.add(matchesMenu);
    }

    public static void addMatch(MatchesMenu matchesMenu) {
        matches.add(matchesMenu);
    }

    public static void removeMatchID(int id) {
        matches.stream()
                .filter(match -> match.getMatchOutline().getId() == id)
                .findFirst().ifPresent(matches::remove);

        File file = new File(matchesFolder + "\\" + id + ".json");

        if (file.exists()) {
            if (file.delete()) PVPStatsPlus.LOGGER.info("Successfully deleted match " + id);
            else PVPStatsPlus.LOGGER.error("Couldn't delete match " + id);
        }
    }

    public static void removeAllMatches() {

        if (matchesFolder.exists() && matchesFolder.isDirectory()) {
            File[] files = matchesFolder.listFiles((dir, name) -> name.endsWith(".json"));

            if (files != null) {
                for (File file : files) {
                    try {
                        String fileName = file.getName();
                        int id = Integer.parseInt(fileName.substring(0, fileName.indexOf('.')));

                        removeMatchID(id);
                    } catch (NumberFormatException e) {
                        PVPStatsPlus.LOGGER.error("Invalid match file name: {}", file.getName());
                    }
                }
            }
        }

        matches.clear();
    }

}
