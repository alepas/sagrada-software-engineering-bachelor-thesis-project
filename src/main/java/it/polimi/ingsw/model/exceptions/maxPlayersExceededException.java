package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.game.AbstractGame;
import it.polimi.ingsw.model.usersdb.User;

public class maxPlayersExceededException extends RuntimeException {
    private final User user;
    private final AbstractGame game;

    public maxPlayersExceededException(User user, AbstractGame game) {
        this.user = user;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Cannot add " + user.getUsername() + " to game: " + game.getGameID() + ". "
                + "This game has already reached the maximum number of players: " + game.getPlayers();
    }
}
