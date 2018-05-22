package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

public class ClientPosition implements Serializable {
    private int row;
    private int column;

    public ClientPosition(int row, int column) {
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
