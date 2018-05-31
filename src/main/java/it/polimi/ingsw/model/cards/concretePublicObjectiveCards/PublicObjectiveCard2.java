package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.WPC;

import java.util.ArrayList;

public class PublicObjectiveCard2 extends PublicObjectiveCard {
    //COLORI DIVERSI - COLONNA

    public PublicObjectiveCard2() {
        this.id = POCConstants.POC2_ID;
        this.name = POCConstants.POC2_NAME;
        this.description = POCConstants.POC2_DESCRIPTION;
    }

    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        for (int column = 0; column < WpcConstants.COLS_NUMBER; column++){
            ArrayList<Dice> columnDices = wpc.getColDices(column);
            if (columnDices.size() == WpcConstants.ROWS_NUMBER && allColorsAreDifferent(columnDices))
                score += POCConstants.POC2_SCORE;
        }

        return score;
    }

    private boolean allColorsAreDifferent(ArrayList<Dice> columnDices){
        ArrayList<Color> extractedColors = new ArrayList<>();

        for (Dice dice : columnDices){
            if (extractedColors.contains(dice.getDiceColor())) return false;
            extractedColors.add(dice.getDiceColor());
        }

        return true;
    }
}
