import java.util.*;

public class Pool {

    ArrayList<Competitor> playerList;
    Comparator<Competitor> rankLevelComparator = getRankLevelComparator();

    public Pool() {
        playerList = new ArrayList<>();
    }

    public Pool(ArrayList<Competitor> playerList) {
        this.playerList = playerList;
    }

    public boolean addPlayer(Competitor player) {
        if (!playerList.contains(player)) {
            playerList.add(player);
            return true;
        } else {
            return false;
        }

    }

    public boolean removePlayer(Competitor player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
            return true;
        } else {
            return false;
        }
    }

    public boolean switchPlayer(Competitor player_out, Competitor player_in) {
        if (playerList.contains(player_out)) {
            playerList.remove(player_out);
            playerList.add(player_in);
            return true;
        } else {
            return false;
        }
    }

    public int getClubAmount(String clubName) {
        int count = 0;
        for (Competitor player : playerList) {
            if (player.getClubName().equals(clubName)) {
                count++;
            }
        }
        return count;
    }

    public Set<String> getClubSet() {
        Set<String> clubSet = new HashSet<>();
        for (Competitor player : playerList) {
            clubSet.add(player.getClubName());
        }
        return clubSet;
    }

    public boolean isSameClub(Competitor player_1, Competitor player_2) {
        if (player_1.getClubName().equals(player_2)) {
            return true;
        }
        return false;
    }

    public Competitor getBestCandidateWithSameRankLevel(Competitor sourcePlayer, Set<String> sourceClubSet) {
        ArrayList<Competitor> candidateList = new ArrayList<>();

        for (Competitor player : playerList) {
            if (rankLevelComparator.compare(sourcePlayer, player) == 0) {
                if (!isSameClub(sourcePlayer, player)) {
                    candidateList.add(player);
                }
            }
        }

        if (candidateList.size() == 0) {
            return null;
        } else if (candidateList.size() > 1) {
            for (Competitor player : candidateList) {
                if (player.getClubName() == null || player.getClubName().equals("") || !sourceClubSet.contains(player.getClubName())) {
                    return player;
                }
            }
        }
        return candidateList.get(0);
    }

    public Competitor getBestCandidateWithLowerRankLevel(Competitor sourcePlayer, Set<String> sourceClubSet) {
        ArrayList<Competitor> candidateList = new ArrayList<>();

        for (Competitor player : playerList) {
            if (rankLevelComparator.compare(sourcePlayer, player) > 0) {
                if (!isSameClub(sourcePlayer, player)) {
                    if (candidateList.size() == 0 || rankLevelComparator.compare(candidateList.get(0), player) == 0) {
                        candidateList.add(player);
                    }
                }
            }
        }

        if (candidateList.size() == 0) {
            return null;
        } else if (candidateList.size() > 1) {
            for (Competitor player : candidateList) {
                if (player.getClubName() == null || player.getClubName().equals("") || !sourceClubSet.contains(player.getClubName())) {
                    return player;
                }
            }
        }
        return candidateList.get(0);
    }

    public Comparator<Competitor> getRankLevelComparator() {

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
