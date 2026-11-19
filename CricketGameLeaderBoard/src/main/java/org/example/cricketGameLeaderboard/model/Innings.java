package main.java.org.example.cricketGameLeaderboard.model;

import java.util.Map;

public class Innings {
    private final Team battingTeam;
    private final Team bowlingTeam;
    private final int maxBalls;
    private final Integer target;

    private final Map<String, MatchPerformance> performances;

    private int totalRuns;
    private int wickets;
    private int legalBalls;

    public Innings(
            Team battingTeam,
            Team bowlingTeam,
            int maxBalls,
            Integer target,
            Map<String, MatchPerformance> performances
    ) {
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.maxBalls = maxBalls;
        this.target = target;
        this.performances = performances;
    }

    public void processDelivery(Delivery delivery) {
        if (isComplete()) {
            throw new IllegalStateException("Innings already completed");
        }

        if (!battingTeam.containsPlayer(delivery.getBatsman())) {
            throw new IllegalArgumentException("Invalid batsman");
        }

        if (!bowlingTeam.containsPlayer(delivery.getBowler())) {
            throw new IllegalArgumentException("Invalid bowler");
        }

        if (delivery.isWicket() &&
                !battingTeam.containsPlayer(delivery.getDismissedPlayer())) {
            throw new IllegalArgumentException("Invalid dismissed player");
        }

        // Update team score.
        totalRuns += delivery.getTotalRuns();

        MatchPerformance batsmanPerformance =
                performances.get(delivery.getBatsman().getId());

        // Update batsman's statistics.
        batsmanPerformance.addRuns(delivery.getBatRuns());

        if (!delivery.isWide()) {
            batsmanPerformance.addBallFaced();
        }

        if (delivery.getBatRuns() == 4) {
            batsmanPerformance.addFour();
        }

        if (delivery.getBatRuns() == 6) {
            batsmanPerformance.addSix();
        }

        // Update legal delivery count.
        if (delivery.isLegalDelivery()) {
            legalBalls++;
        }

        // Update wicket statistics.
        if (delivery.isWicket()) {
            wickets++;

            if (delivery.isBowlerWicket()) {
                performances.get(delivery.getBowler().getId())
                        .addWicket();
            }
        }
    }

    public boolean isComplete() {
        if (legalBalls >= maxBalls) {
            return true;
        }

        if (wickets >= battingTeam.getPlayers().size() - 1) {
            return true;
        }

        return target != null && totalRuns >= target;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public int getWickets() {
        return wickets;
    }

    public int getLegalBalls() {
        return legalBalls;
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }
}