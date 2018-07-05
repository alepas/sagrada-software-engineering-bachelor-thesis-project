package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

import static shared.constants.ExceptionConstants.*;

public class CannotPerformThisMoveException extends Exception{
    private int cause;
    private String user;
    private boolean endTurn;


    /**
     * @param username is the player sername
     * @param cause is the id of the reason why the exception as been thrown
     * @param endTurn is a boolean which tells if is or isn't the player's turn
     */
    public CannotPerformThisMoveException(String username, int cause, boolean endTurn) {
        this.cause=cause;
        this.user = username;
        this.endTurn=endTurn;
    }

    /**
     * @return a different message depending on the cause id:
     * - if it is 0 it means that the player must end an action (place dice) before end the turn;
     * - if it is 1 it means that the player must complete the toolcard;
     * - if it is 2 it means that the player is trying to do something that isn't available in that moment
     * - if is is 3 it means that the player has already done an action like the one that wants to do
     */
    @Override
    public String getMessage() {
        String nameOfMove;
        if (endTurn)
            nameOfMove = ExceptionConstants.CANNOT_PERFORM_THIS_MOVE_NAME_1;
        else nameOfMove = ExceptionConstants.CANNOT_PERFORM_THIS_MOVE_NAME_2;

        if (cause == 0)
            return CANNOT_PERFORM_THIS_MOVE_IMPOSSIBLE + nameOfMove + CANNOT_PERFORM_THIS_MOVE_FOR_PLAYER + user + CANNOT_PERFORM_THIS_MOVE_0;

        else if (cause == 1)
            return CANNOT_PERFORM_THIS_MOVE_IMPOSSIBLE + nameOfMove + CANNOT_PERFORM_THIS_MOVE_FOR_PLAYER + user + "\n" +
                    CANNOT_PERFORM_THIS_MOVE_1;
        else if (cause == 2)
            return CANNOT_PERFORM_THIS_MOVE_2;

        else if (cause == 3)
            return CANNOT_PERFORM_THIS_MOVE_3;

        return CANNOT_PERFORM_THIS_MOVE_IMPOSSIBLE + nameOfMove + CANNOT_PERFORM_THIS_MOVE_FOR_PLAYER + user + ".";
    }


    public int getErrorId() {
        return cause;
    }
}
