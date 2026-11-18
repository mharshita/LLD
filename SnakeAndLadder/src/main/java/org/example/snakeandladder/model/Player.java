package main.java.org.example.snakeandladder.model;

public class Player {
    private final String name;
    private int row;
    private int col;

    public Player(String name) {
        this.name = name;
        this.row = 9;
        this.col = 0;
    }

    public String getName() {
        return name;
    }

    public int getPlayerRow() {
        return row;
    }

    public int getPlayerCol() {
        return col;
    }

    public void moveTo(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
