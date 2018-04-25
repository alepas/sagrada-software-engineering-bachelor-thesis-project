package it.polimi.ingsw.model.exceptions.wpcExceptions;

import it.polimi.ingsw.model.WPC.Cell;

public class NotExistingCellException extends Exception {
    public final Cell cell;

    public NotExistingCellException(Cell cell) {
        this.cell = cell;
    }

    @Override
    public String getMessage() {
        return "This cell doesn't exist";
    }
}
