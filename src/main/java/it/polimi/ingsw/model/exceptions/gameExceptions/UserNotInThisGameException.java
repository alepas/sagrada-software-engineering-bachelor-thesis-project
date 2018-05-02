package it.polimi.ingsw.model.exceptions.gameExceptions;
import it.polimi.ingsw.model.game.Game;

public class UserNotInThisGameException extends RuntimeException {
    private final String username;
    private final Game game;

    public UserNotInThisGameException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return username + " not in game: " + game.getGameID();
    }
}
