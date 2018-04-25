package it.polimi.ingsw.model.exceptions.userExceptions;

public class NullTokenException extends RuntimeException{
    private String user;

    public NullTokenException(){}
    @Override
    public String getMessage() {
       return "The provided token is null";
    }


}
