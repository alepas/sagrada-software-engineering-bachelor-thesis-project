package shared.exceptions.gameExceptions;

import static shared.constants.ExceptionConstants.INVALID_NUM_PLAYERS;
import static shared.constants.ExceptionConstants.INVALID_TOOLCARD_LEVEL;

public class InvalidGameParametersException extends Exception {
    private final int number;
    private final boolean playersOrLevel;

    /**
     * @param number can be both the number of players or toolcard
     * @param playersOrLevel boolean which tells if the number is related to players or toolcard
     */
    public InvalidGameParametersException(int number, boolean playersOrLevel){
        this.number=number;
        this.playersOrLevel=playersOrLevel;
    }

    /**
     * @return a string with the message that will explane to the player the prblem:
     * - if the boolean is true the players' number is out of bound
     * - if the boolean is false the toolcards' number is out of bound
     */
    @Override
    public String getMessage() {
        if (playersOrLevel)
        return INVALID_NUM_PLAYERS + number;
        else
            return INVALID_TOOLCARD_LEVEL + number;

    }
}