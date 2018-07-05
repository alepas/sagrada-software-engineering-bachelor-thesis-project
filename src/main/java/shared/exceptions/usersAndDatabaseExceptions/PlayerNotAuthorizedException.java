package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.PLAYER_NOT_AUTHORIZED_P1;
import static shared.constants.ExceptionConstants.PLAYER_NOT_AUTHORIZED_P2;

public class PlayerNotAuthorizedException extends Exception{
    private String user;

    /**
     * @param username is the player username
     */
    public PlayerNotAuthorizedException(String username ){
        user=username;
    }

    /**
     * @return a messages which tells to the player that he/she can't do nothing because is somebody else turn
     */
    @Override
    public String getMessage() {
       return PLAYER_NOT_AUTHORIZED_P1 + user + PLAYER_NOT_AUTHORIZED_P2;
    }


}
