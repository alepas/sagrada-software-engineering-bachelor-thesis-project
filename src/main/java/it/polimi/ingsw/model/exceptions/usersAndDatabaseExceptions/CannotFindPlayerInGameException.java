package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotFindPlayerInGameException extends Exception{


    public CannotFindPlayerInGameException() {

    }
    @Override
    public String getMessage() {


        return "There has been an internal problem finding the current player in game";
    }



}
