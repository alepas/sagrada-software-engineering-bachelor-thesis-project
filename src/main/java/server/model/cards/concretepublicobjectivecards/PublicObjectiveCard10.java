package server.model.cards.concretepublicobjectivecards;

import server.constants.POCConstants;
import server.constants.WpcConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.dicebag.Color;
import server.model.wpc.Wpc;
import shared.constants.PocConstants;

public class PublicObjectiveCard10  extends PublicObjectiveCard {
    /**
     * Constructor of POC 10.
     */
    public PublicObjectiveCard10(){
        this.id = POCConstants.POC10_ID;
        this.name = PocConstants.POC10_NAME;
        this.description = PocConstants.POC10_DESCRIPTION;
    }

    /**
     * Adds 4 points to the wpc's score for each group of 5 dices with all different colors.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {

        int max = WpcConstants.COLS_NUMBER*WpcConstants.ROWS_NUMBER;

        for (Color color : Color.values()){
            max = Math.min(max, wpc.numDicesOfColor(color));
        }

        return max*POCConstants.POC10_SCORE;
    }
}
