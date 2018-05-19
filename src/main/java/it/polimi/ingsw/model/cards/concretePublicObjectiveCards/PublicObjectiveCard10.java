package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;

public class PublicObjectiveCard10  extends PublicObjectiveCard {
    //VARIETA' DI COLORE

    public PublicObjectiveCard10(){
        this.id = POCConstants.POC10_ID;
        this.name = POCConstants.POC10_NAME;
        this.description = POCConstants.POC10_DESCRIPTION;
    }

    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        int max = WpcConstants.COLS_NUMBER*WpcConstants.ROWS_NUMBER;

        for (Color color : Color.values()){
            max = Math.min(max, wpc.numDicesOfColor(color));
        }

        return max*POCConstants.POC10_SCORE;
    }
}
