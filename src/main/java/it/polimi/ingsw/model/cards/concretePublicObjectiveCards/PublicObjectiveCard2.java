package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard2 extends PublicObjectiveCard {
    //COLORI DIVERSI - COLONNA

    public PublicObjectiveCard2() { this.id = POCConstants.POC2_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
