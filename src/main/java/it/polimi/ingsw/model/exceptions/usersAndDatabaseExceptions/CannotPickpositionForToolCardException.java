package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.wpc.Position;

public class CannotPickpositionForToolCardException extends Exception{
    private Position pos;
    private String user;

    public CannotPickpositionForToolCardException(String user, Position position) {
        this.pos=position;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return "Can't pick the position for toolcard lable in the current set of dices";

    }




}
