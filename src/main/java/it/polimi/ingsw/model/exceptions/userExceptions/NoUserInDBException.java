package it.polimi.ingsw.model.exceptions.userExceptions;

public class NoUserInDBException extends RuntimeException{
    private String user;

    public NoUserInDBException(String username) {
        user = username;
    }
    @Override
    public String getMessage() {
       return "The user "+user+" does not exist.";
    }


}
