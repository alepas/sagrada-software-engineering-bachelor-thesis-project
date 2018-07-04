package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotFindUserInDBException extends Exception{
    private String user;

    public CannotFindUserInDBException(String user){
        this.user=user;
    }
    @Override
    public String getMessage() {
       return ExceptionConstants.CANNOT_FIND_USER_IN_DB_P1 + user + ExceptionConstants.CANNOT_FIND_USER_IN_DB_P2;
    }


}
