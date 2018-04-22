package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.game.AbstractGame;
import it.polimi.ingsw.model.usersdb.User;

public class userAlreadyInThisGameException extends RuntimeException {
    private final User user;
    private final AbstractGame game;

    public userAlreadyInThisGameException(User user, AbstractGame game) {
        this.user = user;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return user.getUsername() + " already joined this game: " + game.getGameID();
    }
}
