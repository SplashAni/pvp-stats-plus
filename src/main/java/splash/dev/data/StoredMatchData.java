package splash.dev.data;

import java.util.ArrayList;
import java.util.List;

public class StoredMatchData {
    private static final List<MatchStatsMenu> matches;
    static {
        matches = new ArrayList<>();
    }
    public static List<MatchStatsMenu> getMatchDataInCategory(Category category) {
        List<MatchStatsMenu> info = new ArrayList<>();
        matches.forEach(matchStatsMenu -> {
            if(matchStatsMenu.category == category) info.add(matchStatsMenu);
        });
        return info;
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

    public static void addInfo(MatchStatsMenu matchStatsMenu){
        matches.add(matchStatsMenu);
    }
}
