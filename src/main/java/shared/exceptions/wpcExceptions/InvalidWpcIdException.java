package shared.exceptions.wpcExceptions;

import static shared.constants.ExceptionConstants.INVALID_WPC_ID;

public class InvalidWpcIdException extends Exception {
    public final String id;

    public InvalidWpcIdException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return INVALID_WPC_ID + id;
    }
}
