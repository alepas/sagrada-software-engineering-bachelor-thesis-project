package it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions;

public class NullTokenException extends Exception{
    private String user;

    public NullTokenException(){}
    @Override
    public String getMessage() {
       return "The provided token is null";
    }


}