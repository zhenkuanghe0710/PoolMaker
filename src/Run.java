import java.util.ArrayList;
import java.util.Set;

public class Run {
    public static void main(String[] args) {

        String inputCSVFilePath = "C:\\Users\\Zhenkuang\\Desktop\\poolmaker\\MEentries.csv";

        ArrayList<Competitor> competitorsList;
        ArrayList<Competitor> sortedCompetitorsList;
        ArrayList<Integer> distributedPoolChainList;
        ArrayList<Pool> distributedPools;
        Set<String> fullClubSet;

        PoolMaker poolMaker = new PoolMaker();
        competitorsList = poolMaker.generateCompetitorListFromInputCSVFile(inputCSVFilePath);
        sortedCompetitorsList = poolMaker.sortCompetitorByRankLevel(competitorsList);
        distributedPoolChainList = poolMaker.generateDistributedPoolChainList(sortedCompetitorsList.size());
        distributedPools = poolMaker.generateDistributedPools(sortedCompetitorsList, distributedPoolChainList);
        fullClubSet = poolMaker.getFullClubSet(competitorsList);

        poolMaker.handleNotBalancedClub(distributedPools, fullClubSet);

        System.out.println("\nPOOL LIST --------\n");
        for (int i = 1; i <= distributedPools.size(); i++) {
            System.out.println(String.format("\n--)------- Pool # %d -------(-- (%d)", i, distributedPools.get(i - 1).getFullPlayerList().size()));

            for (Competitor competitor : distributedPools.get(i - 1).getFullPlayerList()) {
                System.out.println(competitor.getFirstName() + "    "
                        + competitor.getLastName() + "    "
                        + competitor.getClubName() + "    "
                        + competitor.getRankLevel());
            }

        }
    }
}
