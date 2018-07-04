package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.PASSWORD_PARSING_ERROR;

public class PasswordParsingException extends Exception {
    public PasswordParsingException(){}
    @Override
    public String getMessage() {
        return PASSWORD_PARSING_ERROR;
    }
}

