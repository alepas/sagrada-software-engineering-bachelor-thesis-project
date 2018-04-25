package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;

public class PublicObjectiveCard8  extends PublicObjectiveCard {
    //SFUMATURE DIVERSE

    public PublicObjectiveCard8(){ this.id = POCConstants.POC8_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        int max = WpcConstants.COLS_NUMBER*WpcConstants.ROWS_NUMBER;

        for (int i = 1; i<= 6; i++){
            max = Math.min(max, wpc.numDicesOfShade(i));
        }

        return max*POCConstants.POC10_SCORE;
    }
}
