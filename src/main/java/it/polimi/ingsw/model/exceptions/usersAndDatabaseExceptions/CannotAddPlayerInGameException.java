package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotAddPlayerInGameException extends Exception{


    public CannotAddPlayerInGameException() {

    }
    @Override
    public String getMessage() {


        return "There has been an internal problem adding the current player in game";
    }



}
