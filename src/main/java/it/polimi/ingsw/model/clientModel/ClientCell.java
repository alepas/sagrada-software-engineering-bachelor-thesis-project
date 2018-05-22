package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

public class ClientCell implements Serializable {
    private ClientDice cellDice;
    private ClientColor cellColor;
    private int cellNumber;
    private ClientPosition position;

    public ClientCell(ClientDice cellDice, ClientColor cellColor, int cellNumber, ClientPosition position) {
        this.cellDice = cellDice;
        this.cellColor = cellColor;
        this.cellNumber = cellNumber;
        this.position = position;
    }

    public ClientColor getCellColor( ){
        return cellColor;
    }

    public int getCellNumber( ) {
        return cellNumber;
    }

    public ClientDice getCellDice( ){
        return cellDice;
    }

    public ClientPosition getCellPosition( ){
        return position;
    }
}
