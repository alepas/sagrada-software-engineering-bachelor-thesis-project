package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotPickNumberException extends Exception{
    private int number;
    private String user;

    public CannotPickNumberException(String user, int number) {
        //TODO: eliminare user
        this.number=number;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_PICK_NUMBER + number + ".";

    }




}
