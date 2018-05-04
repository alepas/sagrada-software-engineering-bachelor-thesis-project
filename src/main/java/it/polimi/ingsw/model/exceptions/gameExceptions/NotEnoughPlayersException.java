package it.polimi.ingsw.model.exceptions.gameExceptions;
import it.polimi.ingsw.model.game.Game;

public class NotEnoughPlayersException extends RuntimeException {
    private final Game game;

    public NotEnoughPlayersException(Game game) {
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Not enough players to start game: " + game.getID() + ". Joined: "
                + game.getPlayers().size() + ". Required: " + game.getNumPlayers();
    }
}