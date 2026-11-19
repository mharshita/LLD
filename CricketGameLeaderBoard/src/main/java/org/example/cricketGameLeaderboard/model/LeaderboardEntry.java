package main.java.org.example.cricketGameLeaderboard.model;

public class LeaderboardEntry {
    private final Player player;

    private int totalRuns;
    private int totalWickets;
    private int totalSixes;

    public LeaderboardEntry(Player player) {
        this.player = player;
    }

    public void addPerformance(MatchPerformance performance) {
        totalRuns += performance.getRuns();
        totalWickets += performance.getWickets();
        totalSixes += performance.getSixes();
    }

    public Player getPlayer() {
        return player;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public int getTotalWickets() {
        return totalWickets;
    }

    public int getTotalSixes() {
        return totalSixes;
    }
}