package it.polimi.ingsw.model.exceptions;

public class CannotRegisterUserException extends RuntimeException{
    private int cause;
    private String user;

    public CannotRegisterUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==1)
        return "Can't create the new user " + user+" due to an internal problem";
        else if (cause==0)
            return "The username "+user+" has been already used. \r\nCan't register a new user with the same username.";
        else return "There has been an internal problem in the user registration process.\r\nUser "+user+"can't be created";
    }


}
