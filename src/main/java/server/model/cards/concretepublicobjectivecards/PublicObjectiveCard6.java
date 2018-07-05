package server.model.cards.concretepublicobjectivecards;

import server.constants.POCConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.wpc.Wpc;
import shared.constants.PocConstants;

public class PublicObjectiveCard6  extends PublicObjectiveCard {

    /**
     * Constructor of POC 6.
     */
    public PublicObjectiveCard6() {
        this.id = POCConstants.POC6_ID;
        this.name = PocConstants.POC6_NAME;
        this.description = PocConstants.POC6_DESCRIPTION;
    }

    /**
     * Adds 2 points to the wpc's score for each couple of dices with number 3 and 4.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {

        int numOf1 = wpc.numDicesOfShade(3);
        int numOf2 = wpc.numDicesOfShade(4);

        int count = Math.min(numOf1, numOf2);

        return count * POCConstants.POC6_SCORE;
    }
}
