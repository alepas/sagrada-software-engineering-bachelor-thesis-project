package shared.exceptions.dicebagexceptions;

import shared.constants.ExceptionConstants;

public class IncorrectNumberException extends Exception {
    //TODO:io la toglierei
    public final int number;

    public IncorrectNumberException(int number) {
        this.number = number;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.DICEBAG_WRONG_NUMBER;
    }
}
