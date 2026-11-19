package main.java.org.example.cricketGameLeaderboard.model;

public class RankedPlayer {
    private final int rank;
    private final LeaderboardEntry entry;

    public RankedPlayer(int rank, LeaderboardEntry entry) {
        this.rank = rank;
        this.entry = entry;
    }

    public int getRank() {
        return rank;
    }

    public LeaderboardEntry getEntry() {
        return entry;
    }
}