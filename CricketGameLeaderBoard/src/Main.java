import main.java.org.example.cricketGameLeaderboard.enums.*;
import main.java.org.example.cricketGameLeaderboard.model.*;
import main.java.org.example.cricketGameLeaderboard.service.LeaderboardService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Player p1 = new Player("1", "Virat");
        Player p2 = new Player("2", "Rohit");
        Player p3 = new Player("3", "Dhoni");
        Player p4 = new Player("4", "Hardik");

        Team india = new Team(
                "India",
                List.of(p1, p2)
        );

        Team australia = new Team(
                "Australia",
                List.of(p3, p4)
        );

        Match match = new Match(
                "match-1",
                GameType.T20,
                india,
                australia,
                india
        );

        LeaderboardService leaderboardService =
                new LeaderboardService();

        match.start();

        // First innings: India

        // Virat scores 4 runs.
        match.processDelivery(
                new Delivery(
                        p1, p3,
                        4, 0,
                        true, false,
                        DismissalType.NONE, null
                )
        );

        // Virat gets bowled.
        // India is all out because this test team has two players.
        match.processDelivery(
                new Delivery(
                        p1, p3,
                        0, 0,
                        true, false,
                        DismissalType.BOWLED, p1
                )
        );

        // Second innings: Australia

        // Dhoni scores 6 runs and reaches the target.
        match.processDelivery(
                new Delivery(
                        p3, p2,
                        6, 0,
                        true, false,
                        DismissalType.NONE, null
                )
        );

        // Match is completed.
        leaderboardService.addCompletedMatch(match);

        System.out.println("Match status: " + match.getStatus());

        System.out.println("\nGlobal Leaderboard:");

        for (RankedPlayer rankedPlayer :
                leaderboardService.getLeaderboard()) {

            LeaderboardEntry entry = rankedPlayer.getEntry();

            System.out.println(
                    "Rank: " + rankedPlayer.getRank()
                            + " | Player: " + entry.getPlayer().getName()
                            + " | Runs: " + entry.getTotalRuns()
                            + " | Wickets: " + entry.getTotalWickets()
                            + " | Sixes: " + entry.getTotalSixes()
            );
        }
    }
}