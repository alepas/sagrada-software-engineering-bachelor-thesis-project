package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.WPC;

import java.util.ArrayList;

public class PublicObjectiveCard9  extends PublicObjectiveCard {
    //DIAGONALI COLORATE

    public PublicObjectiveCard9(){
        this.id = POCConstants.POC9_ID;
        this.name = POCConstants.POC9_NAME;
        this.description = POCConstants.POC9_DESCRIPTION;
    }

    @Override
    public int calculateScore(WPC wpc){
        ArrayList<ArrayList<Dice>> rows= new ArrayList<>();
        for (int row = 0; row < WpcConstants.ROWS_NUMBER; row++){

            rows.add(wpc.getRowDices(row));
            if (rows.get(row).size() != WpcConstants.COLS_NUMBER)
                return -1;

        }


        return diagSameColor(rows)*POCConstants.POC9_SCORE;
    }



    private int diagSameColor(ArrayList<ArrayList<Dice>> rows ){
        int tempPoints=0;
        boolean found;
        for (int row = 0; row < WpcConstants.ROWS_NUMBER; row++){
            for (int diceCol=0; diceCol<WpcConstants.COLS_NUMBER;diceCol++){
                found=false;
                Color diceColor=rows.get(row).get(diceCol).getDiceColor();
                if(row!=0) {
                    if (diceCol != 0) {
                        if (rows.get(row - 1).get(diceCol - 1).getDiceColor() == diceColor) {
                            tempPoints++;
                            found = true;
                        }
                    }
                    if (!found&&(diceCol != WpcConstants.COLS_NUMBER - 1)) {
                        if (rows.get(row - 1).get(diceCol + 1).getDiceColor() == diceColor) {
                            tempPoints++;
                            found = true;
                        }
                    }
                }
                if(!found&&(row!=WpcConstants.ROWS_NUMBER-1)){
                    if(diceCol!=0){
                        if(rows.get(row+1).get(diceCol-1).getDiceColor()==diceColor) {
                            tempPoints++;
                            found = true;
                        }
                    }
                    if (!found&&(diceCol != WpcConstants.COLS_NUMBER - 1)) {
                        if (rows.get(row + 1).get(diceCol + 1).getDiceColor() == diceColor) {
                            tempPoints++;
                        }
                    }
                }

            }
        }
        return tempPoints;
    }
}

