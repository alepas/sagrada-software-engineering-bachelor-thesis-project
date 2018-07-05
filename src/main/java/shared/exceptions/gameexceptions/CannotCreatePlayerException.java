package shared.exceptions.gameexceptions;

import shared.constants.ExceptionConstants;

public class CannotCreatePlayerException extends Exception {
    private final String username;

    /**
     * Constructor of this.
     *
     * @param username is the player's username
     */
    public CannotCreatePlayerException(String username) {
        this.username = username;
    }

    /**
     * @return a string that tells to the player that it is not possible to create the player in game object
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_CREATE_PLAYER + username;
    }
}
