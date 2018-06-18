package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotInteruptToolCardException extends Exception{
    private String cardId;
    private String user;

    public CannotInteruptToolCardException(String user, String cardId) {
        this.cardId=cardId;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return "Impossibile rispondere al messaggio della ToolCard: "+cardId+" con il valore inviato dall'utente "+user+".";

    }




}
