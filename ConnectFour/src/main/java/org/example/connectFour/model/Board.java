package org.example.connectFour.model;
import org.example.connectFour.enums.DiscColor;

public class Board {
    private static final int rows = 6;
    private static final int cols = 7;

    private final DiscColor[][] grid;

    public Board() {
        this.grid = new DiscColor[rows][cols];
    }

    public DiscColor[][] getGrid() {
        return grid;
    }

    public boolean canPlace(int column) {
        if(column < 0 || column > cols) {
            return false;
        }

        return grid[0][column] == null;
    }

    public boolean isFull() {
        for(int i=0; i< cols; i++) {
            if(grid[0][i] == null) {
                return false;
            }
        }

        return true;
    }

    public boolean checkWin(int row, int column, DiscColor color) {
        if(!inBounds(row, column) || grid[row][column] != color) {
            return false;
        }

        int[][] directions = new int[][] {
                {0, 1},
                {1, 0},
                {1, 1},
                {-1, 1}
        };

        for(int[] dir: directions) {
            int count = 1;
            count += countInDirection(row, column, dir[0], dir[1], color);
            count += countInDirection(row, column, -dir[0], -dir[1], color);

            if(count >= 4) {
                return true;
            }
        }

        return false;
    }

    public int placeDisc(int column, DiscColor color) {
        if(!canPlace(column)) {
            return -1;
        }

        for(int i=rows-1; i >=0; i--) {
            if(grid[i][column] == null) {
                grid[i][column] = color;
                return i;
            }
        }

        return -1;
    }

    private boolean inBounds(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return true;
        }

        return false;
    }

    private int countInDirection(int row, int col, int dr, int dc, DiscColor color) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;

        while(inBounds(r, c) && grid[r][c] == color) {
            count++;
            r += dr;
            c += dc;
        }

        return count;
    }
}
