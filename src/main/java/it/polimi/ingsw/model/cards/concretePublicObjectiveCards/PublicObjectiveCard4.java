package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard4  extends PublicObjectiveCard {
    //SFUMATURE DIVERSE - COLONNA

    public PublicObjectiveCard4(){ this.id = POCConstants.POC4_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}