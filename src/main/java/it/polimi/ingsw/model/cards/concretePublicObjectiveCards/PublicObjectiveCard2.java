package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

public class PublicObjectiveCard2 extends PublicObjectiveCard {

    public PublicObjectiveCard2(){
        this.id = "2";
    }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
