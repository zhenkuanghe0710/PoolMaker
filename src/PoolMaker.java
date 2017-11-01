import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PoolMaker {

    public ArrayList<Competitor> generateCompetitorListFromInputCSVFile(String inputCSVFilePath) {

        BufferedReader br = null;
        String line;
        String csvSplitBy = ",";

        ArrayList<Competitor> competitorsList = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(inputCSVFilePath));

            while ((line = br.readLine()) != null) {

                String[] competitorInfo = line.split(csvSplitBy);
                Competitor newCompetitor = new Competitor(competitorInfo[0].trim(), competitorInfo[1].trim(), competitorInfo[2].trim(), competitorInfo[3].trim());
                competitorsList.add(newCompetitor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return competitorsList;
    }

    public ArrayList<Competitor> sortCompetitorByRankLevel(ArrayList<Competitor> players) {
        Comparator<Competitor> rankLevelComparator = Utility.getRankLevelComparator();

        players.sort(rankLevelComparator.reversed());
        return players;
    }

    public Set<String> getFullClubSet(ArrayList<Competitor> players) {
        Set<String> fullClubSet = new HashSet<>();
        for (Competitor player : players) {
            if (player.getClubName() != null && !player.getClubName().equals("")) {
                fullClubSet.add(player.getClubName());
            }
        }
        return fullClubSet;
    }

    public ArrayList<Integer> generateDistributedPoolChainList(int playersAmount) {

        int poolAmount = 0;

        if ((playersAmount % 6) - (playersAmount / 6) <= 0) {
            poolAmount = playersAmount / 6;
        } else if ((playersAmount % 7) - (playersAmount / 7) <= 0) {
            poolAmount = playersAmount / 7;
        } else if ((playersAmount % 8) == 0) {
            poolAmount = playersAmount / 8;
        } else if ((playersAmount % 5) - (playersAmount / 5) <= 0) {
            poolAmount = playersAmount / 5;
        }

        int poolNumber = 1;
        int inc = 1;

        ArrayList<Integer> distributedPoolChainList = new ArrayList<>();
        for (int i = playersAmount; i > 0; i--) {

            distributedPoolChainList.add(poolNumber);

            if (poolNumber == poolAmount) {
                inc = -1;
                if (distributedPoolChainList.size() != playersAmount) {
                    distributedPoolChainList.add(poolNumber);
                    poolNumber = poolNumber + inc;
                    i--;
                    continue;
                }
            }

            if (poolNumber == 1) {
                inc = 1;
                if (distributedPoolChainList.size() != 1 && distributedPoolChainList.size() != playersAmount) {
                    distributedPoolChainList.add(poolNumber);
                    poolNumber = poolNumber + inc;
                    i--;
                    continue;
                }
            }

            poolNumber = poolNumber + inc;
        }

        return distributedPoolChainList;
    }

    public ArrayList<Pool> generateDistributedPools(ArrayList<Competitor> players, ArrayList<Integer> distributedPoolChainList) {

        ArrayList<Pool> distributedPools = new ArrayList<>(distributedPoolChainList.size());

        for (int i = 0; i < players.size(); i++) {
            if (distributedPools.size() < distributedPoolChainList.get(i)) {
                distributedPools.add(new Pool());
            }

            distributedPools.get(distributedPoolChainList.get(i) - 1).addPlayer(players.get(i));
        }

        return distributedPools;
    }

    public ArrayList<Integer> getSameClubPlayerAmountList(ArrayList<Pool> distributedPools, String clubName) {
        ArrayList<Integer> sameClubPlayerAmountList = new ArrayList<>();
        int playerCount;

        for (Pool pool : distributedPools) {
            playerCount = 0;

            for (Competitor player : pool.getFullPlayerList()) {
                if (player.getClubName().equals(clubName)) {
                    playerCount++;
                }
            }

            sameClubPlayerAmountList.add(playerCount);
        }

        return sameClubPlayerAmountList;
    }

    public boolean isBalancedClub(ArrayList<Integer> sameClubPlayerAmountList) {
        if (Collections.max(sameClubPlayerAmountList) - Collections.min(sameClubPlayerAmountList) >= 2) {
            return false;
        }
        return true;
    }

    public void handleNotBalancedClub(ArrayList<Pool> distributedPools, Set<String> fullClubSet) {

        ArrayList<Integer> sameClubPlayerAmountList;
        String[] fullClubArray = fullClubSet.toArray(new String[fullClubSet.size()]);

        for (int i = 0; i < fullClubSet.size(); i++) {
            String clubName = fullClubArray[i];
            sameClubPlayerAmountList = getSameClubPlayerAmountList(distributedPools, clubName);

            if (sameClubPlayerAmountList != null && !isBalancedClub(sameClubPlayerAmountList)) {
//                System.out.println(clubName + " -- " + sameClubPlayerAmountList);
                balancedPoolsBySwitchPlayers(distributedPools, clubName, sameClubPlayerAmountList);
//                sameClubPlayerAmountList = getSameClubPlayerAmountList(distributedPools, clubName);
//                System.out.println(clubName + " -- " + sameClubPlayerAmountList + "\n");
                i = 0;
            }
        }

        for (Pool pool : distributedPools){
            pool.getFullPlayerList().sort(Utility.getRankLevelComparator().reversed());
        }
    }

    public void balancedPoolsBySwitchPlayers(ArrayList<Pool> distributedPools, String notBalancedClubName, ArrayList<Integer> sameClubPlayerAmountList) {

        Pool sourcePool = new Pool();
        ArrayList<Pool> destinatePoolList = new ArrayList<>();

        for (int i = 0; i < sameClubPlayerAmountList.size(); i++) {
            if (sameClubPlayerAmountList.get(i).equals(Collections.max(sameClubPlayerAmountList))) {
                sourcePool = distributedPools.get(i);
            } else if (sameClubPlayerAmountList.get(i).equals(Collections.min(sameClubPlayerAmountList))) {
                destinatePoolList.add(distributedPools.get(i));
            }
        }

        boolean tradeMade = false;

        for (Competitor sourcePlayer : sourcePool.getPlayerListWithClub(notBalancedClubName)) {
            Competitor destinatePlayer = null;
            Pool destinatePool = null;

            for (Pool destPool : destinatePoolList) {
                if (destPool.getBestCandidateWithSameRankLevel(sourcePlayer, sourcePool.getClubSet()) != null) {
                    Competitor candidatePlayer = destPool.getBestCandidateWithSameRankLevel(sourcePlayer, sourcePool.getClubSet());
//                    System.out.println("||--> " + candidatePlayer.getFirstName() + " " + candidatePlayer.getLastName() + " " + candidatePlayer.getClubName() + " " + candidatePlayer.getRankLevel());

                    if ((destinatePlayer == null && destinatePool == null) || isBetterCandidate(candidatePlayer, destinatePlayer, sourcePool.getClubSet())) {
                        destinatePlayer = candidatePlayer;
                        destinatePool = destPool;
                    }
                }
            }

            if (destinatePlayer != null) {
//                System.out.println("TRADED WITH SAME LEVEL PLAYERS");
//                System.out.println(sourcePlayer.getFirstName() + " " + sourcePlayer.getLastName() + " " + sourcePlayer.getClubName() + " " + sourcePlayer.getRankLevel());
//                System.out.println("--> " + destinatePlayer.getFirstName() + " " + destinatePlayer.getLastName() + " " + destinatePlayer.getClubName() + " " + destinatePlayer.getRankLevel());
                sourcePool.switchPlayer(sourcePlayer, destinatePlayer);
                destinatePool.switchPlayer(destinatePlayer, sourcePlayer);
                tradeMade = true;
                break;
            }
        }

        if (!tradeMade) {
            Competitor sourcePlayer = sourcePool.getLowestRankPlayerWithClub(notBalancedClubName);

            Competitor destinatePlayer = null;
            Pool destinatePool = null;

            for (Pool destPool : destinatePoolList) {
                if (destPool.getBestCandidateWithLowerRankLevel(sourcePlayer, sourcePool.getClubSet()) != null) {
                    Competitor candidatePlayer = destPool.getBestCandidateWithLowerRankLevel(sourcePlayer, sourcePool.getClubSet());
//                    System.out.println("||--> " + candidatePlayer.getFirstName() + " " + candidatePlayer.getLastName() + " " + candidatePlayer.getClubName() + " " + candidatePlayer.getRankLevel());

                    if ((destinatePlayer == null && destinatePool == null) || isBetterCandidate(candidatePlayer, destinatePlayer, sourcePool.getClubSet())) {
                        destinatePlayer = candidatePlayer;
                        destinatePool = destPool;
                    }
                }
            }

            if (destinatePlayer != null) {
//                System.out.println("TRADED WITH LOWER LEVEL PLAYERS");
//                System.out.println(sourcePlayer.getFirstName() + " " + sourcePlayer.getLastName() + " " + sourcePlayer.getClubName() + " " + sourcePlayer.getRankLevel());
//                System.out.println("--> " + destinatePlayer.getFirstName() + " " + destinatePlayer.getLastName() + " " + destinatePlayer.getClubName() + " " + destinatePlayer.getRankLevel());
                sourcePool.switchPlayer(sourcePlayer, destinatePlayer);
                destinatePool.switchPlayer(destinatePlayer, sourcePlayer);
            }
        }
    }

    public boolean isBetterCandidate(Competitor candidatePlayer, Competitor destinatePlayer, Set<String> sourceClubSet) {

        if (Utility.getRankLevelComparator().compare(candidatePlayer, destinatePlayer) > 0) {
            return true;
        }

        if (candidatePlayer.getClubName() == null || candidatePlayer.getClubName().equals("") || !sourceClubSet.contains(candidatePlayer.getClubName())) {
            return true;
        }

        return false;
    }
}
