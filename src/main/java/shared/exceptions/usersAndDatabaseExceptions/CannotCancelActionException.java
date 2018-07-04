package shared.exceptions.usersAndDatabaseExceptions;

import shared.constants.ExceptionConstants;

public class CannotCancelActionException extends Exception{
    private int mode;
    //0=action normale 1=azione dentro la toolcard 2=la toolcard stessa
    private String user ;
    private String cardID;


    public CannotCancelActionException(String user){
        this.user=user;
        this.cardID=null;
        this.mode=0;
    }
    public CannotCancelActionException(String user, String card, int mode) {
        this.user=user;
        this.cardID = card;
        this.mode=mode;
    }

    @Override
    public String getMessage() {
        String temp;
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

        }
        return null;
    }


}
