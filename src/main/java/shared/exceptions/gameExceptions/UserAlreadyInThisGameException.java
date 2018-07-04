package shared.exceptions.gameExceptions;

import server.model.game.Game;
import shared.constants.ExceptionConstants;

public class UserAlreadyInThisGameException extends Exception {
    private final String username;
    private final Game game;

    public UserAlreadyInThisGameException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return username + ExceptionConstants.USER_ALREADY_IN_GAME + game.getID();
    }
}
