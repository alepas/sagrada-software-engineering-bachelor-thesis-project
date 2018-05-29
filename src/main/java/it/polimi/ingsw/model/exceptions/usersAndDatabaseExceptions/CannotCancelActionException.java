package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotCancelActionException extends Exception{
    private int mode;
    //0=action normale 1=azione dentro la toolcard 2=la toolcard stessa
    private String user ;
    private String cardID;


    public CannotCancelActionException(String user){
        this.user=user;
        this.cardID=null;
        this.mode=0;
    }
    public CannotCancelActionException(String user, String card, int mode) {
        this.user=user;
        this.cardID = card;
        this.mode=mode;
    }

    @Override
    public String getMessage() {
        String temp;
        switch (mode) {
            case 0:
                return "Can't cancelCard the current action for user " + user;
            case 1:
                return "Can't cancelCard the current action for user " + user + " because you have to complete the action for this tool card: " + cardID;
            case 2:
                return "Can't cancelCard the setCard of the Tool Card " + cardID +" for the user " + user;

        }
        return null;
    }


}
