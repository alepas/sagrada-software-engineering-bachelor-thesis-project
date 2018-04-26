package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

public class PublicObjectiveCard9  extends PublicObjectiveCard {
    //DIAGONALI COLORATE

    public PublicObjectiveCard9(){ this.id = POCConstants.POC9_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}

