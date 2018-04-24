package it.polimi.ingsw.model.exceptions.userExceptions;

public class CannotLoginUserException extends RuntimeException{
    private int cause;
    private String user;

    public CannotLoginUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==1)
        return "Can't log in the user " + user+" due to an internal problem";
        else if (cause==0)
            return "The password for "+user+" is wrong. \r\nPlease try again.";
        else return "There has been an internal problem in the user log in process.";
    }


}
