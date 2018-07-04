package shared.exceptions.dicebagExceptions;

import shared.constants.ExceptionConstants;

public class IncorrectNumberException extends Exception {
    //TODO: 0 e 6 sono costanti del server
    public final int number;

    public IncorrectNumberException(int number) {
        this.number = number;
    }

    @Override
    public String getMessage() {
        return ExceptionConstants.DICEBAG_WRONG_NUMBER;
    }
}
