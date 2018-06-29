package shared.exceptions.usersAndDatabaseExceptions;

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
                return "Impossibile cancellare l'azione per il giocatore corrente: " + user;
            case 1:
                return "Impossibile cancellare l'azione per il giocatore corrente (" + user + ").\r\n Le azioni richieste dalla toolcard " + cardID+" devono essere completate.";
            case 2:
                return "Impossibile cancellare la toolcard " + cardID +" per il giocatore " + user;
            case 3:
                return "Impossibile cancellare l'azione per il giocatore " + user + " a causa di un problema interno nella toolcard: " + cardID;


        }
        return null;
    }


}
