package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.*;

public class CannotRegisterUserException extends Exception{
    private int cause;
    private String user;

    public CannotRegisterUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==0)
        return CANNOT_REGISTER_USER_0_P1 + user + CANNOT_REGISTER_USER_0_P2;
        else if (cause==1)
            return CANNOT_REGISTER_USER_1_P1 + user + CANNOT_REGISTER_USER_1_P2;
        else return CANNOT_REGISTER_USER_2_P1 + user + CANNOT_REGISTER_USER_2_P2;
    }
    public int getErrorId() {
        return cause;
    }

}
