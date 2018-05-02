package it.polimi.ingsw.model.exceptions.gameExceptions;

import it.polimi.ingsw.model.game.Game;

public class MaxPlayersExceededException extends RuntimeException {
    private final String username;
    private final Game game;

    public MaxPlayersExceededException(String username, Game game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Cannot add " + username + " to game: " + game.getGameID() + ". "
                + "This game has already reached the maximum number of players: " + game.getPlayers();
    }
}
