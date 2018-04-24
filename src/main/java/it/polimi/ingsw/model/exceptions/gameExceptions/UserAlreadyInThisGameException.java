package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.game.AbstractGame;

public class UserAlreadyInThisGameException extends RuntimeException {
    private final String username;
    private final AbstractGame game;

    public UserAlreadyInThisGameException(String username, AbstractGame game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return username + " already joined this game: " + game.getGameID();
    }
}
