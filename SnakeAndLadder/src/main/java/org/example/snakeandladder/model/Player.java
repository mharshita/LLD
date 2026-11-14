package main.java.org.example.snakeandladder.model;

public class Player {
    private final String name;
    private final int row;
    private final int col;

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
}
