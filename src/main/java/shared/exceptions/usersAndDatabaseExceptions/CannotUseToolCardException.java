package shared.exceptions.usersAndDatabaseExceptions;

import static shared.constants.ExceptionConstants.*;

public class CannotUseToolCardException extends Exception{
    private int cause;
    private String cardID;

    public CannotUseToolCardException(String card, int cause) {
        this.cause=cause;
        this.cardID = card;
    }
    @Override
    public String getMessage() {
        if (cause==0)
        return CANNOT_PICK_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_0;
        else if (cause==1)
            return CANNOT_PICK_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_1;
        else if (cause==2)
            return CANNOT_PICK_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_2;
        else if (cause==3)
            return CANNOT_USE_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_3;
        else if (cause==4 || cause==5)
            return CANNOT_PICK_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_45;
        else if (cause==6)
            return CANNOT_USE_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_6;
        else if (cause==7)
            return CANNOT_USE_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_7;
        else if (cause==8)
            return CANNOT_USE_TOOLCARD + cardID + CANNOT_USE_TOOLCARD_8;
        else if (cause ==9)
            return CANNOT_USE_TOOLCARD + cardID + ".";
        else return CANNOT_USE_TOOLCARD_DEF + cardID + ".";
    }


}
