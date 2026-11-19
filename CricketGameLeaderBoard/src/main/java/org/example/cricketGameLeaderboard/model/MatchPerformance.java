package main.java.org.example.cricketGameLeaderboard.model;

public class MatchPerformance {
    private final Player player;

    private int runs;
    private int ballsFaced;
    private int fours;
    private int sixes;
    private int wickets;

    public MatchPerformance(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getRuns() {
        return runs;
    }

    public int getBallsFaced() {
        return ballsFaced;
    }

    public int getFours() {
        return fours;
    }

    public int getSixes() {
        return sixes;
    }

    public int getWickets() {
        return wickets;
    }

    public void addRuns(int runs) {
        this.runs += runs;
    }

    public void addBallFaced() {
        ballsFaced++;
    }

    public void addFour() {
        fours++;
    }

    public void addSix() {
        sixes++;
    }

    public void addWicket() {
        wickets++;
    }
}