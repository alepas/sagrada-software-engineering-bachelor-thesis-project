package it.polimi.ingsw.model.cards;

import java.util.ArrayList;

public abstract class ToolCard {
    private String id;

    //Deve restitituire un ArrayList di Stringhe contententi tutti gli ID delle ToolCards
    //L'ordine non è importante
    public static ArrayList<String> getCardsIDs() {
        return null;
    }


    //Passandogli l'id deve restituire l'oggetto toolcard corrispondente all'id passato
    public static ToolCard getCardByID(String id){
        return null;
    }
}
