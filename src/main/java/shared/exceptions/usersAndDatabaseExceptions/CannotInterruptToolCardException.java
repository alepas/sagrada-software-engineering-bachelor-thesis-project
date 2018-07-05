package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotInterruptToolCardException extends Exception{
    private final String cardId;
    private final String user;

    /**
     * @param user is the player's username
     * @param cardId is the toolcard's id
     */
    public CannotInterruptToolCardException(String user, String cardId) {
        this.cardId=cardId;
        this.user = user;
    }

    /**
     * @return a string that tells to the player that it is not possible to interrupt the toolcard with the chosen value
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_INTERRUPT_TOOLCARD_P1 + cardId +
                ExceptionConstants.CANNOT_INTERRUPT_TOOLCARD_P2 + user + ".";

    }




}
