import java.util.*;

public class Pool {

    ArrayList<Competitor> playerList;
    Comparator<Competitor> rankLevelComparator = Utility.getRankLevelComparator();

    public Pool() {
        playerList = new ArrayList<>();
    }

    public void addPlayer(Competitor player) {
        if (!playerList.contains(player)) {
            playerList.add(player);
        }
    }

    public void removePlayer(Competitor player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
        }
    }

    public void switchPlayer(Competitor player_out, Competitor player_in) {
        removePlayer(player_out);
        addPlayer(player_in);
    }

    public ArrayList<Competitor> getFullPlayerList() {
        return playerList;
    }

    public ArrayList<Competitor> getPlayerListWithClub(String clubName) {
        ArrayList<Competitor> playerListWithClub = new ArrayList<>();
        for (Competitor player : playerList) {
            if (player.getClubName().equals(clubName)) {
                playerListWithClub.add(player);
            }
        }
        return playerListWithClub;
    }

    public Competitor getLowestRankPlayerWithClub(String clubName) {
        Competitor lowestRankPlayer = null;

        for (Competitor player : playerList) {
            if (player.getClubName().equals(clubName)) {
                if (lowestRankPlayer == null || rankLevelComparator.compare(player, lowestRankPlayer) < 0)
                    lowestRankPlayer = player;
            }
        }
        return lowestRankPlayer;
    }

    public Set<String> getClubSet() {
        Set<String> clubSet = new HashSet<>();
        for (Competitor player : playerList) {
            clubSet.add(player.getClubName());
        }
        return clubSet;
    }

    public boolean isSameClub(Competitor player_1, Competitor player_2) {
        if (player_1.getClubName().equals(player_2.getClubName())) {
            return true;
        }
        return false;
    }

    public Competitor getBestCandidateWithSameRankLevel(Competitor sourcePlayer, Set<String> sourceClubSet) {
        ArrayList<Competitor> candidateList = new ArrayList<>();

        for (Competitor player : playerList) {
            if (rankLevelComparator.compare(sourcePlayer, player) == 0) {
                if (!isSameClub(sourcePlayer, player) && !sourcePlayer.getPlayersSwappedList().contains(player)) {
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
                if (!isSameClub(sourcePlayer, player) && !sourcePlayer.getPlayersSwappedList().contains(player)) {
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
}
