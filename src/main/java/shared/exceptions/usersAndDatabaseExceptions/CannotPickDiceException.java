package shared.exceptions.usersAndDatabaseExceptions;

import shared.clientInfo.ClientColor;
import shared.clientInfo.ClientDiceLocations;
import shared.constants.ExceptionConstants;

import static shared.constants.ExceptionConstants.*;

public class CannotPickDiceException extends Exception{
    private final int diceNum;
    private final int diceId;
    private final ClientColor diceColor;
    private final ClientDiceLocations where;
    private final int cause;

    /**
     * Constructor of this
     *  @param num is the dice number
     * @param color is the dice color
     * @param where tells where to place the dice
     * @param cause is the exception id
     */
    public CannotPickDiceException(int num, ClientColor color, ClientDiceLocations where, int cause) {
        this.diceNum = num;
        this.diceColor = color;
        this.where = where;
        this.cause = cause;
        this.diceId = 1000;
    }

    /**
     * Constructor of this
     *
     * @param diceId is the dice number
     * @param where tells where to place the dice
     * @param cause is the exception id
     */
    public CannotPickDiceException(int diceId, ClientDiceLocations where, int cause) {
        this.diceId = diceId;
        this.where = where;
        this.cause = cause;
        this.diceNum = 0;
        this.diceColor = ClientColor.BLUE;

    }

    /**
     * @return the message related to the exception:
     * - if the cause's id is 0 the message tells that the dice isn't in the possible set
     * - if the cause's id is 1 the message tells that the dice color isn't equal to the request one
     * - if the cause's id is 2 the message tells that the chosen dice must be equal to the dice modified by
     *   the toolcard
     * - if the cause's id is 3 the message tells that the dice doesn't respect the toolcard restrictions
     * - if the cause's id is 4 the message tells that it is not possible to use the modified dice
     * - if the cause's id is 5 the message tells that the chosen dice has already been used in the toolcard

     */
    @Override
    public String getMessage() {
        if (cause == 0)
            return ExceptionConstants.CANNOT_PICK_DICE + diceId + CANNOT_PICK_DICE_0_P1 + where.name() + CANNOT_PICK_DICE_0_P2;
        if (cause == 1)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_1;
        if (cause == 2)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_2;
        if (cause == 3)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_3;
        if (cause == 4)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_4;
        if (cause == 5)
            return ExceptionConstants.CANNOT_PICK_DICE + diceColor.name() + ": " + diceNum + CANNOT_PICK_DICE_5;

        return null;
    }




}
