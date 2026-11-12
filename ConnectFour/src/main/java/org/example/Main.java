package org.example;

import org.example.connectFour.enums.DiscColor;
import org.example.connectFour.enums.GameState;
import org.example.connectFour.model.Player;
import org.example.connectFour.service.Game;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("player1", DiscColor.RED);
        Player player2 = new Player("player2", DiscColor.YELLOW);

        Game game = new Game(player1, player2);

        int[] moves = {0, 6, 1, 6, 2, 5, 3};

        for(int column: moves) {
            Player currentPlayer = game.getCurrentPlayer();

            boolean success = game.makeMove(currentPlayer, column);

            if(!success) {
                System.out.println("Invalid move");
                continue;
            }

            System.out.println(
                    currentPlayer.getName() + "placed disc in column " + column
            );

            if(game.getState() == GameState.WON) {
                System.out.println("Winner: " + game.getWinner().getName());
                break;
            }

            if(game.getState() == GameState.DRAW) {
                System.out.println("Game ended in a draw!");
                break;
            }
        }
    }
}