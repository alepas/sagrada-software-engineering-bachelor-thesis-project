package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.NO_TOOLCARD_IN_USE;

public class NoToolCardInUseException extends Exception{

    /**
     * @return a message that tells thet the player isn't using the toolcard
     */
    @Override
    public String getMessage() {
        return NO_TOOLCARD_IN_USE;
    }



}
