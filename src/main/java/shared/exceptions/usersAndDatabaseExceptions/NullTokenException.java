package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.NULL_TOKEN;

public class NullTokenException extends Exception{
    private String user;

    public NullTokenException(){}
    @Override
    public String getMessage() {
       return NULL_TOKEN;
    }


}
