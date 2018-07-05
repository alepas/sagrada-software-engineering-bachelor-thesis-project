package server.model.cards.concretepublicobjectivecards;

import server.constants.POCConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.wpc.Wpc;

public class PublicObjectiveCard5  extends PublicObjectiveCard {

    /**
     * Constructor of POC 5.
     */
    public PublicObjectiveCard5(){
        this.id = POCConstants.POC5_ID;
        this.name = POCConstants.POC5_NAME;
        this.description = POCConstants.POC5_DESCRIPTION;
    }

    /**
     * Adds 2 points to the wpc's score for each couple of dices with number 1 and 2.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {

        int numOf1 = wpc.numDicesOfShade(1);
        int numOf2 = wpc.numDicesOfShade(2);

        int count = Math.min(numOf1, numOf2);

        return count*POCConstants.POC5_SCORE;
    }
}
