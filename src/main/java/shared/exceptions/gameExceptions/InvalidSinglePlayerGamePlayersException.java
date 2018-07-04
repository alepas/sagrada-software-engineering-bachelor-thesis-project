package shared.exceptions.gameExceptions;

import shared.constants.ExceptionConstants;

public class InvalidSinglePlayerGamePlayersException extends Exception {
    private final int numPlayers;

    public InvalidSinglePlayerGamePlayersException(int numPlayers){
        this.numPlayers = numPlayers;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.INVALID_SINGLEPLAYER_GAME_PLAYERS_P1 + numPlayers +
                ExceptionConstants.INVALID_SINGLEPLAYER_GAME_PLAYERS_P2;
    }
}