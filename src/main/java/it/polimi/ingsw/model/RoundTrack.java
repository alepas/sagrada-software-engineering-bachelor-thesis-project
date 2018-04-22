package it.polimi.ingsw.model;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.game.AbstractGame;

public class RoundTrack {
    private int currentRound;
    private ArrayList<Dice>[] dicesNotUsed;

    //array di arraylist in questo modo posso aggiungere tutti i dadi che voglio in modo dinamico
    private void RoundTrack( ){
        currentRound = 0;
        dicesNotUsed = new ArrayList[10];
        for(int i = 0; i< dicesNotUsed.length; i++)
            dicesNotUsed[i] = new ArrayList<Dice>();
            dicesNotUsed[0].add(null);
    }

    private void nextRound( ){
        currentRound = currentRound ++;
    }

    public void addDice( Dice dice){
            int i = currentRound --;
            if(dicesNotUsed[i].get(0) == null)
                dicesNotUsed[i].add(0, dice);
            else
                dicesNotUsed[i].add(dice);
    }

    //genero e ritorno il dado che intendo rimuovere, con la set posiziono il dado che voglio aggiungere
    //potrei anchescrivere il metodo removeDice ma non ne trovo davvero l'utilità in quanto l'operazione risulta c
    //comunque rapida anche in questo modo
    public Dice swapDice (Dice addedDice, int indexRemovedDice, int round){
        Dice removedDice;
        int index = round --;
        Colour color = dicesNotUsed[index].get(indexRemovedDice).getDiceColour();
        int num = dicesNotUsed[index].get(indexRemovedDice).getDiceNumber();
        removedDice = new Dice(color, num);
        dicesNotUsed[index].set(indexRemovedDice, addedDice);
        return removedDice;
    }

    public void getRoundDices ( int round){
        int index = round--;
        dicesNotUsed[index].toArray();
    }

    public void getAllDices ( ){
    }

    //chiamato alla fine del gioco per frimuovere tutti i dadi ? forse useless
    private void removeAll ( ){
        for( int i = 0; i < dicesNotUsed.length; i++)
            for( int j = 0; j < dicesNotUsed[i].size(); j++)
                dicesNotUsed[i].remove(j);
    }

}
