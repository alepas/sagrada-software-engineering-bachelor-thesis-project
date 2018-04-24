package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard7  extends PublicObjectiveCard {

    public PublicObjectiveCard7(){ this.id = POCConstants.POC7_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}