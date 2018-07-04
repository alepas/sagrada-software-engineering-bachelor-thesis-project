package shared.exceptions.wpcExceptions;

import server.model.wpc.Cell;
import shared.constants.ExceptionConstants;

public class NotExistingCellException extends Exception {
    public final Cell cell;

    public NotExistingCellException(Cell cell) {

        this.cell = cell;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.NOT_EXISTING_CELL + cell.getCellPosition().getRow() + ", " +
                cell.getCellPosition().getColumn() + ")";
    }
}
