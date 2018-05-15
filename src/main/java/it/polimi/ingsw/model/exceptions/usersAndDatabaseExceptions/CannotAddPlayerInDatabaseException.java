package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotAddPlayerInDatabaseException extends Exception{


    public CannotAddPlayerInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return "There has been an internal problem adding the current player in game";
    }



}
