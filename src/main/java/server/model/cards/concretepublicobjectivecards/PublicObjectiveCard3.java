package server.model.cards.concretepublicobjectivecards;

import server.constants.POCConstants;
import server.constants.WpcConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.dicebag.Dice;
import server.model.wpc.Wpc;
import shared.constants.PocConstants;

import java.util.ArrayList;

public class PublicObjectiveCard3  extends PublicObjectiveCard {

    /**
     * Constructor of POC 3.
     */
    public PublicObjectiveCard3(){
        this.id = POCConstants.POC3_ID;
        this.name = PocConstants.POC3_NAME;
        this.description = PocConstants.POC3_DESCRIPTION;
    }

    /**
     * Adds 5 points to the wpc's score for each row with all different numbers.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {
        int score = 0;

        for (int row = 0; row < WpcConstants.ROWS_NUMBER; row++){
            ArrayList<Dice> rowDices = wpc.getRowDices(row);

            if (rowDices != null && rowDices.size() == WpcConstants.COLS_NUMBER && allNumbersAreDifferent(rowDices))
                score += POCConstants.POC3_SCORE;
        }

        return score;
    }

    /**
     * Checks if all dices in the same row have different numbers or not.
     *
     * @param dices in the same row
     * @return true if the 5 dices have 5 different numbers, false if the row is not complete (arrayList.size()<5 ) or if
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
