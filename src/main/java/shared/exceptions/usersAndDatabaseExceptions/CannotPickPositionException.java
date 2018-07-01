package shared.exceptions.usersAndDatabaseExceptions;

import shared.clientInfo.Position;

public class CannotPickPositionException extends Exception{
    private Position pos;
    private String user;

    public CannotPickPositionException(String user, Position position) {
        this.pos=position;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return "La posizione scelta non è valida.";

    }




}
