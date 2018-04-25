package it.polimi.ingsw.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

import java.util.ArrayList;

public class PublicObjectiveCard2 extends PublicObjectiveCard {
    //COLORI DIVERSI - COLONNA

    public PublicObjectiveCard2() { this.id = POCConstants.POC2_ID; }

    @Override
    public int calculateScore(WPC wpc) {
        int score = 0;

        for (int column = 0; column < WpcConstants.COLS_NUMBER; column++){
            ArrayList<Dice> columnDices = wpc.getColDices(column);
            if (allColorsAreDifferent(columnDices)) score += POCConstants.POC2_SCORE;
        }

        return score;
    }

    private boolean allColorsAreDifferent(ArrayList<Dice> columnDices){
        //controllo se nella colonna ci sono 4 colori
        ArrayList<Color> extractedColors = new ArrayList<>();
        int numberOfColor = 0;

        for (Dice dice : columnDices){
            if (!extractedColors.contains(dice.getDiceColor())) {
                extractedColors.add(dice.getDiceColor());
                numberOfColor++;
            }
        }

        return numberOfColor == 4;
    }
}
