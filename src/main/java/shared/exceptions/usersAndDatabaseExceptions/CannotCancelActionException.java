package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotCancelActionException extends Exception{
    private int mode;
    //0=action normale 1=azione dentro la toolcard 2=la toolcard stessa
    private String user ;
    private String cardID;


    /**
     * Constructor of this which has only the user variable and sets the cirdID to null and mode to 0
     * @param user is the player username
     */
    public CannotCancelActionException(String user){
        this.user=user;
        this.cardID=null;
        this.mode=0;
    }

    /**
     * Constructor of this.
     *
     * @param user is the player username
     * @param card is the toolcard id
     * @param mode is the exception id
     */
    public CannotCancelActionException(String user, String card, int mode) {
        this.user=user;
        this.cardID = card;
        this.mode=mode;
    }

    /**
     * @return a string containing the message related to the exception:
     * - if the mode's id is 0 the string will tell that it is not possible to cancel the action
     * - if the mode's id is 1 the string will tell that the toolcard request must be end
     * - if the mode's id is 2 the string will tell that it is not possible to cancel the toolcard for the player
     * - if the mode's id is 3 the string will tell that it is not possible to cancel the action because of an
     *      internal problem
     */
    @Override
    public String getMessage() {
        switch (mode) {
            case 0:
                return ExceptionConstants.CANNOT_CANCEL_ACTION_0 + user;
            case 1:
                return ExceptionConstants.CANNOT_CANCEL_ACTION_1_P1
                        + user + ExceptionConstants.CANNOT_CANCEL_ACTION_1_P2 + cardID +
                        ExceptionConstants.CANNOT_CANCEL_ACTION_1_P3;
            case 2:
                return  ExceptionConstants.CANNOT_CANCEL_ACTION_2_P1 + cardID +
                        ExceptionConstants.CANNOT_CANCEL_ACTION_2_P2 + user;
            case 3:
                return  ExceptionConstants.CANNOT_CANCEL_ACTION_3_P1 + user +
                        ExceptionConstants.CANNOT_CANCEL_ACTION_3_P2 + cardID;
            default:
                break;
        }
        return null;
    }


}
