package client.constants;

import client.configLoader.ClientConfigLoader;
import org.json.simple.JSONObject;

/**
 * Contains all constants related to the gui class
 */
public class GuiConstants {
    private static final JSONObject jsonLanguage = ClientConfigLoader.jsonClientLanguage;

    /*
    Constants for the StartController class
     */
    public static final String IMPOSSIBLE_RMI_CONNECTION = (String) jsonLanguage.get("IMPOSSIBLE_RMI_CONNECTION");
    public static final String IMPOSSIBLE_SOCKET_CONNECTION = (String) jsonLanguage.get("IMPOSSIBLE_SOCKET_CONNECTION");

     /*
    Constants for the SetNewGameController class
     */

    //entrance in a game
    public static final String SELECT_NUMBER_ERROR = (String) jsonLanguage.get("SELECT_NUMBER_ERROR");
    public static final String ENTRANCE_IN_GAME = (String) jsonLanguage.get("ENTRANCE_IN_GAME");
    public static final String PLAYERS_IN_GAME = (String) jsonLanguage.get("PLAYERS_IN_GAME");
    public static final String PREPOSITION = (String) jsonLanguage.get("PREPOSITION");
    public static final String NECESSARY = (String) jsonLanguage.get("NECESSARY");
    public static final String ENTRANCE_NEW_PLAYER = (String) jsonLanguage.get("ENTRANCE_NEW_PLAYER");

    //EXIT from game, disconnection and reconnection
    public static final String EXIT_PLAYER = (String) jsonLanguage.get("EXIT_PLAYER");
    public static final String DISCONNECTION_OF_PLAYER = (String) jsonLanguage.get("DISCONNECTION_OF_PLAYER");
    public static final String RECONNECTION_OF_PLAYER = (String) jsonLanguage.get("RECONNECTION_OF_PLAYER");

    public static final String ANIMATION_DICE = (String) jsonLanguage.get("ANIMATION_DICE");
    public static final String PRIVATE_OBJ_IDENTIFIER_CSS = (String) jsonLanguage.get("PRIVATE_OBJ_IDENTIFIER_CSS");
    public static final String TOOL_IDENTIFIER_CSS = (String) jsonLanguage.get("TOOL_IDENTIFIER_CSS");
    public static final String POC_IDENTIFIER_CSS = (String) jsonLanguage.get("POC_IDENTIFIER_CSS");
    public static final String DEFAULT_CELL_COLOR = (String) jsonLanguage.get("DEFAULT_CELL_COLOR");
    public static final String NUMBER_IDENTIFIER_CSS = (String) jsonLanguage.get("NUMBER_IDENTIFIER_CSS");
    public static final String CARD_STYLE = (String) jsonLanguage.get("CARD_STYLE");

    //timer constants
    public static final String MINUTES_TIMER = (String) jsonLanguage.get("MINUTES_TIMER");
    public static final String MINUTES_SECONDS_TIMER = (String) jsonLanguage.get("MINUTES_SECONDS_TIMER");
    public static final String END_TIMER = (String) jsonLanguage.get("END_TIMER");


    //schemas constants
    public static final String YOUR_SCHEMA_CHOICE = (String) jsonLanguage.get("YOUR_SCHEMA_CHOICE");
    public static final String OTHERS_SCHEMA_CHOICE = (String) jsonLanguage.get("OTHERS_SCHEMA_CHOICE");

    public static final String USING_TOOLCARD = (String) jsonLanguage.get("USING_TOOLCARD");
    public static final String USED_TOOLCARD = (String) jsonLanguage.get("USED_TOOLCARD");
    public static final String FAVOURS = (String) jsonLanguage.get("GUI_FAVOURS");

    //turn and rund constrants
    public static final String ROUND = (String) jsonLanguage.get("GUI_ROUND");
    public static final String WAIT_TURN = (String) jsonLanguage.get("WAIT_TURN");
    public static final String YOUR_TURN = (String) jsonLanguage.get("YOUR_TURN");
    public static final String TURN_OF = (String) jsonLanguage.get("TURN_OF");
    public static final String SKIP_TURN = (String) jsonLanguage.get("SKIP_TURN");

    public static final String USE_TOOL_OR_END_TURN = (String) jsonLanguage.get("USE_TOOL_OR_END_TURN");
    public static final String PLACE_DICE_OR_END_TURN = (String) jsonLanguage.get("PLACE_DICE_OR_END_TURN");
    public static final String ONLY_END_TURN = (String) jsonLanguage.get("ONLY_END_TURN");


    public static final String PLACE_DICE_FROM_WPC_TO_WPC = (String) jsonLanguage.get("PLACE_DICE_FROM_WPC_TO_WPC");
    public static final String PLACE_DICE_FROM_EXTRACTED_TO_DICEBAG = (String) jsonLanguage.get("PLACE_DICE_FROM_EXTRACTED_TO_DICEBAG");
    public static final String PLACE_DICE_FROM_EXTRACTED_TO_WPC = (String) jsonLanguage.get("PLACE_DICE_FROM_EXTRACTED_TO_WPC");

    public static final String PICK_DICE_FROM_WPC = (String) jsonLanguage.get("PICK_DICE_FROM_WPC");
    public static final String PICK_DICE_FROM_EXTRACTED = (String) jsonLanguage.get("PICK_DICE_FROM_EXTRACTED");
    public static final String PICK_DICE_FROM_ROUNDTRACK = (String) jsonLanguage.get("PICK_DICE_FROM_ROUNDTRACK");
    public static final String ACTIVE_TOOLCARD_SINGLE_PLAYER = (String) jsonLanguage.get("ACTIVE_TOOLCARD_SINGLE_PLAYER");

    public static final String ADD_SUBTRACT_ONE = (String) jsonLanguage.get("ADD_SUBTRACT_ONE");
    public static final String CANCEL_TOOLCARD = (String) jsonLanguage.get("CANCEL_TOOLCARD");

    //possible values when the toolcard is interrupted
    public static final String TOOLCARD_BLOCKED = (String) jsonLanguage.get("TOOLCARD_BLOCKED");
    public static final String YES_VALUE = (String) jsonLanguage.get("YES_VALUE");
    public static final String NO_VALUE = (String) jsonLanguage.get("NO_VALUE");
    public static final String OK_VALUE = (String) jsonLanguage.get("OK_VALUE");
    public static final String BACK_VALUE = (String) jsonLanguage.get("BACK_VALUE");

    public static final String GAME_LOST = (String) jsonLanguage.get("GAME_LOST");
    public static final String GAME_WON = (String) jsonLanguage.get("GAME_WON");
}
