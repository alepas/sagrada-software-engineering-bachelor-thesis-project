package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class NoSocketForUserException extends Exception{
    private String user;

    public NoSocketForUserException(String username) {
        user = username;
    }
    @Override
    public String getMessage() {
       return "The socket for "+user+" does not exist.";
    }


}
