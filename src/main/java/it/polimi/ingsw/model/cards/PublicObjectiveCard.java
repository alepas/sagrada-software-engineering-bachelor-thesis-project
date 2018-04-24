package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.WPC.WPC;

import java.util.ArrayList;

public abstract class PublicObjectiveCard {
    String id;

    //Deve restitituire un ArrayList di Stringhe contententi tutti gli ID delle PublicObjectiveCards
    //L'ordine non è importante
    public static ArrayList<String> getCardsIDs() {
        return null;
    }


    //Passandogli l'id deve restituire l'oggetto PublicObjectiveCard corrispondente all'id passato
    public static PublicObjectiveCard getCardByID(String id){
        return null;
    }

    public String getID(){
        return id;
    }

    public abstract int calculateScore(WPC wpc);
}
