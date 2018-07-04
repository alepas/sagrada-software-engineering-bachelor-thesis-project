package shared.exceptions.gameExceptions;

import shared.constants.ExceptionConstants;

public class InvalidMultiplayerGamePlayersException extends Exception {
    private final int numPlayers;
    private final int min_players;
    private final int max_players;

    public InvalidMultiplayerGamePlayersException(int numPlayers, int min_players, int max_players) {
        this.numPlayers = numPlayers;
        this.min_players = min_players;
        this.max_players = max_players;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.INVALID_MULTIPLAYER_GAME_PLAYERS_P1 + numPlayers +
                ExceptionConstants.INVALID_MULTIPLAYER_GAME_PLAYERS_P2 + min_players +
                ExceptionConstants.INVALID_MULTIPLAYER_GAME_PLAYERS_P3 + max_players +
                ExceptionConstants.INVALID_MULTIPLAYER_GAME_PLAYERS_P4;
    }
}