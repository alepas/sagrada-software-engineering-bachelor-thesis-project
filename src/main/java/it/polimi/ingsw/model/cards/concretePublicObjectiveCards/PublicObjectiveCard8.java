package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard8  extends PublicObjectiveCard {
    //SFUMATURE DIVERSE

    public PublicObjectiveCard8(){ this.id = POCConstants.POC8_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
