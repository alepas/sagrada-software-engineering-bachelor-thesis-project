package shared.exceptions.gameExceptions;

import shared.constants.ExceptionConstants;

public class InvalidNumOfPlayersException extends Exception {
    private final int numPlayers;

    public InvalidNumOfPlayersException(int numPlayers){
        this.numPlayers = numPlayers;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.INVALID_NUM_OF_PLAYERS + numPlayers;
    }
}