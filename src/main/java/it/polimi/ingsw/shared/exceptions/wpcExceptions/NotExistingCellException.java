package it.polimi.ingsw.shared.exceptions.wpcExceptions;

import it.polimi.ingsw.server.model.wpc.Cell;

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
