package splash.dev.data;

import java.util.ArrayList;
import java.util.List;

public class StoredMatchData {
    private static final List<MatchInfo> matches;
    static {
        matches = new ArrayList<>();
    }
    public static List<MatchInfo> getMatchDataInCategory(Category category) {
        List<MatchInfo> info = new ArrayList<>();
        matches.forEach(matchInfo -> {
            if(matchInfo.category == category) info.add(matchInfo);
        });
        return info;
    }

    public static MatchInfo getMatchId(int id) {
        for (MatchInfo matchInfo : matches) {
            if (matchInfo.matchOutline.getId() == id) return matchInfo;
        }
        return null;
    }


    public static List<MatchInfo> getMatches() {
        return matches;
    }

    public static void addInfo(MatchInfo matchInfo){
        matches.add(matchInfo);
    }
}
