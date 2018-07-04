package shared.exceptions.gameExceptions;

import shared.constants.ExceptionConstants;

public class CannotCreatePlayerException extends Exception {
    private final String username;

    public CannotCreatePlayerException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_CREATE_PLAYER + username;
    }
}
