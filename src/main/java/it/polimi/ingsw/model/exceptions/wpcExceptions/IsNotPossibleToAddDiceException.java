package it.polimi.ingsw.model.exceptions.wpcExceptions;

import it.polimi.ingsw.model.WPC.Cell;

public class IsNotPossibleToAddDiceException extends Exception {
    private final Cell cell;

    public IsNotPossibleToAddDiceException(Cell cell) {
        this.cell = cell;
    }

    @Override
    public String getMessage() {
            return "It is not possible to add a dice in this cell";
    }
}
