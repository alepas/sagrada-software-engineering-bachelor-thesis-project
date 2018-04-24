package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard6  extends PublicObjectiveCard {
    //SFUMATURE MEDIE

    public PublicObjectiveCard6(){ this.id = POCConstants.POC6_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
