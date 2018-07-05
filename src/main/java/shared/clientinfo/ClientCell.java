package shared.clientinfo;

import java.io.Serializable;

/**
 * It's a copy of the cell object in the server model, it doesn't contains any logic
 */
public class ClientCell implements Serializable {
    private final ClientDice cellDice;
    private final ClientColor cellColor;
    private final int cellNumber;
    private final Position position;

    /**
     * @param cellDice is the client dice
     * @param cellColor is the client cell color restriction, it could be null
     * @param cellNumber is the client cell number restriction, it could be null
     * @param position is the position of the cell in the schema
     */
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
