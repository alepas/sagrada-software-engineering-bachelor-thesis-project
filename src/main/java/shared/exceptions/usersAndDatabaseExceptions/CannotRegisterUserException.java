package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.*;

public class CannotRegisterUserException extends Exception{
    private int cause;
    private String user;

    /**
     * Contructor of this exception.
     *
     * @param username is the username of the player that has caused the excpetion
     * @param cause is the id of the reason why the player has thrown the exception
     */
    public CannotRegisterUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }

    /**
     * @return the string that will explain to the player which is the problem:
     * - if the cause's id is 0 the message will tell that it is not possible to create the account because of internal
     *   problems
     * - if the cause's id is 1 the message will tell that somebody has already chosen this username
     * - if the cause's id is 2 the message will tell that it hasn't been possible to register the player
     */
    @Override
    public String getMessage() {
        if (cause == 0)
        return CANNOT_REGISTER_USER_0_P1 + user + CANNOT_REGISTER_USER_0_P2;
        else if (cause == 1)
            return CANNOT_REGISTER_USER_1_P1 + user + CANNOT_REGISTER_USER_1_P2;
        else return CANNOT_REGISTER_USER_2_P1 + user + CANNOT_REGISTER_USER_2_P2;
    }


    public int getErrorId() {
        return cause;
    }

}
