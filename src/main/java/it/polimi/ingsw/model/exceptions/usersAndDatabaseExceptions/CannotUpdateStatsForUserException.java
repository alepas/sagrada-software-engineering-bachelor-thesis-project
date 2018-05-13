package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotUpdateStatsForUserException extends Exception{
    private int cause;
    private String user;

    public CannotUpdateStatsForUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==0)
            return "Can't update the user " + user+" statistics due to an internal problem.";

        else if (cause==1)
            return "Can't update the user " + user+" statistics due to a database internal problem.";


        else return "Can't update the user " + user+" statistics due to an internal problem.";
    }

    public int getErrorId() {
        return cause;
    }
}
