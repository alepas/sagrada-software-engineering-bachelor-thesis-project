package it.polimi.ingsw.model.exceptions.gameExceptions;
import it.polimi.ingsw.model.game.AbstractGame;

public class NotEnoughPlayersException extends RuntimeException {
    private final AbstractGame game;

    public NotEnoughPlayersException(AbstractGame game) {
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Not enough players to start game: " + game.getGameID() + ". Joined: "
                + game.getPlayers().size() + ". Required: " + game.getNumPlayers();
    }
}