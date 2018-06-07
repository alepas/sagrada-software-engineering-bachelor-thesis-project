package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ToolCardClientNextActionInfo implements Serializable {
    public final ClientDiceLocations wherePickNewDice;
    public final ClientDiceLocations wherePutNewDice;
    public final ArrayList<Integer> numbersToChoose;
    public final Integer diceChosenId;
    public final ClientDiceLocations diceChosenLocation;

    public ToolCardClientNextActionInfo(ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice, ArrayList<Integer> numbersToChoose, Integer diceChosenId, ClientDiceLocations diceChosenLocation) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.numbersToChoose = numbersToChoose;
        this.diceChosenId = diceChosenId;
        this.diceChosenLocation = diceChosenLocation;
    }
}
