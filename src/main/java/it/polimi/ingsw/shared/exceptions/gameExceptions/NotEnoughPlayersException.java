package it.polimi.ingsw.shared.exceptions.gameExceptions;
import it.polimi.ingsw.server.model.game.Game;

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