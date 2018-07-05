package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotLoginUserException extends Exception{
    private int cause;
    private String user;

    /**
     * @param username is the username of the player that would like to login
     * @param cause is the id of the reason why the exception must be thrown
     */
    public CannotLoginUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }

    /**
     * @return the string that will explain to the player which is the problem:
     * - if the cause's id is 0 the message will tell that it is not possible login because of internal problems
     * - if the cause's id is 1 the message will tell that the given password is incorrect
     * - if the cause's id is 2 the message will tell that an account with the given username doesn't exist
     * - if the cause's id is 3 the message will tell that the served had a problem in removing the old connection
     * - else if the problem is unknown
     */
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
