package server.model.cards.concretepublicobjectivecards;

import server.constants.POCConstants;
import server.constants.WpcConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.wpc.Wpc;

import java.util.ArrayList;

public class PublicObjectiveCard2 extends PublicObjectiveCard {

    /**
     * Constructor of POC 2.
     */
    public PublicObjectiveCard2() {
        this.id = POCConstants.POC2_ID;
        this.name = POCConstants.POC2_NAME;
        this.description = POCConstants.POC2_DESCRIPTION;
    }


    /**
     * Adds 5 points to the wpc's score for each column with all different colors.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {
        int score = 0;

        for (int column = 0; column < WpcConstants.COLS_NUMBER; column++){
            ArrayList<Dice> columnDices = wpc.getColDices(column);

            if (columnDices != null && columnDices.size() == WpcConstants.ROWS_NUMBER && allColorsAreDifferent(columnDices))
                score += POCConstants.POC2_SCORE;
        }

        return score;
    }

    /**
     * Checks if all dices in the same column have different colors or not.
     *
     * @param dices in the same column
     * @return true if the 4 dices have 4 different colors, false if the column is not complete (arrayList.size()<4 ) or if
     * there are at least two dices with the same color.
     */
    private boolean allColorsAreDifferent(ArrayList<Dice> dices){
        ArrayList<Color> extractedColors = new ArrayList<>();

        for (Dice dice : dices){
            if (extractedColors.contains(dice.getDiceColor())) return false;
            extractedColors.add(dice.getDiceColor());
        }

        return true;
    }
}
