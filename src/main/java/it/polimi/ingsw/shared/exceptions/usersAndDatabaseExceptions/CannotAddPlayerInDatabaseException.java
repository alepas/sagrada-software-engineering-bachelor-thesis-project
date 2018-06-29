package it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions;

public class CannotAddPlayerInDatabaseException extends Exception{


    public CannotAddPlayerInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return "Impossibile inserire il player corrente nella partita a causa di un errore interno.";
    }



}
