package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

public class PublicObjectiveCard10  extends PublicObjectiveCard {

    public PublicObjectiveCard10(){
        this.id = "10";
    }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}
