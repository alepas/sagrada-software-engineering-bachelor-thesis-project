package it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions;

public class CannotFindPlayerInDatabaseException extends Exception{


    public CannotFindPlayerInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return "Impossibile trovare il giocatore nel database.";
    }



}