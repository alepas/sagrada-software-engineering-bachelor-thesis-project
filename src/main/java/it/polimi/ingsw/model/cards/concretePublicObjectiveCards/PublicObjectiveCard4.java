package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Dice;

import java.util.ArrayList;

public class PublicObjectiveCard4  extends PublicObjectiveCard {
    //SFUMATURE DIVERSE - COLONNA

    public PublicObjectiveCard4(){ this.id = POCConstants.POC4_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        for (int col = 0; col < WpcConstants.COLS_NUMBER; col++){
            ArrayList<Dice> colDices = wpc.getColDices(col);
            if (colDices.size() == WpcConstants.ROWS_NUMBER && allNumbersAreDifferent(colDices)){
                score += POCConstants.POC4_SCORE;
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