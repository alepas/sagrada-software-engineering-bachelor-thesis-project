package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.NO_TOOLCARD_IN_USE;

public class NoToolCardInUseException extends Exception{
    private String user;


    public NoToolCardInUseException(String username) {
        //TODO: Eliminare utente
        this.user=username;

    }
    @Override
    public String getMessage() {
        return NO_TOOLCARD_IN_USE;
    }



}
