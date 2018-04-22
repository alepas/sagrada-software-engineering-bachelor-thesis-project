package it.polimi.ingsw.model.exceptions;
import it.polimi.ingsw.model.game.AbstractGame;

public class notEnoughPlayersException extends RuntimeException {
    private final AbstractGame game;

    public notEnoughPlayersException(AbstractGame game) {
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Not enough players to start game: " + game.getGameID() + ". Joined: "
                + game.getPlayers().size() + ". Required: " + game.getNumPlayers();
    }
}