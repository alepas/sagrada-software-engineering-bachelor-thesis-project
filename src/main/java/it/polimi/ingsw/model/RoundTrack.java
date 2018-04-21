package it.polimi.ingsw.model;

public class RoundTrack {
    private int currentRound;
    private Dice[][] dicesNotUsed;

    private void RoundTrack( ){
        currentRound = 0;
        for(int rows = 0; rows < 3; rows++){
            for( int columns = 0; columns < 10; columns++ )
                dicesNotUsed[rows][columns]= null;
        }
    }

    private void nextRound( ){
        currentRound = currentRound ++;
    }

    public void addDice( Dice dice){
        int row = 0;
        int col = currentRound --;
        if( dicesNotUsed[row][col] == null)
            dicesNotUsed[row][col] = dice;
        else{
            row ++;
            while ( row <= 2) {
                if (dicesNotUsed[row][col] != null)
                    row++;
                else
                    dicesNotUsed[row][col] = dice;
            }
        }
    }

    public void swapDice (Dice addedDice, Dice removedDice, int round){
        int col = round--;
        int row = 0;
        while( row <= 2) {
            if (!dicesNotUsed[row][col].equals(removedDice))
                row++;
            else
                dicesNotUsed[row][col] = addedDice;
        }
    }

    //private void removeDice --> è davvero necessario ??
    //public void getRoundDices ( int round){}
    //public void getAllDices ( ){}

}
