package it.polimi.ingsw.model.clientModel;

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


    public ToolCardClientNextActionInfo(ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice, ArrayList<Integer> numbersToChoose, ClientDice diceChosen, ClientDiceLocations diceChosenLocation, String stringForStopToolCard, boolean bothYesAndNo, boolean showBackButton) {
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
