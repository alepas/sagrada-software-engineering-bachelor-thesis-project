package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

public class PublicObjectiveCard5  extends PublicObjectiveCard {

    public PublicObjectiveCard5(){
        this.id = "5";
    }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
