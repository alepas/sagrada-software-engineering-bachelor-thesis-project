package server.model.cards.concretepublicobjectivecards;

import server.constants.POCConstants;
import server.constants.WpcConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.wpc.Wpc;

public class PublicObjectiveCard8  extends PublicObjectiveCard {

    /**
     * Constructor of POC 8.
     */
    public PublicObjectiveCard8(){
        this.id = POCConstants.POC8_ID;
        this.name = POCConstants.POC8_NAME;
        this.description = POCConstants.POC8_DESCRIPTION;
    }

    /**
     * Adds 5 points to the wpc's score for each group of 6 dices with all different numbers.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {

        int max = WpcConstants.COLS_NUMBER*WpcConstants.ROWS_NUMBER;

        for (int i = 1; i<= 6; i++){
            max = Math.min(max, wpc.numDicesOfShade(i));
        }

        return max*POCConstants.POC8_SCORE;
    }
}
