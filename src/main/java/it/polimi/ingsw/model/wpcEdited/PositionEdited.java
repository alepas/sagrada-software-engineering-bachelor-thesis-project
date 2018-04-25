package it.polimi.ingsw.model.wpcEdited;

public class PositionEdited {
    int row;
    int column;

    public PositionEdited(int row, int column) {
        this.row = row ;
        this.column = column;
    }


    public int getRow( ){
        return row;
    }

    public int getColumn( ){
        return column;
    }
}
