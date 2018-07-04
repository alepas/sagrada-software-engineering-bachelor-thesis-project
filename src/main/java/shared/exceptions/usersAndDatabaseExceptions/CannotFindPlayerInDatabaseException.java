package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotFindPlayerInDatabaseException extends Exception{


    public CannotFindPlayerInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return ExceptionConstants.CANNOT_FIND_PLAYER_IN_DB;
    }



}
