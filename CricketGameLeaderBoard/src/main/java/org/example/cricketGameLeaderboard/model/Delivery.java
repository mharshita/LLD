package main.java.org.example.cricketGameLeaderboard.model;

import main.java.org.example.cricketGameLeaderboard.enums.DismissalType;

public class Delivery {
    private final Player batsman;
    private final Player bowler;

    private final int batRuns;
    private final int extraRuns;

    private final boolean legalDelivery;
    private final boolean wide;

    private final DismissalType dismissalType;
    private final Player dismissedPlayer;

    public Delivery(
            Player batsman,
            Player bowler,
            int batRuns,
            int extraRuns,
            boolean legalDelivery,
            boolean wide,
            DismissalType dismissalType,
            Player dismissedPlayer
    ) {
        if (batRuns < 0 || extraRuns < 0) {
            throw new IllegalArgumentException("Runs cannot be negative");
        }

        if (wide && (legalDelivery || batRuns > 0)) {
            throw new IllegalArgumentException("Invalid wide delivery");
        }

        if (dismissalType == null) {
            throw new IllegalArgumentException("Dismissal type is required");
        }

        if ((dismissalType == DismissalType.NONE) !=
                (dismissedPlayer == null)) {
            throw new IllegalArgumentException("Invalid dismissed player");
        }

        if (dismissalType != DismissalType.NONE &&
                dismissalType != DismissalType.RUN_OUT &&
                !batsman.getId().equals(dismissedPlayer.getId())) {
            throw new IllegalArgumentException(
                    "Only the batsman can be dismissed this way"
            );
        }

        this.batsman = batsman;
        this.bowler = bowler;
        this.batRuns = batRuns;
        this.extraRuns = extraRuns;
        this.legalDelivery = legalDelivery;
        this.wide = wide;
        this.dismissalType = dismissalType;
        this.dismissedPlayer = dismissedPlayer;
    }

    public Player getBatsman() {
        return batsman;
    }

    public Player getBowler() {
        return bowler;
    }

    public int getBatRuns() {
        return batRuns;
    }

    public int getExtraRuns() {
        return extraRuns;
    }

    public boolean isLegalDelivery() {
        return legalDelivery;
    }

    public boolean isWide() {
        return wide;
    }

    public DismissalType getDismissalType() {
        return dismissalType;
    }

    public Player getDismissedPlayer() {
        return dismissedPlayer;
    }

    public int getTotalRuns() {
        return batRuns + extraRuns;
    }

    public boolean isWicket() {
        return dismissalType != DismissalType.NONE;
    }

    public boolean isBowlerWicket() {
        return dismissalType == DismissalType.BOWLED ||
                dismissalType == DismissalType.CAUGHT ||
                dismissalType == DismissalType.LBW ||
                dismissalType == DismissalType.STUMPED;
    }
}