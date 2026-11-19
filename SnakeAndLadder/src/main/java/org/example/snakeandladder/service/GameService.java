package main.java.org.example.snakeandladder.service;

import main.java.org.example.snakeandladder.enums.GameState;
import main.java.org.example.snakeandladder.model.Board;
import main.java.org.example.snakeandladder.model.Player;

import java.util.Map;

public class GameService {
    public record Pair(int first, int second) {}

    Board board;
    Map<Pair, Pair> snakePos;
    Map<Pair, Pair> ladderPos;
    Player currentPlayer;
    GameState state;
    Player winner;
    Player[] playerArr;

    public GameService(Map<Pair, Pair> snakePos, Map<Pair, Pair> ladderPos, Player[] playerArr) {
        this.board = new Board(snakePos, ladderPos);
        this.snakePos = snakePos;
        this.ladderPos = ladderPos;
        this.currentPlayer = playerArr[0];
        this.state = GameState.IN_PROGRESS;
        this.winner = null;
        this.playerArr = playerArr;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getState() {
        return state;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getNextPlayer() {
        int idx = 0;
        for(int i=0; i<playerArr.length; i++) {
            if(playerArr[i] == currentPlayer) {
                if(i == playerArr.length - 1) {
                    idx = 0;
                }
                else {
                    idx = i + 1;
                }
            }
        }

        return playerArr[idx];
    }

    public boolean makeMove(Player player, int moves) {
        if (state != GameState.IN_PROGRESS) {
            return false;
        }

        if(player != currentPlayer) {
            return false;
        }

        int currentRow = player.getPlayerRow();
        int currentCol = player.getPlayerCol();

        int[] finalCell = board.getCellAfterMove(moves, currentRow, currentCol);

        if(!board.isValidMove(moves, finalCell[0], finalCell[1])) {
            return false;
        }

        player.moveTo(finalCell[0], finalCell[1]);

        if(board.checkWin(finalCell[0], finalCell[1])) {
            state = GameState.WON;
            winner = player;
        }
        else {
            int[][] grid = board.getGrid();
            grid[finalCell[0]][finalCell[1]] = 1;
            currentPlayer = getNextPlayer();
        }

        return true;
    }
}
