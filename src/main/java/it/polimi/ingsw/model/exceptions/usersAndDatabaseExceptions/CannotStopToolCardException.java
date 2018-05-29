package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotStopToolCardException extends Exception{
    private String cardId;
    private String user;

    public CannotStopToolCardException(String user, String cardId) {
        this.cardId=cardId;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return "Can't stop the toolCard: "+cardId+" from user "+user;

    }




}
