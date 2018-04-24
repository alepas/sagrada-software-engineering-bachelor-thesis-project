package it.polimi.ingsw.model;
import java.util.*;

public class RoundTrack {
    private int currentRound;
    private ArrayList[] dicesNotUsed;

    //array di arraylist in questo modo posso aggiungere tutti i dadi che voglio in modo dinamico
    public RoundTrack(){
        currentRound = 0;
        dicesNotUsed = new ArrayList[10];
        for(int i = 0; i< dicesNotUsed.length; i++) {
            dicesNotUsed[i] = new ArrayList<Dice>();
        }
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public ArrayList[] getDicesNotUsed() {
        return dicesNotUsed;
    }

    private void nextRound( ){
        currentRound = currentRound ++;
    }

    public void addDice( Dice dice){
            int i = currentRound - 1;
            dicesNotUsed[i].add(dice);
    }

    //genero e ritorno il dado che intendo rimuovere, con la set posiziono il dado che voglio aggiungere
    //potrei anchescrivere il metodo removeDice ma non ne trovo davvero l'utilità in quanto l'operazione risulta c
    //comunque rapida anche in questo modo
    public Dice swapDice (Dice addedDice, int indexRemovedDice, int round){
        Dice removedDice;
        int index = round - 1;
        removedDice = (Dice) dicesNotUsed[index].get(indexRemovedDice); //sto dicendo io che sta prendendo un dado
        dicesNotUsed[index].set(indexRemovedDice, addedDice);
        return removedDice;
    }

    public void getRoundDices ( int round){
        int index = round - 1;
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
