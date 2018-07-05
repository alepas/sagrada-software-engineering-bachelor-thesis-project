package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotPickNumberException extends Exception{
    private final int number;

    /**
     * @param number is the number chosen by the player
     */
    public CannotPickNumberException(int number) { this.number=number; }


    /**
     * @return a string that tells that the chosen number is incorrect
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_PICK_NUMBER + number + ".";

    }




}
