package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard10  extends PublicObjectiveCard {
    //VARIETA' DI COLORE

    public PublicObjectiveCard10(){ this.id = POCConstants.POC10_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
