package shared.exceptions.databaseGameExceptions;

import server.model.game.Game;
import shared.constants.ExceptionConstants;

public class GameNotInAvailableListException  extends Exception {
    private final Game game;

    public GameNotInAvailableListException(Game game){
        this.game = game;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.GAME_NOT_IN_AVAILABLE_LIST + game.getID();
    }
}
