package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard1 extends PublicObjectiveCard {
    //COLORI DIVERSI - RIGA

    public PublicObjectiveCard1(){ this.id = POCConstants.POC1_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
