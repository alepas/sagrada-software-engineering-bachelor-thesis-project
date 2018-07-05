package shared.exceptions.gameexceptions;

import server.model.game.Game;
import shared.constants.ExceptionConstants;

public class UserAlreadyInThisGameException extends Exception {
    private final String username;
    private final Game game;

    /**
     * @param username is the player's username
     * @param game is the game where the player should be in
     */
    public UserAlreadyInThisGameException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    /**
     * @return a string that tells to the player that tells that the player is already in the given game
     */
    @Override
    public String getMessage() {
        return username + ExceptionConstants.USER_ALREADY_IN_GAME + game.getID();
    }
}
