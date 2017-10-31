import java.util.*;

public class Pool {

    ArrayList<Competitor> playerList;
    Comparator<Competitor> rankLevelComparator = Utility.getRankLevelComparator();

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
        if (player_1.getClubName().equals(player_2.getClubName())) {
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
}
