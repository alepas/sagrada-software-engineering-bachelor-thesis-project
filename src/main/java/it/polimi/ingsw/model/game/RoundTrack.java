package it.polimi.ingsw.model.game;
import it.polimi.ingsw.model.dicebag.Dice;

import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.model.constants.RoundTrackConstants.HYPOTHETICAL_MAX_DICES_PER_ROUND;
import static it.polimi.ingsw.model.constants.RoundTrackConstants.NUM_OF_ROUND;

public class RoundTrack implements Serializable {
    private int currentRound;
    private Dice[][] dicesNotUsed;

    //array di arraylist in questo modo posso aggiungere tutti i dadi che voglio in modo dinamico
    public RoundTrack(){
        currentRound = 0;
        dicesNotUsed = new Dice[NUM_OF_ROUND][HYPOTHETICAL_MAX_DICES_PER_ROUND];
        for (int row = 0;  row< NUM_OF_ROUND; row++ ) {

            for (int column = 0; column < HYPOTHETICAL_MAX_DICES_PER_ROUND; column++)
                dicesNotUsed[row][column] = null;
        }
    }

    public int getCurrentRound() {
        return currentRound;
    }


    private Boolean isThereADice(int row, int column){
        return dicesNotUsed[row][column]!= null;
    }

    private Dice getDice(int row, int column){
        return dicesNotUsed[row][column];    
    }
    
    
    public ArrayList<Dice> getDicesNotUsed() {
        //restituisce tutti i dadi presenti sul Round Track
        ArrayList<Dice> allRoundTrackDices = new ArrayList<>();

        for (int row = 0;  row< NUM_OF_ROUND; row++ ) {

            for (int column = 0; column < HYPOTHETICAL_MAX_DICES_PER_ROUND; column++)
                if (isThereADice(row, column))
                allRoundTrackDices.add(getDice(row, column));
        }
        return allRoundTrackDices;
    }

    void nextRound( ){
        currentRound = currentRound ++;
    }

    public void addDice( Dice dice){
            int column = currentRound - 1;
            int row = 0;
            
            do {
                row++;
            }while(isThereADice(row,column) && row<HYPOTHETICAL_MAX_DICES_PER_ROUND);
            
            dicesNotUsed[row][column] = dice;
                
    }


    
    public Dice swapDice (Dice addedDice, Dice roundTrackDice, int round){
        Dice removedDice = null;
        int column = round - 1;
        
        for (int row = 0; row < HYPOTHETICAL_MAX_DICES_PER_ROUND; row++) {
            if (getDice(row, column).equals(roundTrackDice)) {
                removedDice = dicesNotUsed[row][column];
                dicesNotUsed[row][column] = addedDice;
            }
        }
        
        return removedDice;
    }

    
    public ArrayList<Dice> getRoundDices ( int round) {
        ArrayList<Dice> roundNotUsedDices = new ArrayList<>();
        int column = round - 1;

        for (int row = 0; row < HYPOTHETICAL_MAX_DICES_PER_ROUND; row++){
            if (isThereADice(row, column))
                roundNotUsedDices.add(dicesNotUsed[row][column]);
        }
        
        return roundNotUsedDices;
    }
}
