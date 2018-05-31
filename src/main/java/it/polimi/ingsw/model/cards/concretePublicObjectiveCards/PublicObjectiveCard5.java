package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.wpc.Wpc;

public class PublicObjectiveCard5  extends PublicObjectiveCard {
    //SFUMATURE CHIARE

    public PublicObjectiveCard5(){
        this.id = POCConstants.POC5_ID;
        this.name = POCConstants.POC5_NAME;
        this.description = POCConstants.POC5_DESCRIPTION;
    }

    @Override
    public int calculateScore(Wpc wpc) {
        int score = 0;

        int numOf1 = wpc.numDicesOfShade(1);
        int numOf2 = wpc.numDicesOfShade(2);

        int count = Math.min(numOf1, numOf2);

        return count*POCConstants.POC5_SCORE;
    }
}
