package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

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
            return "Can't log in the user " + user+" due to an internal problem";

        else if (cause==1)
            return "The password for "+user+" is wrong. \r\nPlease try again.";
        else if (cause==2)
            return "The user "+user+" does not exist.";
        if (cause==3)
            return "Can't log in the user " + user+" because we were not able to close the old connection.";

        else return "There has been an internal problem in the user log in process.";
    }

    public int getErrorId() {
        return cause;
    }
}
