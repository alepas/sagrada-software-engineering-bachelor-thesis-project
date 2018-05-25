package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

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
            return "There has been an internal problem closing the old connection for the user "+user+".";

        else if (cause==1)
            return "Can't find old socket connection for user "+user+".";

        return "There has been an internal problem closing the old connection for the user "+user+".";
    }


    public int getErrorId() {
        return cause;
    }
}
