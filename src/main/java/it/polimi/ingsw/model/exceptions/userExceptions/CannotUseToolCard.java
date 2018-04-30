package it.polimi.ingsw.model.exceptions.userExceptions;

public class CannotUseToolCard extends RuntimeException{
    private int cause;
    private String cardID;

    public CannotUseToolCard(String card, int cause) {
        this.cause=cause;
        this.cardID = card;
    }
    @Override
    public String getMessage() {
        if (cause==0)
        return "Can't use the Tool Card " + cardID+" because it isn't avaiable for this game.";
        else if (cause==1)
            return "Can't use the Tool Card" + cardID+" because the user does not have enough favours.";
        else return "There has been an internal problem in the user registration process.\r\nUser "+cardID+"can't be created";
    }


}
