package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class PlayerNotAuthorizedException extends RuntimeException{
    private String user;

    public PlayerNotAuthorizedException(String username ){
        user=username;
    }
    @Override
    public String getMessage() {
       return "The player "+user+" cannot do this action right now. The current turn is of another player";
    }


}
