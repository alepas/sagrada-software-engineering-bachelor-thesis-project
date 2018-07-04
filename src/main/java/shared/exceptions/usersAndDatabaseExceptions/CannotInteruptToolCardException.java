package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotInteruptToolCardException extends Exception{
    private String cardId;
    private String user;

    public CannotInteruptToolCardException(String user, String cardId) {
        this.cardId=cardId;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_INTERRUPT_TOOLCARD_P1 + cardId +
                ExceptionConstants.CANNOT_INTERRUPT_TOOLCARD_P2 + user + ".";

    }




}
