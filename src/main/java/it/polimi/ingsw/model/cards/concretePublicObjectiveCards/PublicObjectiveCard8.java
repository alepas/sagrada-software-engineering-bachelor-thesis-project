package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.wpc.Wpc;

public class PublicObjectiveCard8  extends PublicObjectiveCard {
    //SFUMATURE DIVERSE

    public PublicObjectiveCard8(){
        this.id = POCConstants.POC8_ID;
        this.name = POCConstants.POC8_NAME;
        this.description = POCConstants.POC8_DESCRIPTION;
    }

    @Override
    public int calculateScore(Wpc wpc) {
        int score = 0;

        int max = WpcConstants.COLS_NUMBER*WpcConstants.ROWS_NUMBER;

        for (int i = 1; i<= 6; i++){
            max = Math.min(max, wpc.numDicesOfShade(i));
        }

        return max*POCConstants.POC8_SCORE;
    }
}
