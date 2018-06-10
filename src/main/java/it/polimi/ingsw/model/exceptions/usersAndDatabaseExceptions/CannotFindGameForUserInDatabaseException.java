package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotFindGameForUserInDatabaseException extends Exception{


    public CannotFindGameForUserInDatabaseException() {

    }
    @Override
    public String getMessage() {


        return "There is no game in database for user";
    }



}
