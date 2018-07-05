package shared.exceptions.gameexceptions;

import shared.constants.ExceptionConstants;

public class InvalidMultiPlayerGamePlayersException extends Exception {
    private final int numPlayers;
    private final int minPlayers;
    private final int maxPlayers;

    public InvalidMultiPlayerGamePlayersException(int numPlayers, int minPlayers, int maxPlayers) {
        this.numPlayers = numPlayers;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    /**
     * @return a string that tells to the player that it is not possible to create a multi player game.
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.INVALID_MULTIPLAYER_GAME_PLAYERS_P1 + numPlayers +
                ExceptionConstants.INVALID_MULTIPLAYER_GAME_PLAYERS_P2 + minPlayers +
                ExceptionConstants.INVALID_MULTIPLAYER_GAME_PLAYERS_P3 + maxPlayers +
                ExceptionConstants.INVALID_MULTIPLAYER_GAME_PLAYERS_P4;
    }
}