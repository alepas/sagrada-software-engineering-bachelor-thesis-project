package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.DATABASE_FILE_ERROR_0;
import static shared.constants.ExceptionConstants.DATABASE_FILE_ERROR_1;

public class DatabaseFileErrorException extends Exception{
    private int cause;

    public DatabaseFileErrorException(int errorID) {
        cause=errorID;
    }

    @Override
    public String getMessage() {
        if (cause == 1) return DATABASE_FILE_ERROR_1;
        return DATABASE_FILE_ERROR_0;
    }

    public int getErrorId() {
        return cause;
    }
}
