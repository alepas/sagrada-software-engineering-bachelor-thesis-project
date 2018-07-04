package shared.exceptions.gameExceptions;

import server.model.game.Game;
import shared.constants.ExceptionConstants;

public class MaxPlayersExceededException extends Exception {
    private final String username;
    private final Game game;

    public MaxPlayersExceededException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.MAX_PLAYER_EXCEEDED_P1 + username + ExceptionConstants.MAX_PLAYER_EXCEEDED_P2
                + game.getID() + ExceptionConstants.MAX_PLAYER_EXCEEDED_P3 + game.getPlayers().length;
    }
}
