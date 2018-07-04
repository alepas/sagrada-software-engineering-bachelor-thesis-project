package shared.exceptions.usersAndDatabaseExceptions;

import shared.clientInfo.ClientColor;
import shared.clientInfo.ClientDiceLocations;
import shared.constants.ExceptionConstants;

import static shared.constants.ExceptionConstants.*;

public class CannotPickDiceException extends Exception{
    private int diceNum;
    private int diceId;
    private ClientColor diceColor;
    private String user;
    private ClientDiceLocations where;
    private int cause;

    public CannotPickDiceException(String user, int num, ClientColor color, ClientDiceLocations where, int cause) {
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
            return ExceptionConstants.CANNOT_PICK_DICE + diceId + CANNOT_PICK_DICE_0_P1 + where.name() + CANNOT_PICK_DICE_0_P2;
        if (cause==1)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_1;
        if (cause==2)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_2;
        if (cause==3)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_3;
        if (cause==4)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_4;
        if (cause==5)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_5;






        return null;
    }




}
