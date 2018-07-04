package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.NO_SOCKET_FOUND_P1;
import static shared.constants.ExceptionConstants.NO_SOCKET_FOUND_P2;

public class NoSocketForUserException extends Exception{
    private String user;

    public NoSocketForUserException(String username) {
        user = username;
    }

    @Override
    public String getMessage() {
       return NO_SOCKET_FOUND_P1 + user + NO_SOCKET_FOUND_P2;
    }


}
