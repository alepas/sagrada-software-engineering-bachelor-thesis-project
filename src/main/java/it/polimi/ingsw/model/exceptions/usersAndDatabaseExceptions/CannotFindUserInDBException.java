package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotFindUserInDBException extends Exception{
    private String user;

    public CannotFindUserInDBException(String user){
        this.user=user;
    }
    @Override
    public String getMessage() {
       return "Impossibile trovare l'utente "+user+" nel database.";
    }


}
