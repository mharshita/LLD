package main.java.org.example.cricketGameLeaderboard.enums;

public enum GameType {
    T20(120),
    ODI(300);

    private final int maxBalls;

    GameType(int maxBalls) {
        this.maxBalls = maxBalls;
    }

    public int getMaxBalls() {
        return maxBalls;
    }
}
