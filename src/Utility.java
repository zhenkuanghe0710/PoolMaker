import java.util.Comparator;

public class Utility {

    public static Comparator<Competitor> getRankLevelComparator() {

        Comparator<Competitor> rankLevelComparator = (player_1, player_2) -> {

            String rankLevel_1 = player_1.getRankLevel();
            String rankLevel_2 = player_2.getRankLevel();

            char levelLetter_1 = rankLevel_1.charAt(0);
            char levelLetter_2 = rankLevel_2.charAt(0);

            int levelNumber_1 = 0;
            int levelNumber_2 = 0;

            if (levelLetter_1 != 'U') {
                levelNumber_1 = Integer.parseInt(rankLevel_1.substring(1));
            }

            if (levelLetter_2 != 'U') {
                levelNumber_2 = Integer.parseInt(rankLevel_2.substring(1));
            }

            if (levelLetter_1 != levelLetter_2) {
                return Character.compare(levelLetter_1, levelLetter_2);
            } else
                return Integer.compare(levelNumber_1, levelNumber_2) * -1;
        };

        return rankLevelComparator;
    }
}
