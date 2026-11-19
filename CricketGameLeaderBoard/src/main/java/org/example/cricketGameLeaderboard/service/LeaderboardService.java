package main.java.org.example.cricketGameLeaderboard.service;

import main.java.org.example.cricketGameLeaderboard.enums.MatchStatus;
import main.java.org.example.cricketGameLeaderboard.model.*;

import java.util.*;

public class LeaderboardService {

    private final Map<String, LeaderboardEntry> leaderboard;
    private final Set<String> processedMatches;

    public LeaderboardService() {
        leaderboard = new HashMap<>();
        processedMatches = new HashSet<>();
    }

    public void addCompletedMatch(Match match) {
        if (match.getStatus() != MatchStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Only completed matches can update leaderboard"
            );
        }

        if (processedMatches.contains(match.getId())) {
            throw new IllegalStateException(
                    "Match has already been processed"
            );
        }

        for (MatchPerformance performance :
                match.getPerformances().values()) {

            String playerId = performance.getPlayer().getId();

            leaderboard.putIfAbsent(
                    playerId,
                    new LeaderboardEntry(performance.getPlayer())
            );

            leaderboard.get(playerId).addPerformance(performance);
        }

        processedMatches.add(match.getId());
    }

    public List<RankedPlayer> getLeaderboard() {
        List<LeaderboardEntry> entries =
                new ArrayList<>(leaderboard.values());

        entries.sort(
                Comparator
                        .comparingInt(LeaderboardEntry::getTotalRuns)
                        .reversed()
                        .thenComparing(
                                Comparator.comparingInt(
                                        LeaderboardEntry::getTotalWickets
                                ).reversed()
                        )
                        .thenComparing(
                                Comparator.comparingInt(
                                        LeaderboardEntry::getTotalSixes
                                ).reversed()
                        )
        );

        List<RankedPlayer> result = new ArrayList<>();

        int rank = 0;

        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry current = entries.get(i);

            if (i == 0 || !isTie(current, entries.get(i - 1))) {
                rank = i + 1;
            }

            result.add(new RankedPlayer(rank, current));
        }

        return result;
    }

    private boolean isTie(
            LeaderboardEntry first,
            LeaderboardEntry second
    ) {
        return first.getTotalRuns() == second.getTotalRuns()
                && first.getTotalWickets() == second.getTotalWickets()
                && first.getTotalSixes() == second.getTotalSixes();
    }
}