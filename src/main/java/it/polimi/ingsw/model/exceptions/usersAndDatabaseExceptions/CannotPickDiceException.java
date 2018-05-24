package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.dicebag.Color;

public class CannotPickDiceException extends Exception{
    private int diceNum;
    private Color diceColor;
    private String user;
    private ClientDiceLocations where;

    public CannotPickDiceException(String user, int num, Color color, ClientDiceLocations where) {
        this.diceNum=num;
        this.diceColor=color;
        this.user = user;
        this.where=where;
    }
    @Override
    public String getMessage() {
        return "Can't choose the dice "+diceColor+": "+diceNum+" because it isn't available in the "+where.name()+" set of dices";

    }




}
