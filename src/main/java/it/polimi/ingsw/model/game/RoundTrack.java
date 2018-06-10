package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.DiceAndPosition;

import java.io.Serializable;
import java.util.ArrayList;

import static it.polimi.ingsw.model.constants.RoundTrackConstants.HYPOTHETICAL_MAX_DICES_PER_ROUND;
import static it.polimi.ingsw.model.constants.RoundTrackConstants.NUM_OF_ROUND;

public class RoundTrack implements Serializable {
    private int currentRound;
    private Dice[][] dicesNotUsed;

    //array di arraylist in questo modo posso aggiungere tutti i dadi che voglio in modo dinamico
    public RoundTrack(){
        currentRound = 0;
        dicesNotUsed = new Dice[NUM_OF_ROUND][NUM_OF_ROUND];
        for (int row = 0;  row< NUM_OF_ROUND; row++ ) {

            for (int column = 0; column < NUM_OF_ROUND; column++)
                dicesNotUsed[row][column] = null;
        }
    }

    public int getCurrentRound() {
        return currentRound;
    }


    private Boolean isThereADice(int row, int column){
        return dicesNotUsed[row][column]!= null;
    }

    //TODO: Ricerca per id
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
        currentRound++;
    }

    void addDice(Dice dice){
            int column = currentRound - 1;
            int row = 0;
            
            do {
                row++;
            }while(isThereADice(row,column) && row<HYPOTHETICAL_MAX_DICES_PER_ROUND);
            
            dicesNotUsed[row][column] = dice;
                
    }

    public Position getPositionFromDice(Dice roundTrackDice) {
        for (int row = 0; row < NUM_OF_ROUND; row++) {

            for (int column = 0; column < HYPOTHETICAL_MAX_DICES_PER_ROUND; column++)
                if (isThereADice(row, column))
                    if (getDice(row, column).equals(roundTrackDice)) {
                        return new Position(row,column);
                    }
        }
        return null;
    }

    Dice swapDice(Dice addedDice, Dice roundTrackDice, int round){
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

    public Dice swapDice (Dice addedDice, Position position){
        Dice removedDice = null;
        removedDice = dicesNotUsed[position.getRow()][position.getColumn()];
        dicesNotUsed[position.getRow()][position.getColumn()] = addedDice;
        return removedDice;

    }

    public DiceAndPosition getDiceAndPosition(int diceId){
        for (int row = 0; row < NUM_OF_ROUND; row++) {

            for (int column = 0; column < HYPOTHETICAL_MAX_DICES_PER_ROUND; column++)
                if (isThereADice(row, column))
                    if (getDice(row, column).getId()==diceId) {
                        return new DiceAndPosition(getDice(row,column),new Position(row,column));
                    }
        }
        return null;
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

    public ClientRoundTrack getClientRoundTrack(){
        ClientDice[][] roundTrackTable=new ClientDice[NUM_OF_ROUND][HYPOTHETICAL_MAX_DICES_PER_ROUND];
        for (int i=0; i<NUM_OF_ROUND; i++){
            for (int j=0;j<HYPOTHETICAL_MAX_DICES_PER_ROUND;j++){
                if(dicesNotUsed[i][j]!= null)
                    roundTrackTable[i][j] = dicesNotUsed[i][j].getClientDice();
                else
                    roundTrackTable[i][j] = null;
            }
        }
        return new ClientRoundTrack(currentRound, roundTrackTable);
    }
}
