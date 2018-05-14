package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;

import java.util.ArrayList;

public class PublicObjectiveCard3  extends PublicObjectiveCard {
    //SFUMATURE DIVERSE - RIGA

    public PublicObjectiveCard3(){ this.id = POCConstants.POC3_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        for (int row = 0; row < WpcConstants.ROWS_NUMBER; row++){
            ArrayList<Dice> rowDices = wpc.getRowDices(row);
            if (rowDices.size() == WpcConstants.COLS_NUMBER && allNumbersAreDifferent(rowDices)){
                score += POCConstants.POC3_SCORE;
            }
        }

        return score;
    }

    private boolean allNumbersAreDifferent(ArrayList<Dice> dices) {
        ArrayList<Integer> extractedNumbers = new ArrayList<>();

        for (Dice dice : dices){
            if (extractedNumbers.contains(dice.getDiceNumber())) return false;
            extractedNumbers.add(dice.getDiceNumber());
        }

        return true;
    }
}
