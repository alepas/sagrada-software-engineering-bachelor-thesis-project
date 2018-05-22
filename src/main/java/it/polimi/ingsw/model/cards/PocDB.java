package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.model.cards.concretePublicObjectiveCards.*;
import it.polimi.ingsw.model.constants.POCConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class PocDB {
    private static PocDB instance;
    private HashMap<String, PublicObjectiveCard> cards;

    public static PocDB getInstance(){
        if (instance==null) {
            instance = new PocDB();
        }
        return instance;
    }



    private PocDB(){
        cards=new HashMap<>();
        cards.put( POCConstants.POC1_ID,new PublicObjectiveCard1());
        cards.put( POCConstants.POC2_ID,new PublicObjectiveCard2());
        cards.put( POCConstants.POC3_ID,new PublicObjectiveCard3());
        cards.put( POCConstants.POC4_ID,new PublicObjectiveCard4());
        cards.put( POCConstants.POC5_ID,new PublicObjectiveCard5());
        cards.put( POCConstants.POC6_ID,new PublicObjectiveCard6());
        cards.put( POCConstants.POC7_ID,new PublicObjectiveCard7());
        cards.put( POCConstants.POC8_ID,new PublicObjectiveCard8());
        cards.put( POCConstants.POC9_ID,new PublicObjectiveCard9());
        cards.put( POCConstants.POC10_ID,new PublicObjectiveCard10());
    }

    public ArrayList<String> getCardsIDs() {
        ArrayList<String> ids = new ArrayList<>();
        ids.addAll(cards.keySet());
        return ids;
    }

    public PublicObjectiveCard getCardByID(String id){
        PublicObjectiveCard temp=cards.get(id);
        if (temp==null)
            return null;
        return temp;
    }


}
