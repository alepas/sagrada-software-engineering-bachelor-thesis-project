package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

import static shared.constants.ExceptionConstants.*;

public class CannotPerformThisMoveException extends Exception{
    private int cause;
    private String user;
    private boolean endTurn;


    public CannotPerformThisMoveException(String username, int cause, boolean endTurn) {

        this.cause=cause;
        this.user = username;
        this.endTurn=endTurn;
    }
    @Override
    public String getMessage() {
        String nameOfMove;
        if (endTurn)
            nameOfMove = ExceptionConstants.CANNOT_PERFORM_THIS_MOVE_NAME_1;
        else nameOfMove = ExceptionConstants.CANNOT_PERFORM_THIS_MOVE_NAME_2;

        if (cause==0)
            return CANNOT_PERFORM_THIS_MOVE_IMPOSSIBLE + nameOfMove + CANNOT_PERFORM_THIS_MOVE_FOR_PLAYER + user + CANNOT_PERFORM_THIS_MOVE_0;

        else if (cause==1)
            return CANNOT_PERFORM_THIS_MOVE_IMPOSSIBLE + nameOfMove + CANNOT_PERFORM_THIS_MOVE_FOR_PLAYER + user + "\n" +
                    CANNOT_PERFORM_THIS_MOVE_1;
        else if (cause==2)
            return CANNOT_PERFORM_THIS_MOVE_2;

        else if (cause==3)
            return CANNOT_PERFORM_THIS_MOVE_3;

        return CANNOT_PERFORM_THIS_MOVE_IMPOSSIBLE + nameOfMove + CANNOT_PERFORM_THIS_MOVE_FOR_PLAYER + user + ".";
    }


    public int getErrorId() {
        return cause;
    }
}
