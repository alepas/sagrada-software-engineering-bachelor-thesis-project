package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.Wpc;

import java.util.ArrayList;

public class PublicObjectiveCard9  extends PublicObjectiveCard {

    /**
     * Constructor of POC 9.
     */
    public PublicObjectiveCard9(){
        this.id = POCConstants.POC9_ID;
        this.name = POCConstants.POC9_NAME;
        this.description = POCConstants.POC9_DESCRIPTION;
    }

    /**
     * Adds 1 points for each dice which is in a diagonal of the schema.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc){
        ArrayList<ArrayList<Dice>> rows= new ArrayList<>();

        for (int row = 0; row < WpcConstants.ROWS_NUMBER; row++)
            if (rows.get(row).size() != WpcConstants.COLS_NUMBER)
                return -1;

        return diagSameColor(rows)*POCConstants.POC9_SCORE;
    }


    /**
     *
     * @param rows arrayList of arrayList composed by all dices in the same row of the schema
     * @return the number of dices in diagonals
     */
    private int diagSameColor(ArrayList<ArrayList<Dice>> rows ){
        int tempPoints=0;
        boolean found;
        Dice tempDice;
        Dice secondTempDice;
        for (int row = 0; row < WpcConstants.ROWS_NUMBER; row++){
            for (int diceCol=0; diceCol<WpcConstants.COLS_NUMBER;diceCol++){
                found=false;
                tempDice=rows.get(row).get(diceCol);
                if (tempDice!=null){

                    Color diceColor=tempDice.getDiceColor();
                    if(row!=0) {
                        if (diceCol != 0) {
                            secondTempDice=rows.get(row - 1).get(diceCol - 1);
                            if (secondTempDice!=null&&secondTempDice.getDiceColor() == diceColor) {
                                tempPoints++;
                                found = true;
                            }
                        }
                        if (!found&&(diceCol < (WpcConstants.COLS_NUMBER-1))) {
                            secondTempDice=rows.get(row - 1).get(diceCol +1);
                            if (secondTempDice!=null&&secondTempDice.getDiceColor() == diceColor)  {
                                tempPoints++;
                                found = true;
                            }
                        }
                    }
                    if(!found&&(row<(WpcConstants.ROWS_NUMBER-1))){
                        if(diceCol!=0){
                            secondTempDice=rows.get(row + 1).get(diceCol - 1);
                            if (secondTempDice!=null&&secondTempDice.getDiceColor() == diceColor)  {
                                tempPoints++;
                                found = true;
                            }
                        }
                        if (!found&&(diceCol <(WpcConstants.COLS_NUMBER - 1))) {
                            secondTempDice=rows.get(row + 1).get(diceCol + 1);
                            if (secondTempDice!=null&&secondTempDice.getDiceColor() == diceColor)  {
                                tempPoints++;
                            }
                        }
                    }


                }

            }
        }
        return tempPoints;
    }
}

