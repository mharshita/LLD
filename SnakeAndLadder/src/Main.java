import main.java.org.example.snakeandladder.enums.GameState;
import main.java.org.example.snakeandladder.model.Player;
import main.java.org.example.snakeandladder.service.GameService;

import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");

        Player[] playerArr = new Player[]{player1, player2, player3, player4};

        Map<GameService.Pair, GameService.Pair> snakePos = Map.of(
                new GameService.Pair(1, 3), new GameService.Pair(5, 5)
        );
        Map<GameService.Pair, GameService.Pair> ladderPos = Map.of(
                new GameService.Pair(6, 4), new GameService.Pair(8, 2)
        );

        GameService game = new GameService(snakePos, ladderPos, playerArr);

        while(game.getState() == GameState.IN_PROGRESS) {
            Player currPlayer = game.getCurrentPlayer();

            Random random = new Random();

            int move = random.nextInt(6) + 1;

            boolean success =  game.makeMove(currPlayer, move);

            if(!success) {
                System.out.println("Invalid move");
                break;
            }

            if(game.getState() == GameState.WON) {
                System.out.println("Winner: " + game.getWinner().getName());
                break;
            }
        }
    }
}