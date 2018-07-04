package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotLoginUserException extends Exception{
    private int cause;
    private String user;

    public CannotLoginUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==0)
            return ExceptionConstants.CANNOT_LOGIN_USER_03_P1 + user + ExceptionConstants.CANNOT_LOGIN_USER_0_P2;

        else if (cause==1)
            return ExceptionConstants.CANNOT_LOGIN_USER_1_P1 + user + ExceptionConstants.CANNOT_LOGIN_USER_1_P2;
        else if (cause==2)
            return ExceptionConstants.CANNOT_LOGIN_USER_2_P1 + user + ExceptionConstants.CANNOT_LOGIN_USER_2_P2;
        if (cause==3)
            return ExceptionConstants.CANNOT_LOGIN_USER_03_P1 + user+ ExceptionConstants.CANNOT_LOGIN_USER_3_P2;

        else return ExceptionConstants.CANNOT_LOGIN_USER_4;
    }

    public int getErrorId() {
        return cause;
    }
}
