package it.polimi.ingsw.model.exceptions.dicebagExceptions;

public class IncorrectNumberException extends Exception {
    public final int number;

    public IncorrectNumberException(int number) {
        this.number = number;
    }

    @Override
    public String getMessage() {
        return "You can only set a number between 0 and 6";
    }
}
