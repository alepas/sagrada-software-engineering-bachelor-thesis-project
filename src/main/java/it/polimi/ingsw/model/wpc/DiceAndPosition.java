package it.polimi.ingsw.model.wpc;

import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.dicebag.Dice;

public class DiceAndPosition {
    private Dice dice;
    private Position position;

    public DiceAndPosition(Dice dice, Position position) {
        this.dice = dice;
        this.position = position;
    }

    public Dice getDice() {
        return dice;
    }

    public Position getPosition() {
        return position;
    }
}
