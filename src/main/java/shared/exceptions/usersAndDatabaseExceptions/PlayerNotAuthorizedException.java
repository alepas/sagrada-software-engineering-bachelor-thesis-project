package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.PLAYER_NOT_AUTHORIZED_P1;
import static shared.constants.ExceptionConstants.PLAYER_NOT_AUTHORIZED_P2;

public class PlayerNotAuthorizedException extends Exception{
    private String user;

    public PlayerNotAuthorizedException(String username ){
        user=username;
    }

    @Override
    public String getMessage() {
       return PLAYER_NOT_AUTHORIZED_P1 + user + PLAYER_NOT_AUTHORIZED_P2;
    }


}
