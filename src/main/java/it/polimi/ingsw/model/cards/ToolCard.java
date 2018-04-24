package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public abstract class ToolCard {
    private String id;
    private Boolean used = false;

    //Deve restitituire un ArrayList di Stringhe contententi tutti gli ID delle ToolCards
    //L'ordine non è importante
    public static ArrayList<String> getCardsIDs() {
        return null;
    }

    //Passandogli l'id deve restituire l'oggetto toolcard corrispondente all'id passato
    public static ToolCard getCardByID(String id){
        return null;
    }

    public String getID(){ return id; }

    public Boolean getUsed() { return used; }

    public abstract void use(PlayerInGame player);


}
