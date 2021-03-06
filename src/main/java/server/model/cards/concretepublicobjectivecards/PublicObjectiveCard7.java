package server.model.cards.concretepublicobjectivecards;

import server.constants.POCConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.wpc.Wpc;
import shared.constants.PocConstants;

public class PublicObjectiveCard7  extends PublicObjectiveCard {

    /**
     * Constructor of POC 7.
     */
    public PublicObjectiveCard7(){
        this.id = POCConstants.POC7_ID;
        this.name = PocConstants.POC7_NAME;
        this.description = PocConstants.POC7_DESCRIPTION;
    }

    /**
     * Adds 2 points to the wpc's score for each couple of dices with number 5 and 6.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {

        int numOf5 = wpc.numDicesOfShade(5);
        int numOf6 = wpc.numDicesOfShade(6);

        int count = Math.min(numOf5, numOf6);

        return count*POCConstants.POC7_SCORE;
    }
}