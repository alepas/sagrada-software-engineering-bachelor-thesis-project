package shared.exceptions.gameExceptions;

import shared.constants.ExceptionConstants;

public class NotYourWpcException extends Exception {
    public final String id;

    /**
     * Constructor of this.
     *
     * @param id is the id of the chosen schema
     */
    public NotYourWpcException(String id) {
        this.id = id;
    }

    /**
     * @return a message which tells to the player that it is not possible to choose a schema that hasn't been
     * assigned to him/her
     */
    @Override
    public String getMessage() {
        return ExceptionConstants.NOT_YOUR_WPC_P1 + id
                + ExceptionConstants.NOT_YOUR_WPC_P2;
    }
}
