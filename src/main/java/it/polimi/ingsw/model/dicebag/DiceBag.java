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


    /**
     * Initializes the dice bag with 18 dices for each color, sets the id generator equals to zero.
     */
    public DiceBag(){
        diceIdGenerator = 0;
        yellowDices = INITIAL_DICE_NUMBER;
        redDices = INITIAL_DICE_NUMBER;
        blueDices = INITIAL_DICE_NUMBER;
        greenDices = INITIAL_DICE_NUMBER;
        violetDices = INITIAL_DICE_NUMBER;
    }

    /**
     * creates as many dices as the value given by the formula numplayers*2-1.
     *
     * @param numPlayers is the number of players of the game related to the object
     * @return an arrayList composed by the dices created
     */
    public ArrayList<Dice> extractDices(int numPlayers){
        int numOfDices;
        ArrayList<Dice> extractedDices = new ArrayList<>();
        if(numPlayers == 1)
            numOfDices = SOLO_PLAYER_DICES;
        else
            numOfDices = numPlayers*2 +1;
        for (int i = 0; i < numOfDices; i++)
            extractedDices.add(pickDice());
        return extractedDices;
    }

    /**
     * Calls the dice's constructor after choosing the dice color in a random way. It decreases the parameter related to
     * the chosen color iff the checkColor method returns true.
     *
     * @return the new dice
     */
     public Dice pickDice() {
        Color color;
        do {
            color = Color.randomColor();
        } while(!checkColor(color));
        switch (color){
            case RED:
                redDices --;
                break;
            case BLUE:
                blueDices --;
                break;
            case GREEN:
                greenDices --;
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


    private int setDiceID() { return diceIdGenerator++; }


    /**
     * Checks if are still available dices of the color passed as parameter.
     *
     * @param color is the random color choosen for the new dice
     * @return a boolean: if it is true it means that it is possible to create a new dice with the chosen color,
     * if it is false the method above will have to call again the random method to chose a new one
     */
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


    /**
     *This method is related to a tool card; it add one to the integer related to the color of the dice chosen.
     *
     * @param dice is the object that has been chosen by the player
     */
    public void reInsertDice(Dice dice){
        Color color = dice.getDiceColor();
        switch (color) {
            case RED:
                if (redDices < 18)
                    redDices++;
                break;
            case BLUE:
                if (blueDices < 18)
                    blueDices++;
                break;
            case GREEN:
                if (greenDices < 18)
                    greenDices++;
                break;
            case VIOLET:
                if (violetDices < 18)
                    violetDices++;
                break;
            case YELLOW:
                if (yellowDices < 18)
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

