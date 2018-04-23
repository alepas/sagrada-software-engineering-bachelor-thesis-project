package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.game.AbstractGame;

public class MaxPlayersExceededException extends RuntimeException {
    private final String username;
    private final AbstractGame game;

    public MaxPlayersExceededException(String username, AbstractGame game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Cannot add " + username + " to game: " + game.getGameID() + ". "
                + "This game has already reached the maximum number of players: " + game.getPlayers();
    }
}
