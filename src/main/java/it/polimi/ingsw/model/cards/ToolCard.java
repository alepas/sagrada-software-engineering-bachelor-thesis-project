package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.concreteToolCards.*;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public abstract class ToolCard {
    protected String id;
    private Boolean used = false;

    public static ArrayList<ToolCard> toolCards = new ArrayList<>();

    public static void loadCards(){
        toolCards.add(new ToolCard1());
        toolCards.add(new ToolCard2());
        toolCards.add(new ToolCard3());
        toolCards.add(new ToolCard4());
        toolCards.add(new ToolCard5());
        toolCards.add(new ToolCard6());
        toolCards.add(new ToolCard7());
        toolCards.add(new ToolCard8());
        toolCards.add(new ToolCard9());
        toolCards.add(new ToolCard10());
        toolCards.add(new ToolCard11());
        toolCards.add(new ToolCard12());
    }

    public static ArrayList<String> getCardsIDs() {
        ArrayList<String> ids = new ArrayList<>();

        for (ToolCard card : toolCards){
            ids.add(card.getID());
        }

        return ids;
    }

    public static ToolCard getCardByID(String id){
        for(ToolCard card : toolCards){
            if(card.getID().equals(id)) return card;
        }
        return null;
    }

    public String getID(){ return id; }

    public Boolean isUsed() { return used; }

    public abstract void use(PlayerInGame player);

}
