package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotFindPlayerInDatabaseException extends Exception{

    /**
     * @return a string that tells that it is not possible to find the player in the DB
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_FIND_PLAYER_IN_DB;
    }



}
