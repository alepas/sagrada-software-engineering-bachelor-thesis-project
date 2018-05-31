package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.wpc.Wpc;

public class PublicObjectiveCard7  extends PublicObjectiveCard {
    //SFUMATURE SCURE

    public PublicObjectiveCard7(){
        this.id = POCConstants.POC7_ID;
        this.name = POCConstants.POC7_NAME;
        this.description = POCConstants.POC7_DESCRIPTION;
    }

    @Override
    public int calculateScore(Wpc wpc) {
        int score = 0;

        int numOf5 = wpc.numDicesOfShade(5);
        int numOf6 = wpc.numDicesOfShade(6);

        int count = Math.min(numOf5, numOf6);

        return count*POCConstants.POC7_SCORE;
    }
}