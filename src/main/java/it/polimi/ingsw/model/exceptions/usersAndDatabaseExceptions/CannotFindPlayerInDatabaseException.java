package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotFindPlayerInDatabaseException extends Exception{


    public CannotFindPlayerInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return "There has been an internal problem finding the current player in game";
    }



}
