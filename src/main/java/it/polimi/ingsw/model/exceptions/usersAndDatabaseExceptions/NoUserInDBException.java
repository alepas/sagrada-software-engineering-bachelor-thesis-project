package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

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
