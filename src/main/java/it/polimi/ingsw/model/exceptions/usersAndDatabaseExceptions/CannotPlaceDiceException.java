package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotPlaceDiceException extends Exception{
    private int cause;
    private String user;

    public CannotPlaceDiceException(String user, int cause) {
        this.cause=cause;
        this.user = user;
    }
    @Override
    public String getMessage() {
        if (cause==0)
        return "Can't place the dice because the dice selected is not available in the extracted dices";
        else if (cause==1)
            return "Can't place the dice because the position is not legal.";
        else return "Can't place the dice due to an internal problem";
    }

    public int getErrorId() {
        return cause;
    }



}
