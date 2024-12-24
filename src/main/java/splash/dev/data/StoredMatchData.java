package splash.dev.data;

import splash.dev.PVPStatsPlus;
import splash.dev.data.gamemode.Gamemode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static splash.dev.saving.Savable.matchesFolder;

public class StoredMatchData {
    private static final List<MatchStatsMenu> matches;

    static {
        matches = new ArrayList<>();
    }

    public static List<MatchStatsMenu> getMatchDataInCategory(Gamemode gamemode) {
        List<MatchStatsMenu> info = new ArrayList<>();
        matches.forEach(matchStatsMenu -> {
            if (matchStatsMenu.gamemode == gamemode) info.add(matchStatsMenu);
        });
        info.sort((j, k) -> {
            long l = j.getMatchOutline().getId();
            long m = k.getMatchOutline().getId();
            return Long.compare(m, l);
        });
        return info;
    }

    public static int[] getKD(String name) {
        int enemyKills = 0;
        int kills = 0;

        for (MatchStatsMenu match : getMatches()) {
            if (Objects.equals(match.getMatchOutline().getUsername(), name)) {
                if (match.getMatchOutline().isWon()) kills++;
                else enemyKills++;
            }
        }
        return new int[]{kills, enemyKills};
    }

    public static MatchStatsMenu getMatchId(int id) {
        for (MatchStatsMenu matchStatsMenu : matches) {
            if (matchStatsMenu.matchOutline.getId() == id) return matchStatsMenu;
        }
        return null;
    }

    public static List<MatchStatsMenu> getMatches() {
        return matches;
    }

    public static void addInfo(MatchStatsMenu matchStatsMenu) {
        matches.add(matchStatsMenu);
    }

    public static void addMatch(MatchStatsMenu matchStatsMenu) {
        matches.add(matchStatsMenu);
    }

    public static void removeMatchID(int id) {

        matches.stream()
                .filter(match -> match.getMatchOutline().getId() == id)
                .findFirst().ifPresent(matches::remove);

        File file = new File(matchesFolder + "\\" + id + ".json");

        if (file.exists()) if (file.delete()) PVPStatsPlus.LOGGER.info("Successfully deleted match " + id);
        else PVPStatsPlus.LOGGER.error("Coudln't delete match " + id);

    }
}
