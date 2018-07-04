package shared.exceptions.gameExceptions;

import shared.constants.ExceptionConstants;

public class NotYourWpcException extends Exception {
    public final String id;

    public NotYourWpcException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.NOT_YOUR_WPC_P1 + id
                + ExceptionConstants.NOT_YOUR_WPC_P2;
    }
}
