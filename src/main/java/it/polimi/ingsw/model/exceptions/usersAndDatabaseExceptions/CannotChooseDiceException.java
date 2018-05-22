package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

import it.polimi.ingsw.model.dicebag.Color;

public class CannotChooseDiceException extends Exception{
    private int diceNum;
    private Color diceColor;
    private String user;

    public CannotChooseDiceException(String user, int num,Color color) {
        this.diceNum=num;
        this.diceColor=color;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return "Can't choose the dice "+diceColor+": "+diceNum+" because it isn't available in the current set of dices";

    }




}
