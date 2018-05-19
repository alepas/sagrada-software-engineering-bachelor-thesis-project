package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard6  extends PublicObjectiveCard {
    //SFUMATURE MEDIE

    public PublicObjectiveCard6() {
        this.id = POCConstants.POC6_ID;
        this.name = POCConstants.POC6_NAME;
        this.description = POCConstants.POC6_DESCRIPTION;
    }


    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        int numOf1 = wpc.numDicesOfShade(3);
        int numOf2 = wpc.numDicesOfShade(4);

        int count = Math.min(numOf1, numOf2);

        return count * POCConstants.POC6_SCORE;
    }
}
