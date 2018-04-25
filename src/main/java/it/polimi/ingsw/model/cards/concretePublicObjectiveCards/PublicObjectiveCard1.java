package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

import java.util.ArrayList;

public class PublicObjectiveCard1 extends PublicObjectiveCard {
    //COLORI DIVERSI - RIGA

    public PublicObjectiveCard1(){ this.id = POCConstants.POC1_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        for (int row = 0; row < WpcConstants.ROWS_NUMBER; row++){
            Dice[] rowDices = wpc.getRowDices(row);
            if (allColorsAreDifferent(rowDices)) score += POCConstants.POC1_SCORE;
        }

        return score;
    }

    private boolean allColorsAreDifferent(Dice[] dices){
        ArrayList<Color> extractedColors = new ArrayList<>();

        for (Dice dice : dices){
            if (extractedColors.contains(dice.getDiceColour())) return false;
            extractedColors.add(dice.getDiceColour());
        }

        return true;
    }
}
