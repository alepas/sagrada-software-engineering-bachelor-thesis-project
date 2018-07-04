package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotAddPlayerInDatabaseException extends Exception{

    public CannotAddPlayerInDatabaseException() { }

    @Override
    public String getMessage() {

        return ExceptionConstants.CANNOT_ADD_PLAYER_IN_DB;
    }

}
