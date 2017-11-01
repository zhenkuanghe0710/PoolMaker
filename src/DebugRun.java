import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class DebugRun {
    public static void main(String[] args) {

        String inputCSVFilePath = "C:\\Users\\Zhenkuang\\Desktop\\poolmaker\\MEentries.csv";

        ArrayList<Competitor> competitorsList;
        ArrayList<Competitor> sortedCompetitorsList;
        ArrayList<Integer> distributedPoolChainList;
        ArrayList<Pool> distributedPools;
        PoolMaker poolMaker = new PoolMaker();

        System.out.println("\nORIGINAL LIST --------\n");
        competitorsList = poolMaker.generateCompetitorListFromInputCSVFile(inputCSVFilePath);
        for (Competitor competitor : competitorsList) {
            System.out.println(competitor.getFirstName() + "    "
                    + competitor.getLastName() + "    "
                    + competitor.getClubName() + "    "
                    + competitor.getRankLevel());
        }


        System.out.println("\nSORT LIST --------\n");
        sortedCompetitorsList = poolMaker.sortCompetitorByRankLevel(competitorsList);
        for (Competitor competitor : sortedCompetitorsList) {
            System.out.println(competitor.getFirstName() + "    "
                    + competitor.getLastName() + "    "
                    + competitor.getClubName() + "    "
                    + competitor.getRankLevel());
        }


        System.out.println("\nPOOL DISTRIBUTED CHAIN LIST --------\n");
        distributedPoolChainList = poolMaker.generateDistributedPoolChainList(sortedCompetitorsList.size());

        int[] poolSize = new int[Collections.max(distributedPoolChainList)];

        for (int n : distributedPoolChainList) {
            poolSize[n - 1] = poolSize[n - 1] + 1;
        }

        System.out.println(distributedPoolChainList);
        System.out.println("List Length = " + distributedPoolChainList.size());

        for (int i = 0; i < poolSize.length; i++) {
            System.out.println("Pool " + (i + 1) + ": " + poolSize[i]);
        }


        System.out.println("\nPOOL LIST --------\n");
        distributedPools = poolMaker.generateDistributedPools(sortedCompetitorsList, distributedPoolChainList);

        for (int i = 1; i <= distributedPools.size(); i++) {
            System.out.println(String.format("\n--)------- Pool # %d -------(-- (%d)", i, distributedPools.get(i - 1).getFullPlayerList().size()));

            for (Competitor competitor : distributedPools.get(i - 1).getFullPlayerList()) {
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
