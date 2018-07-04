package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotCloseOldConnectionException extends Exception{
    private int cause;
    private String user;

    public CannotCloseOldConnectionException(String username, int errorId) {
        cause=errorId;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==0)
            return ExceptionConstants.CANNOT_CLOSE_OLD_CONNECTION_0 + user + ".";

        else if (cause==1)
            return ExceptionConstants.CANNOT_CLOSE_OLD_CONNECTION_1 + user + ".";

        return ExceptionConstants.CANNOT_CLOSE_OLD_CONNECTION_2 + user + ".";
    }


    public int getErrorId() {
        return cause;
    }
}
