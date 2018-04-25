package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard7  extends PublicObjectiveCard {
    //SFUMATURE SCURE

    public PublicObjectiveCard7(){ this.id = POCConstants.POC7_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        int numOf5 = wpc.numDicesOfShade(5);
        int numOf6 = wpc.numDicesOfShade(6);

        int count = Math.min(numOf5, numOf6);

        return count*POCConstants.POC7_SCORE;
    }
}