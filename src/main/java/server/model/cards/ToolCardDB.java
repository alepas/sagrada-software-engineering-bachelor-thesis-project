package server.model.cards;

import server.constants.ToolCardConstants;
import server.model.cards.concretetoolcards.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolCardDB {
    private static ToolCardDB instance;
    private HashMap<String, ToolCard> toolCards;

    /**
     * @return an instance of this class
     */
    public static ToolCardDB getInstance(){
        if (instance==null)
            instance = new ToolCardDB();
        return instance;
    }


    /**
     * constructor of the DB: adds to the hash map all tool cards
     */
    private ToolCardDB(){
        toolCards = new HashMap<>();
        toolCards.put( ToolCardConstants.TOOLCARD1_ID,new ToolCard1());
        toolCards.put( ToolCardConstants.TOOLCARD2_ID,new ToolCard2());
        toolCards.put( ToolCardConstants.TOOLCARD3_ID,new ToolCard3());
        toolCards.put( ToolCardConstants.TOOLCARD4_ID,new ToolCard4());
        toolCards.put( ToolCardConstants.TOOLCARD5_ID,new ToolCard5());
        toolCards.put( ToolCardConstants.TOOLCARD6_ID,new ToolCard6());
        toolCards.put( ToolCardConstants.TOOLCARD7_ID,new ToolCard7());
        toolCards.put( ToolCardConstants.TOOLCARD8_ID,new ToolCard8());
        toolCards.put( ToolCardConstants.TOOLCARD9_ID,new ToolCard9());
        toolCards.put( ToolCardConstants.TOOLCARD10_ID,new ToolCard10());
        toolCards.put( ToolCardConstants.TOOLCARD11_ID,new ToolCard11());
        toolCards.put( ToolCardConstants.TOOLCARD12_ID,new ToolCard12());
    }

    /**
     * The tool card DB is composed by 12 different card which are loaded in an HashMap.
     * The HashMap's keys are the tool cards' IDs
     */
    public List<String> getCardsIDs() {
        return new ArrayList<>(toolCards.keySet());
    }

    /**
     * @param id is the id of the chosen card
     * @return the object associated to that id
     */
    public ToolCard getCardByID(String id){
        ToolCard temp=toolCards.get(id);
        if (temp==null)
            return null;
        return temp.getToolCardCopy();
    }
}
