package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard5  extends PublicObjectiveCard {

    public PublicObjectiveCard5(){ this.id = POCConstants.POC5_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
