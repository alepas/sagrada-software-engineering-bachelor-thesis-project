package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotFindGameForUserInDatabaseException extends Exception{


    public CannotFindGameForUserInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return ExceptionConstants.CANNOT_FIND_GAME_FOR_USER;
    }



}
