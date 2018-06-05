package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.dicebag.Color;

public class CannotPickDiceException extends Exception{
    private int diceNum;
    private int diceId;
    private Color diceColor;
    private String user;
    private ClientDiceLocations where;
    private int cause;

    public CannotPickDiceException(String user, int num, Color color, ClientDiceLocations where, int cause) {
        this.diceNum=num;
        this.diceColor=color;
        this.user = user;
        this.where=where;
        this.cause = cause;
    }

    public CannotPickDiceException(String user, int diceId, ClientDiceLocations where, int cause) {
        this.diceId=diceId;
        this.user = user;
        this.where=where;
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        if (cause==0)
            return "Can't choose the dice "+diceId+" because it isn't available in the "+where.name()+" set of dices";
        if (cause==1)
            return "Can't choose the dice "+diceColor+": "+diceNum+" because it does not match the color required by the tool card";
        if (cause==2)
            return "Can't choose the dice "+diceColor+": "+diceNum+" because it does not match the restrictions of the tool card";
        if (cause==3)
            return "Can't choose the dice "+diceColor+": "+diceNum+" because you have to use the dice modified by toolCard";
        if (cause==4)
            return "Can't choose the dice "+diceColor+": "+diceNum+" because you have to use another dice not modified by toolCard";



        return null;
    }




}
