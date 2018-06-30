package shared.exceptions.usersAndDatabaseExceptions;

public class CannotPerformThisMoveException extends Exception{
    private int cause;
    private String user;
    private boolean endTurn;


    public CannotPerformThisMoveException(String username, int cause, boolean endTurn) {

        this.cause=cause;
        this.user = username;
        this.endTurn=endTurn;
    }
    @Override
    public String getMessage() {
        String nameOfMove;
        if (endTurn)
            nameOfMove="terminare il turno corrente";
        else nameOfMove="eseguire la mossa";

        if (cause==0)
            return "Impossibile "+nameOfMove+" per il giocatore "+user+".\r\nC'è una mossa che deve essere terminata: posizionare un dado. Completala o cancella la mossa per terminare il turno.";

        else if (cause==1)
            return "Impossibile "+nameOfMove+" per il giocatore "+user+"\n" +
                    "C'è una mossa che deve essere terminata: stai utilizzando una toolcard. Completala o cancella la ToolCard per terminare il turno.";
        else if (cause==2)
            return "Impossibile eseguire questa mossa perchè non è disponibile in questo momento.\r\nSegui i prossimi passi visualizzati a schermo";

        else if (cause==3)
            return "Impossibile eseguire questa mossa perchè è già stata eseguita nel turno corrente.\r\nSegui i prossimi passi visualizzati a schermo";

        return "Impossibile "+nameOfMove+" per il giocatore "+user+".";
    }


    public int getErrorId() {
        return cause;
    }
}
