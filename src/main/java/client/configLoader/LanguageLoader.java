package client.configLoader;

import client.constants.CliConstants;
import client.constants.CliRenderConstants;
import client.constants.GuiConstants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import static client.constants.CliConstants.*;
import static client.constants.CliRenderConstants.*;
import static client.constants.GuiConstants.*;

public class LanguageLoader {

    public static void loadConfig(JSONObject jsonLanguage){
        JSONArray array;
        String p1, p2, p3, p4;

        YES_RESPONSE = (String) jsonLanguage.get("YES_RESPONSE");
        NO_RESPONSE = (String) jsonLanguage.get("NO_RESPONSE");
        ESCAPE_RESPONSE = (String) jsonLanguage.get("ESCAPE_RESPONSE");
        QUIT_RESPONSE = (String) jsonLanguage.get("QUIT_RESPONSE");
        SOCKET_OR_RMI = (String) jsonLanguage.get("SOCKET_OR_RMI");

        array = (JSONArray) jsonLanguage.get("SOCKET_RESPONSES");
        SOCKET_RESPONSES = new ArrayList<>();
        for(Object str : array) SOCKET_RESPONSES.add((String) str);

        array = (JSONArray) jsonLanguage.get("RMI_RESPONSES");
        RMI_RESPONSES = new ArrayList<>();
        for(Object str : array) RMI_RESPONSES.add((String) str);

        array = (JSONArray) jsonLanguage.get("QUIT_RESPONSES");
        QUIT_RESPONSES = new ArrayList<>();
        QUIT_RESPONSES.add(QUIT_RESPONSE);
        for(Object str : array) QUIT_RESPONSES.add((String) str);

        p1 = (String) jsonLanguage.get("INSTRUCTION_NOT_RECOGNIZED_P1");
        p2 = (String) jsonLanguage.get("INSTRUCTION_NOT_RECOGNIZED_P2");
        p3 = (String) jsonLanguage.get("INSTRUCTION_NOT_RECOGNIZED_P3");
        INSTRUCTION_NOT_RECOGNIZED = p1 + SOCKET_RESPONSES.get(0) + p2 + RMI_RESPONSES.get(0) + p3;

        CANNOT_CONNECT_WITH_SOCKET_SERVER = (String) jsonLanguage.get("CANNOT_CONNECT_WITH_SOCKET_SERVER");
        CANNOT_CONNECT_WITH_RMI_SERVER = (String) jsonLanguage.get("CANNOT_CONNECT_WITH_RMI_SERVER");
        VOID_STRING = (String) jsonLanguage.get("VOID_STRING");
        TECHNOLOGY_NOT_SUPPORTED_P1 = (String) jsonLanguage.get("TECHNOLOGY_NOT_SUPPORTED_P1");
        TECHNOLOGY_NOT_SUPPORTED_P2 = (String) jsonLanguage.get("TECHNOLOGY_NOT_SUPPORTED_P2");
        ENTER = (String) jsonLanguage.get("ENTER");
        PRESENT_RETURN_BACK = ENTER + ESCAPE_RESPONSE + jsonLanguage.get("PRESENT_RETURN_BACK");
        PRESENT_QUIT = ENTER + QUIT_RESPONSE + jsonLanguage.get("PRESENT_QUIT");

        p1 = (String) jsonLanguage.get("INSERT_YES_OR_NO_P1");
        p2 = (String) jsonLanguage.get("INSERT_YES_OR_NO_P2");
        p3 = (String) jsonLanguage.get("INSERT_YES_OR_NO_P3");
        INSERT_YES_OR_NO = p1 + YES_RESPONSE + p2 + NO_RESPONSE + p3;

        p1 = (String) jsonLanguage.get("CHOOSE_LOG_TYPE_P1");
        p2 = (String) jsonLanguage.get("CHOOSE_LOG_TYPE_P2");
        CHOOSE_LOG_TYPE = p1 + YES_RESPONSE + p2 + NO_RESPONSE + PRESENT_QUIT;

        p1 = (String) jsonLanguage.get("CHOOSE_LOG_TYPE_ERROR_P1");
        p2 = (String) jsonLanguage.get("CHOOSE_LOG_TYPE_ERROR_P2");
        CHOOSE_LOG_TYPE_ERROR = p1 + INSERT_YES_OR_NO + p2;

        CREATE_USER_PHASE = (String) jsonLanguage.get("CREATE_USER_PHASE");
        LOGIN_PHASE = (String) jsonLanguage.get("LOGIN_PHASE");

        p1 = (String) jsonLanguage.get("LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE_P1");
        p2 = (String) jsonLanguage.get("LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE_P2");
        p3 = (String) jsonLanguage.get("LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE_P3");
        p4 = (String) jsonLanguage.get("LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE_P4");
        LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE = p1 + QUIT_RESPONSE + p2 + YES_RESPONSE + p3 + NO_RESPONSE + p4;

        INSERT_USERNAME = (String) jsonLanguage.get("INSERT_USERNAME");
        INSERT_PASS = (String) jsonLanguage.get("INSERT_PASS");
        CREATE_USER_ERROR = (String) jsonLanguage.get("CREATE_USER_ERROR");
        LOG_SUCCESS = (String) jsonLanguage.get("LOG_SUCCESS");
        LOGIN_ERROR = (String) jsonLanguage.get("LOGIN_ERROR");
        PRESENT_MAIN_MENU = (String) jsonLanguage.get("PRESENT_MAIN_MENU");
        SELECT_NUM_PLAYERS = (String) jsonLanguage.get("SELECT_NUM_PLAYERS");
        PRESS_ENTER_TO_CONTINUE = (String) jsonLanguage.get("PRESS_ENTER_TO_CONTINUE");
        PRESS_ENTER_TO_MENU = (String) jsonLanguage.get("PRESS_ENTER_TO_MENU");
        NUM_PLAYERS_ERROR = (String) jsonLanguage.get("NUM_PLAYERS_ERROR");
        RECONNECTED_TO_GAME = (String) jsonLanguage.get("RECONNECTED_TO_GAME");
        WERE_PLACING_DICE_STRING = (String) jsonLanguage.get("WERE_PLACING_DICE_STRING");
        DICE_COME_FROM = (String) jsonLanguage.get("DICE_COME_FROM");
        DICE_SHOULD_BE_PUT = (String) jsonLanguage.get("DICE_SHOULD_BE_PUT");
        USERNAME = (String) jsonLanguage.get("USERNAME");
        WON_GAMES = (String) jsonLanguage.get("WON_GAMES");
        LOST_GAMES = (String) jsonLanguage.get("LOST_GAMES");
        ABANDONED_GAMES = (String) jsonLanguage.get("ABANDONED_GAMES");
        RANKING = (String) jsonLanguage.get("RANKING");
        IMPOSSIBLE_TO_FIND_GAME = (String) jsonLanguage.get("IMPOSSIBLE_TO_FIND_GAME");
        INSERT_INT_NUMBER = (String) jsonLanguage.get("INSERT_INT_NUMBER");
        WAITING_PLAYERS = (String) jsonLanguage.get("WAITING_PLAYERS");
        WAITING_GAME = (String) jsonLanguage.get("WAITING_GAME");
        WAITING_PRIVATE_OBJECTIVE = (String) jsonLanguage.get("WAITING_PRIVATE_OBJECTIVE");
        WAITING_WPCS = (String) jsonLanguage.get("WAITING_WPCS");
        WAITING_TOOLCARD = (String) jsonLanguage.get("WAITING_TOOLCARD");
        WAITING_POCS = (String) jsonLanguage.get("WAITING_POCS");
        WAITING_TURN = (String) jsonLanguage.get("WAITING_TURN");
        SELECT_WPC = (String) jsonLanguage.get("SELECT_WPC");
        WAITING_PLAYERS_TO_SELECT_WPC = (String) jsonLanguage.get("WAITING_PLAYERS_TO_SELECT_WPC");
        TIMES_UP = (String) jsonLanguage.get("TIMES_UP");
        PRESENT_WPC = (String) jsonLanguage.get("PRESENT_WPC");
        FAVOURS_LEFT = (String) jsonLanguage.get("FAVOURS_LEFT");
        EXTRACTED_DICES = (String) jsonLanguage.get("EXTRACTED_DICES");
        RETURN = (String) jsonLanguage.get("RETURN");
        SHOW_PRIVATE_OBJECTIVE = (String) jsonLanguage.get("SHOW_PRIVATE_OBJECTIVE");
        SHOW_POCS = (String) jsonLanguage.get("SHOW_POCS");
        SHOW_TOOLCARDS = (String) jsonLanguage.get("SHOW_TOOLCARDS");
        SHOW_ROUNDTRACK = (String) jsonLanguage.get("SHOW_ROUNDTRACK");
        SHOW_ALL_WPCS = (String) jsonLanguage.get("SHOW_ALL_WPCS");
        TIME_LEFT_TO_CHOOSE_WPC_P1 = (String) jsonLanguage.get("TIME_LEFT_TO_CHOOSE_WPC_P1");
        TIME_LEFT_TO_CHOOSE_WPC_P2 = (String) jsonLanguage.get("TIME_LEFT_TO_CHOOSE_WPC_P2");
        ASK_WHAT_TO_DO = (String) jsonLanguage.get("ASK_WHAT_TO_DO");
        PLACE_DICE = (String) jsonLanguage.get("PLACE_DICE");
        USE_TOOLCARD = (String) jsonLanguage.get("USE_TOOLCARD");
        CLOSE_PARENTHESIS = (String) jsonLanguage.get("CLOSE_PARENTHESIS");
        END_TURN = (String) jsonLanguage.get("END_TURN");
        COMMAND_NOT_RECOGNIZED = (String) jsonLanguage.get("COMMAND_NOT_RECOGNIZED");
        INSERT_ACTION_NUMBER = (String) jsonLanguage.get("INSERT_ACTION_NUMBER");
        NO_STANDARD_ACTION_PASSED = (String) jsonLanguage.get("NO_STANDARD_ACTION_PASSED");
        PRESENT_PRIVATE_OBJS = (String) jsonLanguage.get("PRESENT_PRIVATE_OBJS");
        PRESENT_PRIVATE_OBJ = (String) jsonLanguage.get("PRESENT_PRIVATE_OBJ");
        PRESENT_POCS = (String) jsonLanguage.get("PRESENT_POCS");
        PRESENT_TOOLCARDS = (String) jsonLanguage.get("PRESENT_TOOLCARDS");
        PRESENT_WPCS = (String) jsonLanguage.get("PRESENT_WPCS");
        PRESENT_NEW_DICE = (String) jsonLanguage.get("PRESENT_NEW_DICE");
        ID = (String) jsonLanguage.get("ID");
        NAME = (String) jsonLanguage.get("NAME");
        DESCRIPTION = (String) jsonLanguage.get("DESCRIPTION");
        INSERT_DICE_ID_TO_PLACE = (String) jsonLanguage.get("INSERT_DICE_ID_TO_PLACE");
        INSERT_DICE_ID_TO_USE = (String) jsonLanguage.get("INSERT_DICE_ID_TO_USE");
        INSERT_ROW_TO_PLACE_DICE = (String) jsonLanguage.get("INSERT_ROW_TO_PLACE_DICE");
        INSERT_COL_TO_PLACE_DICE = (String) jsonLanguage.get("INSERT_COL_TO_PLACE_DICE");
        INSERT_DICE_ROW = (String) jsonLanguage.get("INSERT_DICE_ROW");
        INSERT_DICE_COL = (String) jsonLanguage.get("INSERT_DICE_COL");
        DICE_ALREADY_PRESENT = (String) jsonLanguage.get("DICE_ALREADY_PRESENT");
        DICE_NOT_FOUND = (String) jsonLanguage.get("DICE_NOT_FOUND");
        GOING_TO_PLACE_DICE = (String) jsonLanguage.get("GOING_TO_PLACE_DICE");
        GOING_TO_PLACE_DICE_INTO_DICEBAG = (String) jsonLanguage.get("GOING_TO_PLACE_DICE_INTO_DICEBAG");
        INCREMENT_DECREMENT_DICE = (String) jsonLanguage.get("INCREMENT_DECREMENT_DICE");
        SELECT_TOOLCARD_ID = (String) jsonLanguage.get("SELECT_TOOLCARD_ID");
        CANT_DO_PICK_FROM_THIS_POSITION = (String) jsonLanguage.get("CANT_DO_PICK_FROM_THIS_POSITION");
        SELECT_DICE_FROM_WPC = (String) jsonLanguage.get("SELECT_DICE_FROM_WPC");
        INSERT_NUMBER_FROM_PRESENTED = (String) jsonLanguage.get("INSERT_NUMBER_FROM_PRESENTED");
        ALL_PLAYERS_CHOOSE_WPC = (String) jsonLanguage.get("ALL_PLAYERS_CHOOSE_WPC");
        PLAYER_CHOSE_WPC = (String) jsonLanguage.get("PLAYER_CHOSE_WPC");
        GAME_STARTED = (String) jsonLanguage.get("GAME_STARTED");
        PLAYER_ENTER_GAME = (String) jsonLanguage.get("PLAYER_ENTER_GAME");
        PLAYER_EXIT_GAME = (String) jsonLanguage.get("PLAYER_EXIT_GAME");
        IN_GAME_PLAYERS = (String) jsonLanguage.get("IN_GAME_PLAYERS");
        OF = (String) jsonLanguage.get("OF");
        NEEDED = (String) jsonLanguage.get("NEEDED");
        TURN = (String) jsonLanguage.get("TURN");
        CliConstants.ROUND = (String) jsonLanguage.get("ROUND");
        ACTIVE_PLAYER = (String) jsonLanguage.get("ACTIVE_PLAYER");
        PLAYER_PLACED_DICE = (String) jsonLanguage.get("PLAYER_PLACED_DICE");
        IN_POSITION = (String) jsonLanguage.get("IN_POSITION");
        PLAYER_USED_TOOLCARD = (String) jsonLanguage.get("PLAYER_USED_TOOLCARD");
        CHANGED_DICE = (String) jsonLanguage.get("CHANGED_DICE");
        INTO_DICE = (String) jsonLanguage.get("INTO_DICE");
        POSITION = (String) jsonLanguage.get("POSITION");
        PLAYER_REPLACED_DICE = (String) jsonLanguage.get("PLAYER_REPLACED_DICE");
        PLACED_IN = (String) jsonLanguage.get("PLACED_IN");
        WITH_DICE = (String) jsonLanguage.get("WITH_DICE");
        EXTRACTED_DICES_REPLACED = (String) jsonLanguage.get("EXTRACTED_DICES_REPLACED");
        PLAYER_DISCONNECTED = (String) jsonLanguage.get("PLAYER_DISCONNECTED");
        PLAYER_RECONNECTED = (String) jsonLanguage.get("PLAYER_RECONNECTED");
        LOST_CONNECTION = (String) jsonLanguage.get("LOST_CONNECTION");
        LOGIN_FROM_ANOTHER_DEVICE = (String) jsonLanguage.get("LOGIN_FROM_ANOTHER_DEVICE");
        YOU_HAVE_BEEN_DISCONNECTED = (String) jsonLanguage.get("YOU_HAVE_BEEN_DISCONNECTED");
        TIME_TO_WAIT_PLAYER_ENDED = (String) jsonLanguage.get("TIME_TO_WAIT_PLAYER_ENDED");
        POINTS = (String) jsonLanguage.get("POINTS");
        PLAYER_SKIPPED_TURN = (String) jsonLanguage.get("PLAYER_SKIPPED_TURN");
        PLAYER_SKIPPED_TURN_DUE_TO_TOOLCARD = (String) jsonLanguage.get("PLAYER_SKIPPED_TURN_DUE_TO_TOOLCARD");
        GAME_ENDED = (String) jsonLanguage.get("GAME_ENDED");
        FINAL_TABLE = (String) jsonLanguage.get("FINAL_TABLE");

        CliRenderConstants.FAVOURS = (String) jsonLanguage.get("FAVOURS");
        RESET = "\033" + jsonLanguage.get("RESET");
        BLACK = "\033" + jsonLanguage.get("BLACK");
        BLACK_BRIGHT = "\033" + jsonLanguage.get("BLACK_BRIGHT");
        RED = "\033" + jsonLanguage.get("RED");
        GREEN = "\033" + jsonLanguage.get("GREEN");
        YELLOW = "\033" + jsonLanguage.get("YELLOW");
        BLUE = "\033" + jsonLanguage.get("BLUE");
        PURPLE = "\033" + jsonLanguage.get("PURPLE");
        RED_BACKGROUND = "\033" + jsonLanguage.get("RED_BACKGROUND");
        GREEN_BACKGROUND = "\033" + jsonLanguage.get("GREEN_BACKGROUND");
        YELLOW_BACKGROUND = "\033" + jsonLanguage.get("YELLOW_BACKGROUND");
        BLUE_BACKGROUND = "\033" + jsonLanguage.get("BLUE_BACKGROUND");
        PURPLE_BACKGROUND = "\033" + jsonLanguage.get("PURPLE_BACKGROUND");

        IMPOSSIBLE_RMI_CONNECTION = (String) jsonLanguage.get("IMPOSSIBLE_RMI_CONNECTION");
        IMPOSSIBLE_SOCKET_CONNECTION = (String) jsonLanguage.get("IMPOSSIBLE_SOCKET_CONNECTION");
        SELECT_NUMBER_ERROR = (String) jsonLanguage.get("SELECT_NUMBER_ERROR");
        ENTRANCE_IN_GAME = (String) jsonLanguage.get("ENTRANCE_IN_GAME");
        PLAYERS_IN_GAME = (String) jsonLanguage.get("PLAYERS_IN_GAME");
        PREPOSITION = (String) jsonLanguage.get("PREPOSITION");
        NECESSARY = (String) jsonLanguage.get("NECESSARY");
        ENTRANCE_NEW_PLAYER = (String) jsonLanguage.get("ENTRANCE_NEW_PLAYER");
        EXIT_PLAYER = (String) jsonLanguage.get("EXIT_PLAYER");
        DISCONNECTION_OF_PLAYER = (String) jsonLanguage.get("DISCONNECTION_OF_PLAYER");
        RECONNECTION_OF_PLAYER = (String) jsonLanguage.get("RECONNECTION_OF_PLAYER");
        ANIMATION_DICE = (String) jsonLanguage.get("ANIMATION_DICE");
        PRIVATE_OBJ_IDENTIFIER_CSS = (String) jsonLanguage.get("PRIVATE_OBJ_IDENTIFIER_CSS");
        TOOL_IDENTIFIER_CSS = (String) jsonLanguage.get("TOOL_IDENTIFIER_CSS");
        POC_IDENTIFIER_CSS = (String) jsonLanguage.get("POC_IDENTIFIER_CSS");
        DEFAULT_CELL_COLOR = (String) jsonLanguage.get("DEFAULT_CELL_COLOR");
        NUMBER_IDENTIFIER_CSS = (String) jsonLanguage.get("NUMBER_IDENTIFIER_CSS");
        CARD_STYLE = (String) jsonLanguage.get("CARD_STYLE");
        MINUTES_TIMER = (String) jsonLanguage.get("MINUTES_TIMER");
        MINUTES_SECONDS_TIMER = (String) jsonLanguage.get("MINUTES_SECONDS_TIMER");
        END_TIMER = (String) jsonLanguage.get("END_TIMER");
        YOUR_SCHEMA_CHOICE = (String) jsonLanguage.get("YOUR_SCHEMA_CHOICE");
        OTHERS_SCHEMA_CHOICE = (String) jsonLanguage.get("OTHERS_SCHEMA_CHOICE");
        USING_TOOLCARD = (String) jsonLanguage.get("USING_TOOLCARD");
        USED_TOOLCARD = (String) jsonLanguage.get("USED_TOOLCARD");
        GuiConstants.FAVOURS = (String) jsonLanguage.get("GUI_FAVOURS");
        GuiConstants.ROUND = (String) jsonLanguage.get("GUI_ROUND");
        WAIT_TURN = (String) jsonLanguage.get("WAIT_TURN");
        YOUR_TURN = (String) jsonLanguage.get("YOUR_TURN");
        TURN_OF = (String) jsonLanguage.get("TURN_OF");
        SKIP_TURN = (String) jsonLanguage.get("SKIP_TURN");
        USE_TOOL_OR_END_TURN = (String) jsonLanguage.get("USE_TOOL_OR_END_TURN");
        PLACE_DICE_OR_END_TURN = (String) jsonLanguage.get("PLACE_DICE_OR_END_TURN");
        ONLY_END_TURN = (String) jsonLanguage.get("ONLY_END_TURN");
        PLACE_DICE_FROM_WPC_TO_WPC = (String) jsonLanguage.get("PLACE_DICE_FROM_WPC_TO_WPC");
        PLACE_DICE_FROM_EXTRACTED_TO_DICEBAG = (String) jsonLanguage.get("PLACE_DICE_FROM_EXTRACTED_TO_DICEBAG");
        PLACE_DICE_FROM_EXTRACTED_TO_WPC = (String) jsonLanguage.get("PLACE_DICE_FROM_EXTRACTED_TO_WPC");
        PICK_DICE_FROM_WPC = (String) jsonLanguage.get("PICK_DICE_FROM_WPC");
        PICK_DICE_FROM_EXTRACTED = (String) jsonLanguage.get("PICK_DICE_FROM_EXTRACTED");
        PICK_DICE_FROM_ROUNDTRACK = (String) jsonLanguage.get("PICK_DICE_FROM_ROUNDTRACK");
        ACTIVE_TOOLCARD_SINGLE_PLAYER = (String) jsonLanguage.get("ACTIVE_TOOLCARD_SINGLE_PLAYER");
        ADD_SUBTRACT_ONE = (String) jsonLanguage.get("ADD_SUBTRACT_ONE");
        CANCEL_TOOLCARD = (String) jsonLanguage.get("CANCEL_TOOLCARD");
        TOOLCARD_BLOCKED = (String) jsonLanguage.get("TOOLCARD_BLOCKED");
        YES_VALUE = (String) jsonLanguage.get("YES_VALUE");
        NO_VALUE = (String) jsonLanguage.get("NO_VALUE");
        OK_VALUE = (String) jsonLanguage.get("OK_VALUE");
        BACK_VALUE = (String) jsonLanguage.get("BACK_VALUE");
        GAME_LOST = (String) jsonLanguage.get("GAME_LOST");
        GAME_WON = (String) jsonLanguage.get("GAME_WON");
    }
}
