import java.util.ArrayList;

public class Competitor {

    private String firstName;
    private String lastName;
    private String clubName;
    private String rankLevel;
    private ArrayList<Competitor> playersSwappedList;

    public Competitor(String lastName, String firstName, String clubName, String rankLevel) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.clubName = clubName;
        this.rankLevel = rankLevel;
        this.playersSwappedList = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getRankLevel() {
        return rankLevel;
    }

    public void setRankLevel(String rankLevel) {
        this.rankLevel = rankLevel;
    }

    public ArrayList<Competitor> getPlayersSwappedList() { return playersSwappedList; }

    public void addSwappedPlayer(Competitor player) { playersSwappedList.add(player); }
}
