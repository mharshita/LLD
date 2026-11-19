package main.java.org.example.cricketGameLeaderboard.model;

import main.java.org.example.cricketGameLeaderboard.enums.GameType;
import main.java.org.example.cricketGameLeaderboard.enums.MatchStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Match {
    private final String id;
    private final GameType type;

    private final Team team1;
    private final Team team2;
    private final Team firstBattingTeam;

    private final Map<String, MatchPerformance> performances;

    private MatchStatus status;
    private Innings currentInnings;
    private int inningsCount;

    public Match(
            String id,
            GameType type,
            Team team1,
            Team team2,
            Team firstBattingTeam
    ) {
        if (team1 == team2 ||
                (firstBattingTeam != team1 &&
                        firstBattingTeam != team2)) {
            throw new IllegalArgumentException("Invalid teams");
        }

        this.id = id;
        this.type = type;
        this.team1 = team1;
        this.team2 = team2;
        this.firstBattingTeam = firstBattingTeam;

        this.status = MatchStatus.SCHEDULED;
        this.performances = new HashMap<>();

        initializePerformances(team1);
        initializePerformances(team2);
    }

    private void initializePerformances(Team team) {
        for (Player player : team.getPlayers()) {
            if (performances.containsKey(player.getId())) {
                throw new IllegalArgumentException(
                        "Player IDs must be unique within a match"
                );
            }

            performances.put(
                    player.getId(),
                    new MatchPerformance(player)
            );
        }
    }

    public void start() {
        if (status != MatchStatus.SCHEDULED) {
            throw new IllegalStateException("Match cannot be started");
        }

        status = MatchStatus.IN_PROGRESS;
        inningsCount = 1;

        currentInnings = new Innings(
                firstBattingTeam,
                getOtherTeam(firstBattingTeam),
                type.getMaxBalls(),
                null,
                performances
        );
    }

    public void processDelivery(Delivery delivery) {
        if (status != MatchStatus.IN_PROGRESS) {
            throw new IllegalStateException("Match is not in progress");
        }

        currentInnings.processDelivery(delivery);

        if (currentInnings.isComplete()) {
            if (inningsCount == 1) {
                startSecondInnings();
            } else {
                status = MatchStatus.COMPLETED;
            }
        }
    }

    private void startSecondInnings() {
        inningsCount++;

        Team secondBattingTeam = getOtherTeam(firstBattingTeam);

        int target = currentInnings.getTotalRuns() + 1;

        currentInnings = new Innings(
                secondBattingTeam,
                firstBattingTeam,
                type.getMaxBalls(),
                target,
                performances
        );
    }

    private Team getOtherTeam(Team team) {
        return team == team1 ? team2 : team1;
    }

    public void abandon() {
        if (status != MatchStatus.IN_PROGRESS &&
                status != MatchStatus.SCHEDULED) {
            throw new IllegalStateException(
                    "Match cannot be abandoned"
            );
        }

        status = MatchStatus.ABANDONED;
    }

    public String getId() {
        return id;
    }

    public GameType getType() {
        return type;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public Map<String, MatchPerformance> getPerformances() {
        return Collections.unmodifiableMap(performances);
    }
}