package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.*;

public class CannotUseToolCardException extends Exception{
    private int cause;
    private String cardID;

    /**
     * @param card is the toolcard's id
     * @param cause is the exception's id
     */
    public CannotUseToolCardException(String card, int cause) {
        this.cause=cause;
        this.cardID = card;
    }

    /**
     * @return a string containing the message related to the exception thrown:
     * - if the cause's id is 0 the string will tell that the toolcard isn't available iin this game
     * - if the cause's id is 1 the string will tell that the player doesn't have enough favours
     * - if the cause's id is 2 the string will tell that the toolcard must be used before place the dice
     * - if the cause's id is 3 the string will tell that the dice colors isn't equal to the request one
     * - if the cause's id is 45 the string will tell that a toolcard has already been used during this turn
     * - if the cause's id is 6 the string will tell that there aren't enough dices in the roundTrack
     * - if the cause's id is 7 the string will tell that the toolcard must be use only in the second turn of each player
     * - if the cause's id is 8 the string will tell that the toolcard must be use only in the first turn of each player
     * - if the cause's id is 9 the string will tell that there was an internal problem
     */
    @Override
    public String getMessage() {
        if (cause == 0)
        return CANNOT_PICK_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_0;
        else if (cause == 1)
            return CANNOT_PICK_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_1;
        else if (cause == 2)
            return CANNOT_PICK_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_2;
        else if (cause == 3)
            return CANNOT_USE_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_3;
        else if (cause == 4 || cause == 5)
            return CANNOT_PICK_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_45;
        else if (cause == 6)
            return CANNOT_USE_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_6;
        else if (cause == 7)
            return CANNOT_USE_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_7;
        else if (cause == 8)
            return CANNOT_USE_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_8;
        else if (cause == 9)
            return CANNOT_USE_TOOLCARD + cardID + ".";
        else return CANNOT_USE_TOOLCARD_DEF + cardID + ".";
    }


}
