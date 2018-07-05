package shared.clientinfo;

import java.io.Serializable;
import java.util.ArrayList;

public class ToolCardClientNextActionInfo implements Serializable {
    public final ClientDiceLocations wherePickNewDice;
    public final ClientDiceLocations wherePutNewDice;
    public final ArrayList<Integer> numbersToChoose;
    public final ClientDice diceChosen;
    public final ClientDiceLocations diceChosenLocation;
    public final String stringForStopToolCard;
    public final boolean bothYesAndNo;
    public final boolean showBackButton;


    /**
     * Constructor of this.
     *
     * @param wherePickNewDice is the location from where the dice must be picked
     * @param wherePutNewDice is the location where the dice must be put
     * @param numbersToChoose is the arrylist composed by the possible number that the player can choose
     * @param diceChosen is the chosen dice
     * @param diceChosenLocation is the previous location of the chosen dice
     * @param stringForStopToolCard is the string error text
     * @param bothYesAndNo is a boolean
     * @param showBackButton is a boolean
     */
    public ToolCardClientNextActionInfo(ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
          ArrayList<Integer> numbersToChoose, ClientDice diceChosen, ClientDiceLocations diceChosenLocation, String stringForStopToolCard, boolean bothYesAndNo, boolean showBackButton) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.numbersToChoose = numbersToChoose;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.stringForStopToolCard = stringForStopToolCard;
        this.bothYesAndNo = bothYesAndNo;
        this.showBackButton = showBackButton;
    }
}
