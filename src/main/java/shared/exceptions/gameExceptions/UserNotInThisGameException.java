package shared.exceptions.gameExceptions;

import server.model.game.Game;
import static shared.constants.ExceptionConstants.USER_NOT_IN_GAME;

public class UserNotInThisGameException extends Exception {
    private final String username;
    private final Game game;

    /**
     * @param username is the player username
     * @param game is the game that the player is playing
     */
    public UserNotInThisGameException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    /**
     * @return a string that tells that the player is not in the given game
     */
    @Override
    public String getMessage() {
        return username + USER_NOT_IN_GAME + game.getID();
    }
}
