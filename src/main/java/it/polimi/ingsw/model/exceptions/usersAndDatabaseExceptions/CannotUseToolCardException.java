package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

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
        return "Can't setCard the Tool Card " + cardID+" because it isn't avaiable for this game.";
        else if (cause==1)
            return "Can't setCard the Tool Card" + cardID+" because the user does not have enough favours.";
        else if (cause==2)
            return "Can't setCard the Tool Card "+ cardID +"because it must be used before placing a dice";
        else if (cause==3)
            return "Can't setCard the Tool Card "+ cardID +"because the dice selected is not of the right color";
        else if (cause==4)
            return "Can't setCard the Tool Card "+ cardID +"because you have already used a toolcard";
        else if (cause==5)
            return "Can't setCard the Tool Card "+ cardID +"because don't have enough dices on the wpc";



        else return "There has been an internal problem in the user registration process.\r\nUser "+cardID+"can't be created";
    }


}
