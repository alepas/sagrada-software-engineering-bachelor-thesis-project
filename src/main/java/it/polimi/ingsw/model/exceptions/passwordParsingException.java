package it.polimi.ingsw.model.exceptions;

public class passwordParsingException extends RuntimeException {
    @Override
    public String getMessage() {
        return "There was a problem parsing the password";
    }
}

