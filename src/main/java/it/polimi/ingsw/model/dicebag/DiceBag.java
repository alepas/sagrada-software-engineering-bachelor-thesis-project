package it.polimi.ingsw.model.dicebag;

import java.util.ArrayList;

import static it.polimi.ingsw.model.constants.DiceBagCostants.INITIAL_DICE_NUMBER;
import static it.polimi.ingsw.model.constants.DiceBagCostants.SOLO_PLAYER_DICES;

public class DiceBag {
    private int diceIdGenerator;
    private int yellowDices;
    private int redDices;
    private int blueDices;
    private int greenDices;
    private int violetDices;


    //quando costruisco il sacco nella partita multiplayer passerò al costruttore
    // l'int numDices =2*numplayers +1
    public DiceBag(){
        diceIdGenerator = 0;
        yellowDices = INITIAL_DICE_NUMBER;
        redDices = INITIAL_DICE_NUMBER;
        blueDices = INITIAL_DICE_NUMBER;
        greenDices = INITIAL_DICE_NUMBER;
        violetDices = INITIAL_DICE_NUMBER;
    }

    public ArrayList<Dice> extractDices(int numPlayers){
        int numOfDices;
        ArrayList<Dice> extractedDices = new ArrayList<>();
        if(numPlayers == 1)
            numOfDices = SOLO_PLAYER_DICES;
        else
            numOfDices = numPlayers*2 +1; //costante computata
        for (int i = 0; i < numOfDices; i++)
            extractedDices.add(pickDice());
        return extractedDices;
    }

     private Dice pickDice() {
        Color color;
        do {
            color = Color.randomColor();
        } while(!checkColor(color));
        switch (color){
            case RED:
                redDices--;
                break;
            case BLUE:
                blueDices--;
                break;
            case GREEN:
                greenDices--;
                break;
            case VIOLET:
                violetDices--;
                break;
            case YELLOW:
                yellowDices--;
                break;
        }
        return new Dice(color, setDiceID());
    }

    //id per riconoscere il dado, valore sequenziale, non tiene conto del numero totale dei dadi
    private int setDiceID() { return diceIdGenerator++; }


    //nell enum ogni val è associato ad un valore numerico tramite color.ordinal() recupero tale valore
    //utile per fare il check
    private boolean checkColor(Color color){
        if (color == Color.VIOLET)
            return (violetDices > 0);
        else if (color == Color.BLUE)
            return (blueDices > 0);
        else if (color == Color.RED)
            return (redDices > 0);
        else if (color == Color.YELLOW)
            return (yellowDices > 0);
        else
            return (greenDices > 0);
    }

    //Diluente per pasta salda: reinserisco un dado nel sacchetto
    public void reInsertDice(Dice dice){
        Color color = dice.getDiceColor();
        switch (color) {
            case RED:
                if (redDices <= 18)
                    redDices++;
                break;
            case BLUE:
                if (blueDices <= 18)
                    blueDices++;
                break;
            case GREEN:
                if (greenDices <= 18)
                    greenDices++;
                break;
            case VIOLET:
                if (violetDices <= 18)
                    violetDices++;
                break;
            case YELLOW:
                if (yellowDices <= 18)
                    yellowDices++;
                break;
        }
    }


    int getYellowDices(){return yellowDices;}
    int getRedDices(){return redDices;}
    int getGreenDices(){return greenDices;}
    int getBlueDices(){return blueDices;}
    int getVioletDices(){return violetDices;}
    int getDiceIdGenerator(){return diceIdGenerator;}
}

