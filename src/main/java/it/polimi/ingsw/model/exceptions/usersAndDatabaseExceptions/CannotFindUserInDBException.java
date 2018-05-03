package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotFindUserInDBException extends RuntimeException{
    private String user;

    public CannotFindUserInDBException(String user){
        this.user=user;
    }
    @Override
    public String getMessage() {
       return "Can't find the User "+user;
    }


}
