package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.dicebag.DiceBag;
import it.polimi.ingsw.model.game.RoundTrack;

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

    public String getID(){
        return id;
    }

    public abstract void use(WPC wpc, DiceBag diceBag, ArrayList<Dice> extractedDices, RoundTrack roundTrack);
}
