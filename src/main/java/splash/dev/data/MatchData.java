package splash.dev.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchData {
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
    public static void addInfo(MatchInfo matchInfo){
        matches.add(matchInfo);
    }
}
