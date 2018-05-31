package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

public enum ClientNextActions implements Serializable {
    PLACE_DICE,
    PLACE_DICE_TOOLCARD,
    STOP_TOOL_CARD_QUESTION,
    PICK_DICE_TOOLCARD,
    PICK_NUMBER_TOOLCARD,
    MOVEFINISHED,
    MENU_ALL,
    MENU_ONLY_PICKDICE,
    MENU_ONLY_TOOLCARD,
    TURN_FINISHED,
    PICKDICE_SINGLEPLAYER_ENABLECARD;

}
