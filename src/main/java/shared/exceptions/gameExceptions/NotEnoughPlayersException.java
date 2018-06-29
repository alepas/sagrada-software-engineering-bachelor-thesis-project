package shared.exceptions.gameExceptions;
import server.model.game.Game;

public class NotEnoughPlayersException extends Exception {
    private final Game game;

    public NotEnoughPlayersException(Game game) {
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Not enough players to start game: " + game.getID() + ". Joined: "
                + game.getPlayers().length + ". Required: " + game.getNumPlayers();
    }
}