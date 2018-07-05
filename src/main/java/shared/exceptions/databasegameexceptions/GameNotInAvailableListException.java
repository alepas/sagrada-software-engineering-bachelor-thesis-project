package shared.exceptions.databasegameexceptions;

import server.model.game.Game;
import shared.constants.ExceptionConstants;

public class GameNotInAvailableListException  extends Exception {
    private final Game game;

    /**
     * Constructor of this.
     *
     * @param game is the played game
     */
    public GameNotInAvailableListException(Game game){
        this.game = game;
    }

    /**
     * @return a string that tells to the player that the chosen game isn't available
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.GAME_NOT_IN_AVAILABLE_LIST + game.getID();
    }
}
