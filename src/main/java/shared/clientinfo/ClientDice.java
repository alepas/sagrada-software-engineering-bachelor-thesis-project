package shared.clientinfo;

import java.io.Serializable;

/**
 * It's a copy of the cell object in the server model, it doesn't contains any logic
 */
public class ClientDice implements Serializable {
    private final ClientColor color;
    private final int number;
    private final int diceID;

    /**
     * @param color is the dice client color
     * @param number is the dice number
     * @param diceID is the dice id
     */
    public ClientDice(ClientColor color, int number, int diceID) {
        this.color = color;
        this.number = number;
        this.diceID = diceID;
    }

    public ClientColor getDiceColor( ){
        return color;
    }

    public int getDiceNumber( ){
        return number;
    }

    public int getDiceID( ){ return diceID ;}
}
