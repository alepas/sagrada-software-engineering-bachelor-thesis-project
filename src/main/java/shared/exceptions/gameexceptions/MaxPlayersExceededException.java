package shared.exceptions.gameexceptions;

import server.model.game.Game;
import shared.constants.ExceptionConstants;

public class MaxPlayersExceededException extends Exception {
    private final String username;
    private final Game game;

    /**
     * Constructor of this.
     *
     * @param username is the player's username
     * @param game is the game the player would like to be added
     */
    public MaxPlayersExceededException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    /**
     * @return a string that tells to the player that it is not possible to add a new player to the game because it has
     * already the max number of players
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.MAX_PLAYER_EXCEEDED_P1 + username + ExceptionConstants.MAX_PLAYER_EXCEEDED_P2
                + game.getID() + ExceptionConstants.MAX_PLAYER_EXCEEDED_P3 + game.getPlayers().length;
    }
}
