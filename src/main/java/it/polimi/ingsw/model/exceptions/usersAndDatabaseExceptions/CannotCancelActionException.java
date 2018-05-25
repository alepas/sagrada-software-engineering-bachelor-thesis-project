package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotCancelActionException extends Exception{
    private boolean toolcard;
    private String user ;
    private String cardID;

    public CannotCancelActionException(String user){
        this.user=user;
        this.cardID=null;
        toolcard=false;
    }
    public CannotCancelActionException(String user, String card) {
        this.user=user;
        this.cardID = card;
        toolcard=true;
    }
    @Override
    public String getMessage() {
        if (toolcard)
        return "Can't cancel the use of the Tool Card " + cardID+" for the user "+user;
        else return "Can't cancel the current action for user "+user;

    }


}
