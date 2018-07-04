package shared.exceptions.usersAndDatabaseExceptions;

import shared.clientInfo.Position;
import shared.constants.ExceptionConstants;

public class CannotPickPositionException extends Exception{
    private Position pos;
    private String user;

    public CannotPickPositionException(String user, Position position) {
        //TODO: Eliminare pos e user
        this.pos=position;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_PICK_POSITION;

    }




}
