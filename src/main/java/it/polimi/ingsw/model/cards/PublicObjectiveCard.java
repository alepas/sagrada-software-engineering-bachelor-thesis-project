package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.cards.concretePublicObjectiveCards.*;

import java.util.ArrayList;

public abstract class PublicObjectiveCard {
    protected String id;
    protected String name;
    protected String description;

    public static ArrayList<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();

    public static void loadCards(){
        publicObjectiveCards.add(new PublicObjectiveCard1());
        publicObjectiveCards.add(new PublicObjectiveCard2());
        publicObjectiveCards.add(new PublicObjectiveCard3());
        publicObjectiveCards.add(new PublicObjectiveCard4());
        publicObjectiveCards.add(new PublicObjectiveCard5());
        publicObjectiveCards.add(new PublicObjectiveCard6());
        publicObjectiveCards.add(new PublicObjectiveCard7());
        publicObjectiveCards.add(new PublicObjectiveCard8());
        publicObjectiveCards.add(new PublicObjectiveCard9());
        publicObjectiveCards.add(new PublicObjectiveCard10());
    }

    public static ArrayList<String> getCardsIDs() {
        ArrayList<String> ids = new ArrayList<>();

        for (PublicObjectiveCard card : publicObjectiveCards){
            ids.add(card.getID());
        }

        return ids;
    }

    public static PublicObjectiveCard getCardByID(String id){
        for(PublicObjectiveCard card : publicObjectiveCards){
            if(card.getID().equals(id)) return card;
        }
        return null;
    }

    public String getID(){ return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public abstract int calculateScore(WPC wpc);
}
