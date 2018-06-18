package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotFindPlayerInDatabaseException extends Exception{


    public CannotFindPlayerInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return "Impossibile trovare il giocatore nel database.";
    }



}
