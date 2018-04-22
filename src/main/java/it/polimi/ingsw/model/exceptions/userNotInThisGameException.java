package it.polimi.ingsw.model.exceptions;
import it.polimi.ingsw.model.game.AbstractGame;
import it.polimi.ingsw.model.usersdb.User;

public class userNotInThisGameException extends RuntimeException {
    private final User user;
    private final AbstractGame game;

    public userNotInThisGameException(User user, AbstractGame game) {
        this.user = user;
        this.game = game;
    }

    @Override
    public String getMessage() {
        return user.getUsername() + " not in game: " + game.getGameID();
    }
}
