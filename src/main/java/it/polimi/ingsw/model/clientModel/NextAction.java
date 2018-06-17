package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

public enum NextAction implements Serializable {
    PLACE_DICE_TOOLCARD,
    INTERRUPT_TOOLCARD,
    CANCEL_ACTION_TOOLCARD,
    SELECT_DICE_TOOLCARD,
    SELECT_NUMBER_TOOLCARD,
    MENU_ALL,
    MENU_ONLY_PLACE_DICE,
    MENU_ONLY_TOOLCARD,
    MENU_ONLY_ENDTURN,
    SELECT_DICE_TO_ACTIVATE_TOOLCARD,
    WAIT_FOR_TURN;
}
