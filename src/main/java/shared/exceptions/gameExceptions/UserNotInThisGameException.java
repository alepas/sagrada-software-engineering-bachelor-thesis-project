package shared.exceptions.gameExceptions;
import server.model.game.Game;
import shared.constants.ExceptionConstants;

public class UserNotInThisGameException extends Exception {
    private final String username;
    private final Game game;

    public UserNotInThisGameException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return username + ExceptionConstants.USER_NOT_IN_GAME + game.getID();
    }
}
