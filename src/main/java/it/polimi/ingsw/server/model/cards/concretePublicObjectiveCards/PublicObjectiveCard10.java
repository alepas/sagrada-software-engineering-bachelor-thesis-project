package it.polimi.ingsw.server.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.server.constants.POCConstants;
import it.polimi.ingsw.server.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.server.model.wpc.Wpc;
import it.polimi.ingsw.shared.constants.WpcConstants;

public class PublicObjectiveCard10  extends PublicObjectiveCard {
    /**
     * Constructor of POC 10.
     */
    public PublicObjectiveCard10(){
        this.id = POCConstants.POC10_ID;
        this.name = POCConstants.POC10_NAME;
        this.description = POCConstants.POC10_DESCRIPTION;
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
