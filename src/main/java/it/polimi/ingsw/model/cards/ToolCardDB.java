package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.concreteToolCards.*;
import it.polimi.ingsw.model.constants.ToolCardConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class ToolCardDB {
    private static ToolCardDB instance;
    private HashMap<String, ToolCard> toolCards;

    public static ToolCardDB getInstance(){
        if (instance==null) {
            instance = new ToolCardDB();
        }
        return instance;
    }



    private ToolCardDB(){
        toolCards = new HashMap<>();
        toolCards.put( ToolCardConstants.TOOLCARD1_ID,new ToolCard1());
        toolCards.put( ToolCardConstants.TOOLCARD2_ID,new ToolCard2());
        toolCards.put( ToolCardConstants.TOOLCARD3_ID,new ToolCard3());
/*        toolCards.put( ToolCardConstants.TOOLCARD4_ID,new ToolCard4());
        toolCards.put( ToolCardConstants.TOOLCARD5_ID,new ToolCard5());
        toolCards.put( ToolCardConstants.TOOLCARD6_ID,new ToolCard6());
        toolCards.put( ToolCardConstants.TOOLCARD7_ID,new ToolCard7());*/
        toolCards.put( ToolCardConstants.TOOLCARD8_ID,new ToolCard8());
        toolCards.put( ToolCardConstants.TOOLCARD9_ID,new ToolCard9());
        toolCards.put( ToolCardConstants.TOOLCARD10_ID,new ToolCard10());
        toolCards.put( ToolCardConstants.TOOLCARD11_ID,new ToolCard11());
        toolCards.put( ToolCardConstants.TOOLCARD12_ID,new ToolCard12());
    }

    public ArrayList<String> getCardsIDs() {
        ArrayList<String> ids = new ArrayList<>();
        ids.addAll(toolCards.keySet());
        return ids;
    }

    public ToolCard getCardByID(String id){
        ToolCard temp=toolCards.get(id);
        if (temp==null)
            return null;
        return temp.getToolCardCopy();
    }


}
