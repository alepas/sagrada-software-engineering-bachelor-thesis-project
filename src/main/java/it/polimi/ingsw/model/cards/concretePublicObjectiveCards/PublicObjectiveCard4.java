package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.Wpc;

import java.util.ArrayList;

public class PublicObjectiveCard4  extends PublicObjectiveCard {

    /**
     * Constructor of POC 4.
     */
    public PublicObjectiveCard4(){
        this.id = POCConstants.POC4_ID;
        this.name = POCConstants.POC4_NAME;
        this.description = POCConstants.POC4_DESCRIPTION;
    }

    /**
     * Adds 4 points to the wpc's score for each column with all different numbers.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {
        int score = 0;

        for (int col = 0; col < WpcConstants.COLS_NUMBER; col++){
            ArrayList<Dice> colDices = wpc.getColDices(col);
            if (colDices.size() == WpcConstants.ROWS_NUMBER && allNumbersAreDifferent(colDices)){
                score += POCConstants.POC4_SCORE;
            }
        }

        return score;
    }

    /**
     * Checks if all dices in the same column have different numbers or not.
     *
     * @param dices in the same column
     * @return true if the 4 dices have 4 different numbers, false if the column is not complete (arrayList.size()<4 ) or if
     * there are at least two dices with the same number.
     */
    private boolean allNumbersAreDifferent(ArrayList<Dice> dices) {
        ArrayList<Integer> extractedNumbers = new ArrayList<>();

        for (Dice dice : dices){
            if (extractedNumbers.contains(dice.getDiceNumber())) return false;
            extractedNumbers.add(dice.getDiceNumber());
        }

        return true;
    }
}