package shared.exceptions.gameexceptions;

import shared.constants.ExceptionConstants;

public class InvalidSinglePlayerGamePlayersException extends Exception {
    private final int numPlayers;

    /**
     * Constructor of this.
     *
     * @param numPlayers is the number of players
     */
    public InvalidSinglePlayerGamePlayersException(int numPlayers){
        this.numPlayers = numPlayers;
    }

    /**
     * @return a string that tells to the player that it is not possible to create a single player game.
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.INVALID_SINGLEPLAYER_GAME_PLAYERS_P1 + numPlayers +
                ExceptionConstants.INVALID_SINGLEPLAYER_GAME_PLAYERS_P2;
    }
}