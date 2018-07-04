package server.model.cards;


import server.constants.POCConstants;
import server.model.cards.concretePublicObjectiveCards.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PocDB {
    private static PocDB instance;
    private HashMap<String, PublicObjectiveCard> cards;

    /**
     * @return an instance of this class
     */
    public static PocDB getInstance(){
        if (instance == null) instance = new PocDB();
        return instance;
    }


    /**
     * The public Objective card DB is composed by 10 different card which are loaded in an HashMap.
     * The HashMap's keys are the POCs' IDs
     */
    private PocDB(){
        cards=new HashMap<>();
        cards.put( POCConstants.POC1_ID, new PublicObjectiveCard1());
        cards.put( POCConstants.POC2_ID, new PublicObjectiveCard2());
        cards.put( POCConstants.POC3_ID, new PublicObjectiveCard3());
        cards.put( POCConstants.POC4_ID, new PublicObjectiveCard4());
        cards.put( POCConstants.POC5_ID, new PublicObjectiveCard5());
        cards.put( POCConstants.POC6_ID, new PublicObjectiveCard6());
        cards.put( POCConstants.POC7_ID, new PublicObjectiveCard7());
        cards.put( POCConstants.POC8_ID, new PublicObjectiveCard8());
        cards.put( POCConstants.POC9_ID, new PublicObjectiveCard9());
        cards.put( POCConstants.POC10_ID, new PublicObjectiveCard10());
    }


    /**
     * @return an arrayList formed by all the ids
     */
    public List<String> getCardsIDs() { return new ArrayList<>(cards.keySet()); }


    /**
     * @param id is the id of the chosen card
     * @return the object associated to that id
     */
    public PublicObjectiveCard getCardByID(String id){
        PublicObjectiveCard card = cards.get(id);
        if (card == null)
            return null;
        return card;
    }


}
