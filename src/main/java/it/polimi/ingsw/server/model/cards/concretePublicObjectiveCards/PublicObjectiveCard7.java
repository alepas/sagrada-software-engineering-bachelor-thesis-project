package it.polimi.ingsw.server.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.server.constants.POCConstants;
import it.polimi.ingsw.server.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.server.model.wpc.Wpc;

public class PublicObjectiveCard7  extends PublicObjectiveCard {

    /**
     * Constructor of POC 7.
     */
    public PublicObjectiveCard7(){
        this.id = POCConstants.POC7_ID;
        this.name = POCConstants.POC7_NAME;
        this.description = POCConstants.POC7_DESCRIPTION;
    }

    /**
     * Adds 2 points to the wpc's score for each couple of dices with number 5 and 6.
     *
     * @param wpc is the schema on which must be calculate the score
     * @return the score related to this poc card
     */
    @Override
    public int calculateScore(Wpc wpc) {
        int score = 0;

        int numOf5 = wpc.numDicesOfShade(5);
        int numOf6 = wpc.numDicesOfShade(6);

        int count = Math.min(numOf5, numOf6);

        return count*POCConstants.POC7_SCORE;
    }
}