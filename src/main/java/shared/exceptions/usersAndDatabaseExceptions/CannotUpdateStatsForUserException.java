package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.CANNOT_UPDATE_STATS_P1;
import static shared.constants.ExceptionConstants.CANNOT_UPDATE_STATS_P2;

public class CannotUpdateStatsForUserException extends Exception{
    private int cause;
    private String user;

    public CannotUpdateStatsForUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }
    @Override
    public String getMessage() {
        return  CANNOT_UPDATE_STATS_P1 + user + CANNOT_UPDATE_STATS_P2;
    }

    public int getErrorId() {
        return cause;
    }
}
