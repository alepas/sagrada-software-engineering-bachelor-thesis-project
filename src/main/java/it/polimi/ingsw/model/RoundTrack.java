package it.polimi.ingsw.model;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import it.polimi.ingsw.model.Dice;

public class RoundTrack {
    private int currentRound;
    private Dice[] dicesNotUsed;

    //array di arraylist in questo modo posso aggiungere tutti i dadi che voglio in modo dinamico
    private void RoundTrack( ){
        currentRound = 0;
        ArrayList<Dice> dicesNotUsed[] = new ArrayList[10];
        for(int i = 0; i< dicesNotUsed.length; i++)
            dicesNotUsed[i] = new ArrayList<Dice>();
    }

    private void nextRound( ){
        currentRound = currentRound ++;
    }

    public void addDice( Dice dice){
            int i = currentRound --;
            //dicesNotUsed[i].add(dice);
    }
/*
    public void swapDice (Dice addedDice, Dice removedDice, int round){
        int col = round--;
        int row = 0;
        while( row <= 2) {
            if (!dicesNotUsed[row][col].equals(removedDice))
                row++;
            else
                dicesNotUsed[row][col] = addedDice;
        }
    }*/

    //private void removeDice --> è davvero necessario ??
    //public void getRoundDices ( int round){}
    //public void getAllDices ( ){}

}
