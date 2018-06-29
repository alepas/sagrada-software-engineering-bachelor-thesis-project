package it.polimi.ingsw.server.model.wpc;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.shared.clientInfo.Position;

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
