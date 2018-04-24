package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard3  extends PublicObjectiveCard {
    //SFUMATURE DIVERSE - RIGA

    public PublicObjectiveCard3(){ this.id = POCConstants.POC3_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
