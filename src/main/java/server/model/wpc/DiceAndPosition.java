package server.model.wpc;

import server.model.dicebag.Dice;
import shared.clientinfo.Position;

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
