import java.util.ArrayList;
import java.util.Set;

public class Run {

    public static void main(String[] args) throws InterruptedException {
        String inputCSVFilePath = args[0]; //Input file path
        //String inputCSVFilePath = "C:\\Users\\zhenkuang.he\\Desktop\\Job\\poolmaker\\MEConflicts2.csv";
        Run run = new Run();

        run.runPoolMaker(inputCSVFilePath);
    }

    public void runPoolMaker(String inputCSVFilePath) throws InterruptedException {
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

        poolMaker.printCompetitorList(sortedCompetitorsList);
        poolMaker.printPoolsList(distributedPools);
    }
}
