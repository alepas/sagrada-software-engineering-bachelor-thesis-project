package it.polimi.ingsw.shared.clientInfo;

import java.io.Serializable;

public class ClientCell implements Serializable {
    private final ClientDice cellDice;
    private final ClientColor cellColor;
    private final int cellNumber;
    private final Position position;

    public ClientCell(ClientDice cellDice, ClientColor cellColor, int cellNumber, Position position) {
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

    public Position getCellPosition( ){
        return position;
    }
}
