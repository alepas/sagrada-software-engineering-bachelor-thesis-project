package shared.exceptions.databaseGameExceptions;

import server.model.game.Game;

public class GameNotInAvailableListException  extends Exception {
    private final Game game;

    public GameNotInAvailableListException(Game game){
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Game non found in available list: " + game.getID();
    }
}
