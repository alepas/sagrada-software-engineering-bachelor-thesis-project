package it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions;

public class PlayerNotAuthorizedException extends Exception{
    private String user;

    public PlayerNotAuthorizedException(String username ){
        user=username;
    }
    @Override
    public String getMessage() {
       return "Il giocatore "+user+" non può eseguire azioni in questo momento.\r\nÈ il turno di un altro giocatore.";
    }


}
