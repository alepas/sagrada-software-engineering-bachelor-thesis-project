package shared.exceptions.usersAndDatabaseExceptions;


import shared.constants.ExceptionConstants;

public class CannotPickPositionException extends Exception{

    /**
     * @return a string that tell that it is not possible to add a dice in the chosen position
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.CANNOT_PICK_POSITION;

    }

}
