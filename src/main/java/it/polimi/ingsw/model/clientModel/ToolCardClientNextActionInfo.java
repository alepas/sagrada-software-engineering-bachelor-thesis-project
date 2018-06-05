package it.polimi.ingsw.model.clientModel;

import java.util.ArrayList;

public class ToolCardClientNextActionInfo {
    public ClientDiceLocations wherePickNewDice;
    public ClientDiceLocations wherePutNewDice;
    public ArrayList<Integer> numbersToChoose;
    public Integer diceChosenId;

    public ToolCardClientNextActionInfo(ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice, ArrayList<Integer> numbersToChoose, Integer diceChosenId) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.numbersToChoose = numbersToChoose;
        this.diceChosenId = diceChosenId;
    }
}
