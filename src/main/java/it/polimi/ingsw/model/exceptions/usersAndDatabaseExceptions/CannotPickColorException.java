package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.wpc.Position;

public class CannotPickColorException extends Exception{
    private Color color;
    private String user;

    public CannotPickColorException(String user, Color color) {
        this.color=color;
        this.user = user;
    }
    @Override
    public String getMessage() {
        return "Can't pick the color";

    }




}
