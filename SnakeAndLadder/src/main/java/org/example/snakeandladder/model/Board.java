package main.java.org.example.snakeandladder.model;
import main.java.org.example.snakeandladder.service.GameService;

import java.util.Map;

public class Board {
    private static final int rows = 10;
    private static final int cols = 10;

    private int[][] grid;
    private Map<GameService.Pair, GameService.Pair> snakePos;
    private Map<GameService.Pair, GameService.Pair> ladderPos;

    public Board(Map<GameService.Pair, GameService.Pair> snakePos, Map<GameService.Pair, GameService.Pair> ladderPos) {
        this.grid = new int[rows][cols];
        this.snakePos = snakePos;
        this.ladderPos = ladderPos;
    }

    public int[][] getGrid() {
        return grid;
    }

    public boolean inBounds(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return true;
        }

        return false;
    }

    public int[] getCellAfterMove(int move, int row, int col) {
        int newRow = 0;
        int newCol = 0;

        boolean isEven = false;

        if(row % 2 == 0) {
            isEven = true;
        }

        if(!isEven) {
            if(col + move > 9) {
                int temp = 9 - col;
                newCol = move - temp + 1;
                newRow = row - 1;
            }
            else {
                newRow = row;
                newCol = col + move;
            }
        }
        else {
            if(col - move >= 0) {
                int temp = move - col;
                newCol = move - temp + 1;
                newRow = row - 1;
            }
            else {
                newRow = row;
                newCol = col + move;
            }
        }

        GameService.Pair key = new GameService.Pair(newRow, newCol);

        if(snakePos.containsKey(key)) {
            GameService.Pair val = snakePos.get(key);
            newRow = val.first();
            newCol = val.second();
        }
        else if (ladderPos.containsKey(key)) {
            GameService.Pair val = ladderPos.get(key);
            newRow = val.first();
            newCol = val.second();
        }

        return new int[]{newRow, newCol};
    }

    public boolean isValidMove(int move, int row, int col) {
        if(!inBounds(row, col)) {
            return false;
        }

        if(move < 1 || move > 6) {
            return false;
        }

        if(grid[row][col] != 0) {
            return false;
        }

        return true;
    }

    public boolean checkWin(int row, int col) {
        return row == 0 && col == 9;
    }
}
