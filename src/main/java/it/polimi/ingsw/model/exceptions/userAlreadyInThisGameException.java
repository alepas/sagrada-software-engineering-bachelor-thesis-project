package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.game.AbstractGame;

public class userAlreadyInThisGameException extends RuntimeException {
    private final String username;
    private final AbstractGame game;

    public userAlreadyInThisGameException(String username, AbstractGame game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return username + " already joined this game: " + game.getGameID();
    }
}
