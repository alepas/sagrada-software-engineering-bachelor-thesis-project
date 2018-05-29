package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotPickNumberException extends Exception{
    private int number;
    private String user;

    public CannotPickNumberException(String user, int number) {
        this.number=number;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return "Can't pick the color";

    }




}
