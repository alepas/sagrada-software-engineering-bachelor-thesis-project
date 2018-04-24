package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

public class PublicObjectiveCard1 extends PublicObjectiveCard {

    public PublicObjectiveCard1(){
        this.id = "1";
    }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
