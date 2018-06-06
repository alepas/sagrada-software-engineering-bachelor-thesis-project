package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.clientModel.NextAction;

public enum CliStatus {
    //BEFORE-GAME
    LOG_PHASE,
    LOGIN,
    CREATE_ACCOUNT,
    MAIN_MENU_PHASE,
    FIND_GAME_PHASE,
    DISPLAY_STAT,
    LOGOUT,
    START_GAME_PHASE,
    QUIT_SAGRADA,
    UNKNOWN,


    //IN-GAME
    PLACE_DICE,
    PLACE_DICE_TOOLCARD,
    INTERRUPT_TOOLCARD,
    SELECT_DICE_TOOLCARD,
    SELECT_NUMBER_TOOLCARD,
    MENU_ALL,
    MENU_ONLY_PLACEDICE,
    MENU_ONLY_TOOLCARD,
    MENU_ONLY_ENDTURN,
    SELECT_DICE_TO_ACTIVE_TOOLCARD,
    ANOTHER_PLAYER_TURN;

    public CliStatus change(NextAction nextAction){
        switch (nextAction){
            case PLACE_DICE:
                return PLACE_DICE;
            case PLACE_DICE_TOOLCARD:
                return PLACE_DICE_TOOLCARD;
            case INTERRUPT_TOOLCARD:
                return INTERRUPT_TOOLCARD;
            case SELECT_DICE_TOOLCARD:
                return SELECT_DICE_TOOLCARD;
            case SELECT_NUMBER_TOOLCARD:
                return SELECT_NUMBER_TOOLCARD;
            case MENU_ALL:
                return MENU_ALL;
            case MENU_ONLY_PLACE_DICE:
                return MENU_ONLY_PLACEDICE;
            case MENU_ONLY_TOOLCARD:
                return MENU_ONLY_TOOLCARD;
            case MENU_ONLY_ENDTURN:
                return MENU_ONLY_ENDTURN;
            case SELECT_DICE_TO_ACTIVATE_TOOLCARD:
                return SELECT_DICE_TO_ACTIVE_TOOLCARD;
            case WAIT_FOR_TURN:
                return ANOTHER_PLAYER_TURN;
        }
        return null;
    }

    public CliStatus getPrevious(){
        //TODO: implementare back di LogPhase, MAIN_MENU_PHASE, altri mancanti
        switch (this){
            case LOG_PHASE: case LOGOUT:
                //TODO
                return LOG_PHASE;
            case LOGIN: case CREATE_ACCOUNT: case MAIN_MENU_PHASE:
                return LOG_PHASE;
            case FIND_GAME_PHASE: case DISPLAY_STAT:
                return MAIN_MENU_PHASE;
            default:
                return this;
        }
    }
}
