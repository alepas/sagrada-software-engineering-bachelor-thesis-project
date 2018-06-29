package shared.exceptions.usersAndDatabaseExceptions;

public class CannotUseToolCardException extends Exception{
    private int cause;
    private String cardID;

    public CannotUseToolCardException(String card, int cause) {
        this.cause=cause;
        this.cardID = card;
    }
    @Override
    public String getMessage() {
        if (cause==0)
        return "Impossibile scegliere la Tool Card " + cardID+" perchè non è disponibile nella partita corrente.";
        else if (cause==1)
            return "Impossibile scegliere la Tool Card " + cardID+" perchè il giocatore non ha abbastanza favours.";
        else if (cause==2)
            return "Impossibile scegliere la Tool Card "+ cardID +" perchè va utilizzata prima di posizionare un dado";
        else if (cause==3)
            return "Impossibile utilizzare la Tool Card "+ cardID +" perchè il dado selezionato non è del colore corretto";
        else if (cause==4)
            return "Impossibile scegliere la Tool Card "+ cardID +" perchè hai già utilizzato una Tool Card in questo turno.";
        else if (cause==5)
            return "Impossibile utilizzare la Tool Card "+ cardID +" perchè non ci sono dadi a sufficienza sulla Window Pattern Card.";
        else if (cause==6)
            return "Impossibile utilizzare la Tool Card "+ cardID +" perchè non ci sono dadi a sufficienza sul RoundTrack.";
        else if (cause==7)
            return "Impossibile utilizzare la Tool Card "+ cardID +"nel secondo turno del round.";
        else if (cause==8)
            return "Impossibile utilizzare la Tool Card "+ cardID +"nel primo turno del round.";
        else if (cause ==9)
            return "Impossibile utilizzare la Tool Card "+ cardID+".";





        else return "C'è stato un problema interno nell'utilizzo della Tool Card "+cardID+".";
    }


}
