import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PoolMaker {

    private ArrayList<Competitor> generateCompetitorListFromInputCSVFile(String inputCSVFilePath) {

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

    private ArrayList<Competitor> sortCompetitorByRankLevel(ArrayList<Competitor> players) {

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

        players.sort(rankLevelComparator);
        return players;
    }

    private Set<String> getFullClubSet(ArrayList<Competitor> players) {
        Set<String> fullClubSet = new HashSet<>();
        for (Competitor player : players) {
            if (player.getClubName() != null && !player.getClubName().equals("")) {
                fullClubSet.add(player.getClubName());
            }
        }
        return fullClubSet;
    }

    private ArrayList<Integer> generateDistributedPoolChainList(int playersAmount) {

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

    public ArrayList<ArrayList<Competitor>> generateDistributedPools(ArrayList<Competitor> players, ArrayList<Integer> distributedPoolChainList) {

        ArrayList<ArrayList<Competitor>> distributedPools = new ArrayList<>(2);

        for (int i = 0; i < players.size(); i++) {
            if (distributedPools.size() < distributedPoolChainList.get(i)) {
                distributedPools.add(new ArrayList<>());
            }

            distributedPools.get(distributedPoolChainList.get(i) - 1).add(players.get(i));
        }

        return distributedPools;
    }

    public ArrayList<Integer> getSameClubPlayerAmountList(ArrayList<ArrayList<Competitor>> distributedPools, String clubName) {
        ArrayList<Integer> sameClubPlayerAmountList = new ArrayList<>();
        int playerCount;

        for (ArrayList<Competitor> pool : distributedPools) {
            playerCount = 0;

            for (Competitor player : pool) {
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

    public void handleNotBalancedClub(ArrayList<ArrayList<Competitor>> distributedPools, Set<String> fullClubSet) {

        ArrayList<Integer> sameClubPlayerAmountList;
        String[] fullClubArray = fullClubSet.toArray(new String[fullClubSet.size()]);

        for (int i = 0; i < fullClubSet.size(); i++) {
            String clubName = fullClubArray[i];
            sameClubPlayerAmountList = getSameClubPlayerAmountList(distributedPools, clubName);

            if (sameClubPlayerAmountList != null && !isBalancedClub(sameClubPlayerAmountList)) {
                System.out.println(clubName + " -- " + sameClubPlayerAmountList);
                balancedPoolsBySwitchPlayers(distributedPools, clubName, sameClubPlayerAmountList);
                sameClubPlayerAmountList = getSameClubPlayerAmountList(distributedPools, clubName);
                System.out.println(clubName + " -- " + sameClubPlayerAmountList + "\n");
                i = 0;
            }
        }
    }

    public void balancedPoolsBySwitchPlayers(ArrayList<ArrayList<Competitor>> distributedPools, String notBalancedClubName, ArrayList<Integer> sameClubPlayerAmountList) {

        Pool sourcePool = new Pool();
        ArrayList<Pool> destinatePoolList = new ArrayList<>();

        for (int i = 0; i < sameClubPlayerAmountList.size(); i++) {
            if (sameClubPlayerAmountList.get(i).equals(Collections.max(sameClubPlayerAmountList))) {
                sourcePool = new Pool(distributedPools.get(i));
            } else if (sameClubPlayerAmountList.get(i).equals(Collections.min(sameClubPlayerAmountList))) {
                destinatePoolList.add(new Pool(distributedPools.get(i)));
            }
        }

        boolean tradeMade = false;

        for (Competitor sourcePlayer : sourcePool.getPlayerListWithClub(notBalancedClubName)) {
            Competitor destinatePlayer = null;
            Pool destinatePool = null;

            for (Pool destPool : destinatePoolList) {
                if (destPool.getBestCandidateWithSameRankLevel(sourcePlayer, sourcePool.getClubSet()) != null) {
                    Competitor candidatePlayer = destPool.getBestCandidateWithSameRankLevel(sourcePlayer, sourcePool.getClubSet());

                    if ((destinatePlayer == null && destinatePool == null) || isBetterCandidate(candidatePlayer, destinatePlayer, sourcePool.getClubSet())) {
                        destinatePlayer = candidatePlayer;
                        destinatePool = destPool;
                    }
                }
            }

            if (destinatePlayer != null) {
                System.out.println(sourcePlayer.getFirstName() + " " + sourcePlayer.getLastName());
                System.out.println("--> " + destinatePlayer.getFirstName() + " " + destinatePlayer.getLastName());
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

                    if ((destinatePlayer == null && destinatePool == null) || isBetterCandidate(candidatePlayer, destinatePlayer, sourcePool.getClubSet())) {
                        destinatePlayer = candidatePlayer;
                        destinatePool = destPool;
                    }
                }
            }

            if (destinatePlayer != null) {
                System.out.println(sourcePlayer.getFirstName() + " " + sourcePlayer.getLastName());
                System.out.println("--> " + destinatePlayer.getFirstName() + " " + destinatePlayer.getLastName());
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

    public static void main(String[] args) {

        String inputCSVFilePath = "C:\\Users\\zhenkuang.he\\Desktop\\Job\\poolmaker\\MEconflicts.csv";

        ArrayList<Competitor> competitorsList;
        ArrayList<Competitor> sortedCompetitorsList;
        ArrayList<Integer> distributedPoolChainList;
        ArrayList<ArrayList<Competitor>> distributedPools;
        PoolMaker poolMaker = new PoolMaker();

//        System.out.println("\nORIGINAL LIST --------\n");
        competitorsList = poolMaker.generateCompetitorListFromInputCSVFile(inputCSVFilePath);
//        for (Competitor competitor : competitorsList) {
//            System.out.println(competitor.getFirstName() + "    "
//                    + competitor.getLastName() + "    "
//                    + competitor.getClubName() + "    "
//                    + competitor.getRankLevel());
//        }


//        System.out.println("\nSORT LIST --------\n");
        sortedCompetitorsList = poolMaker.sortCompetitorByRankLevel(competitorsList);
//        for (Competitor competitor : sortedCompetitorsList) {
//            System.out.println(competitor.getFirstName() + "    "
//                    + competitor.getLastName() + "    "
//                    + competitor.getClubName() + "    "
//                    + competitor.getRankLevel());
//        }


//        System.out.println("\nPOOL DISTRIBUTED CHAIN LIST --------\n");
        distributedPoolChainList = poolMaker.generateDistributedPoolChainList(sortedCompetitorsList.size());

//        int[] poolSize = new int[Collections.max(distributedPoolChainList)];
//
//        for (int n : distributedPoolChainList) {
//            poolSize[n - 1] = poolSize[n - 1] + 1;
//        }
//
//        System.out.println(distributedPoolChainList);
//        System.out.println("List Length = " + distributedPoolChainList.size());
//
//        for (int i = 0; i < poolSize.length; i++) {
//            System.out.println("Pool " + (i + 1) + ": " + poolSize[i]);
//        }


//        System.out.println("\nPOOL LIST --------\n");
        distributedPools = poolMaker.generateDistributedPools(sortedCompetitorsList, distributedPoolChainList);

        for (int i = 1; i <= distributedPools.size(); i++) {
            System.out.println(String.format("\n--)------- Pool # %d -------(-- (%d)", i, distributedPools.get(i - 1).size()));

            for (Competitor competitor : distributedPools.get(i - 1)) {
                System.out.println(competitor.getFirstName() + "    "
                        + competitor.getLastName() + "    "
                        + competitor.getClubName() + "    "
                        + competitor.getRankLevel());
            }

        }

        Set<String> fullClubSet = poolMaker.getFullClubSet(competitorsList);
        System.out.println("\n" + fullClubSet + "\n");

        poolMaker.handleNotBalancedClub(distributedPools, fullClubSet);


    }
}
