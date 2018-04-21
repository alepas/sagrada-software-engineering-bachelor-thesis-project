package it.polimi.ingsw.model;

public class Position {
    int row;
    int column;

    private void Position (int x, int y){
        row = x;
        column = y;
    }

    public int getRow( ){
        return row;
    }

    public int getColumn( ){
        return column;
    }
}
