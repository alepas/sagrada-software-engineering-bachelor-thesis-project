package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

public class PublicObjectiveCard3  extends PublicObjectiveCard {

    public PublicObjectiveCard3(){
        this.id = "3";
    }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
