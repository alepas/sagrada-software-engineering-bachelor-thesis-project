package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

public class PublicObjectiveCard4  extends PublicObjectiveCard {

    public PublicObjectiveCard4(){
        this.id = "4";
    }

    @Override
    public int calculateScore(WPC wpc) {
        return 0;
    }
}