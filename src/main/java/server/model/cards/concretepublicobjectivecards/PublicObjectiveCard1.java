package server.model.cards.concretepublicobjectivecards;

import server.constants.POCConstants;
import server.constants.WpcConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.wpc.Wpc;
import shared.constants.PocConstants;

import java.util.ArrayList;

public class PublicObjectiveCard1 extends PublicObjectiveCard {

    /**
     * Constructor of POC 1.
     */
    public PublicObjectiveCard1() {
        this.id = POCConstants.POC1_ID;
        this.name = PocConstants.POC1_NAME;
        this.description = PocConstants.POC1_DESCRIPTION;
    }

    /**
     * Adds 6 points to the wpc's score for each row with all different colors.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {
        int score = 0;

        for (int row = 0; row < WpcConstants.ROWS_NUMBER; row++) {
            ArrayList<Dice> rowDices = wpc.getRowDices(row);
            if (rowDices != null && rowDices.size() == WpcConstants.COLS_NUMBER && allColorsAreDifferent(rowDices))
                score += POCConstants.POC1_SCORE;
        }

        return score;
    }


    /**
     * Checks if all dices in the same row have different colors or not.
     *
     * @param dices in the same row
     * @return true if the 5 dices have 5 different colors, false if the row is not complete (arrayList.size()<5 ) or if
     * there are at least two dices with the same color.
     */
    private boolean allColorsAreDifferent(ArrayList<Dice> dices) {
        ArrayList<Color> extractedColors = new ArrayList<>();

        for (Dice dice : dices) {
            if (extractedColors.contains(dice.getDiceColor())) return false;
            extractedColors.add(dice.getDiceColor());
        }

        return true;
    }
}
