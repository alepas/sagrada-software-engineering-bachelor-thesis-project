package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotFindUserInDBException extends Exception{
    private String user;

    /**
     * @param user is the player username
     */
    public CannotFindUserInDBException(String user){
        this.user = user;
    }

    /**
     * @return a message which says that it has been impossible to find the player in the db
     */
    @Override
    public String getMessage() {
       return ExceptionConstants.CANNOT_FIND_USER_IN_DB_P1 + user + ExceptionConstants.CANNOT_FIND_USER_IN_DB_P2;
    }


}
