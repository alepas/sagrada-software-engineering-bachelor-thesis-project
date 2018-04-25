package it.polimi.ingsw.model.exceptions.userExceptions;

public class CannotFindUserInDB extends RuntimeException{
    private String user;

    public CannotFindUserInDB(String user){
        this.user=user;
    }
    @Override
    public String getMessage() {
       return "Can't find the User "+user;
    }


}
