package shared.exceptions.gameExceptions;
import server.model.game.Game;
import shared.constants.ExceptionConstants;

public class NotEnoughPlayersException extends Exception {
    private final Game game;

    public NotEnoughPlayersException(Game game) {
        this.game = game;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.NOT_ENOUGH_PLAYERS_P1 + game.getID() + ExceptionConstants.NOT_ENOUGH_PLAYERS_P2
                + game.getPlayers().length + ExceptionConstants.NOT_ENOUGH_PLAYERS_P3 + game.getNumPlayers();
    }
}