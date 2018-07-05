package client.constants;

import client.configLoader.ClientConfigLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all constants related to the the cli
 */
public class CliConstants {
    private static final JSONObject jsonClientObject = ClientConfigLoader.jsonClientObject;
    private static final JSONObject jsonLanguage = ClientConfigLoader.jsonClientLanguage;

    public static final int COUNTER_START_NUMBER = ((Long) jsonClientObject.get("COUNTER_START_NUMBER")).intValue();

    public static final int WPC_SPACING = ((Long) jsonClientObject.get("WPC_SPACING")).intValue();
    public static final int[] WPC_WAITING_STEPS = getSteps((JSONArray) jsonClientObject.get("WPC_WAITING_STEPS"));

    public static final String YES_RESPONSE = (String) jsonLanguage.get("YES_RESPONSE");
    public static final String NO_RESPONSE = (String) jsonLanguage.get("NO_RESPONSE");
    public static final String ESCAPE_RESPONSE = (String) jsonLanguage.get("ESCAPE_RESPONSE");
    public static final String QUIT_RESPONSE = (String) jsonLanguage.get("QUIT_RESPONSE");

    public static final List<String> CLI_RESPONSES = getArrayElements((JSONArray) jsonLanguage.get("CLI_RESPONSES"));
    public static final List<String> GUI_RESPONSES = getArrayElements((JSONArray) jsonLanguage.get("GUI_RESPONSES"));

    public static final String SOCKET_OR_RMI = (String) jsonLanguage.get("SOCKET_OR_RMI");
    public static final List<String> SOCKET_RESPONSES = getArrayElements((JSONArray) jsonLanguage.get("SOCKET_RESPONSES"));

    public static final List<String> RMI_RESPONSES = getArrayElements((JSONArray) jsonLanguage.get("RMI_RESPONSES"));
    public static final List<String> QUIT_RESPONSES = getQuitResponses((JSONArray) jsonLanguage.get("QUIT_RESPONSES"));

    public static final String INSTRUCTION_NOT_RECOGNIZED = jsonLanguage.get("INSTRUCTION_NOT_RECOGNIZED_P1")
            + SOCKET_RESPONSES.get(0) + jsonLanguage.get("INSTRUCTION_NOT_RECOGNIZED_P2") +
            RMI_RESPONSES.get(0) + jsonLanguage.get("INSTRUCTION_NOT_RECOGNIZED_P3");

    public static final String CANNOT_CONNECT_WITH_SOCKET_SERVER = (String) jsonLanguage.get("CANNOT_CONNECT_WITH_SOCKET_SERVER");
    public static final String CANNOT_CONNECT_WITH_RMI_SERVER = (String) jsonLanguage.get("CANNOT_CONNECT_WITH_RMI_SERVER");
    public static final String VOID_STRING = (String) jsonLanguage.get("VOID_STRING");

    public static final String TECHNOLOGY_NOT_SUPPORTED_P1 = (String) jsonLanguage.get("TECHNOLOGY_NOT_SUPPORTED_P1");
    public static final String TECHNOLOGY_NOT_SUPPORTED_P2 = (String) jsonLanguage.get("TECHNOLOGY_NOT_SUPPORTED_P2");

    private static final String ENTER = (String) jsonLanguage.get("ENTER");
    public static final String PRESENT_RETURN_BACK = ENTER + ESCAPE_RESPONSE + jsonLanguage.get("PRESENT_RETURN_BACK");
    public static final String PRESENT_QUIT = ENTER + QUIT_RESPONSE + jsonLanguage.get("PRESENT_QUIT");
    public static final String INSERT_YES_OR_NO = jsonLanguage.get("INSERT_YES_OR_NO_P1") + YES_RESPONSE +
            jsonLanguage.get("INSERT_YES_OR_NO_P2") + NO_RESPONSE + jsonLanguage.get("INSERT_YES_OR_NO_P3");

    public static final String CLI_OR_GUI = (String) jsonLanguage.get("CLI_OR_GUI") + PRESENT_QUIT;

    public static final String ENTER_GAME = (String) jsonLanguage.get("ENTER_GAME");
    public static final String PLAYER_CONNECTED = (String) jsonLanguage.get("PLAYER_CONNECTED");

    public static final String CHOOSE_LOG_TYPE = jsonLanguage.get("CHOOSE_LOG_TYPE_P1") + YES_RESPONSE +
            jsonLanguage.get("CHOOSE_LOG_TYPE_P2") + NO_RESPONSE + PRESENT_QUIT;

    public static final String CHOOSE_LOG_TYPE_ERROR = jsonLanguage.get("CHOOSE_LOG_TYPE_ERROR_P1") +
            INSERT_YES_OR_NO + jsonLanguage.get("CHOOSE_LOG_TYPE_ERROR_P2");

    public static final String CREATE_USER_PHASE = (String) jsonLanguage.get("CREATE_USER_PHASE");
    public static final String LOGIN_PHASE = (String) jsonLanguage.get("LOGIN_PHASE");
    public static final String LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE = jsonLanguage.get("LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE_P1")
            + QUIT_RESPONSE + jsonLanguage.get("LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE_P2") + YES_RESPONSE
            + jsonLanguage.get("LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE_P3") + NO_RESPONSE +
            jsonLanguage.get("LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE_P4");


    public static final String INSERT_USERNAME = (String) jsonLanguage.get("INSERT_USERNAME");
    public static final String INSERT_PASS = (String) jsonLanguage.get("INSERT_PASS");

    public static final String CREATE_USER_ERROR = (String) jsonLanguage.get("CREATE_USER_ERROR");
    public static final String LOGIN_ERROR = (String) jsonLanguage.get("LOGIN_ERROR");

    public static final String LOG_SUCCESS = (String) jsonLanguage.get("LOG_SUCCESS");

    public static final String PRESENT_MAIN_MENU = (String) jsonLanguage.get("PRESENT_MAIN_MENU");

    public static final String SELECT_NUM_PLAYERS = (String) jsonLanguage.get("SELECT_NUM_PLAYERS");

    public static final String PRESS_ENTER_TO_CONTINUE = (String) jsonLanguage.get("PRESS_ENTER_TO_CONTINUE");
    public static final String PRESS_ENTER_TO_MENU = (String) jsonLanguage.get("PRESS_ENTER_TO_MENU");

    public static final String NUM_PLAYERS_ERROR = (String) jsonLanguage.get("NUM_PLAYERS_ERROR");

    public static final String RECONNECTED_TO_GAME = (String) jsonLanguage.get("RECONNECTED_TO_GAME");

    public static final String WERE_PLACING_DICE_STRING = (String) jsonLanguage.get("WERE_PLACING_DICE_STRING");

    public static final String DICE_COME_FROM = (String) jsonLanguage.get("DICE_COME_FROM");

    public static final String DICE_SHOULD_BE_PUT = (String) jsonLanguage.get("DICE_SHOULD_BE_PUT");

    public static final String USERNAME = (String) jsonLanguage.get("USERNAME");
    public static final String WON_GAMES = (String) jsonLanguage.get("WON_GAMES");
    public static final String LOST_GAMES = (String) jsonLanguage.get("LOST_GAMES");
    public static final String ABANDONED_GAMES = (String) jsonLanguage.get("ABANDONED_GAMES");
    public static final String RANKING = (String) jsonLanguage.get("RANKING");

    public static final String IMPOSSIBLE_TO_FIND_GAME = (String) jsonLanguage.get("IMPOSSIBLE_TO_FIND_GAME");
    public static final String INSERT_INT_NUMBER = (String) jsonLanguage.get("INSERT_INT_NUMBER");

    public static final String WAITING_PLAYERS = (String) jsonLanguage.get("WAITING_PLAYERS");
    public static final String WAITING_GAME = (String) jsonLanguage.get("WAITING_GAME");
    public static final String WAITING_PRIVATE_OBJECTIVE = (String) jsonLanguage.get("WAITING_PRIVATE_OBJECTIVE");
    public static final String WAITING_WPCS = (String) jsonLanguage.get("WAITING_WPCS");
    public static final String WAITING_TOOLCARD = (String) jsonLanguage.get("WAITING_TOOLCARD");
    public static final String WAITING_POCS = (String) jsonLanguage.get("WAITING_POCS");
    public static final String WAITING_TURN = (String) jsonLanguage.get("WAITING_TURN");

    public static final String SELECT_WPC = (String) jsonLanguage.get("SELECT_WPC");
    public static final String WAITING_PLAYERS_TO_SELECT_WPC = (String) jsonLanguage.get("WAITING_PLAYERS_TO_SELECT_WPC");
    public static final String TIMES_UP = (String) jsonLanguage.get("TIMES_UP");
    public static final String PRESENT_WPC = (String) jsonLanguage.get("PRESENT_WPC");
    public static final String FAVOURS_LEFT = (String) jsonLanguage.get("FAVOURS_LEFT");
    public static final String EXTRACTED_DICES = (String) jsonLanguage.get("EXTRACTED_DICES");
    public static final String RETURN = (String) jsonLanguage.get("RETURN");

    public static final String SHOW_PRIVATE_OBJECTIVE = (String) jsonLanguage.get("SHOW_PRIVATE_OBJECTIVE");
    public static final String SHOW_POCS = (String) jsonLanguage.get("SHOW_POCS");
    public static final String SHOW_TOOLCARDS = (String) jsonLanguage.get("SHOW_TOOLCARDS");
    public static final String SHOW_ROUNDTRACK = (String) jsonLanguage.get("SHOW_ROUNDTRACK");
    public static final String SHOW_ALL_WPCS = (String) jsonLanguage.get("SHOW_ALL_WPCS");

    public static final String TIME_LEFT_TO_CHOOSE_WPC_P1 = (String) jsonLanguage.get("TIME_LEFT_TO_CHOOSE_WPC_P1");
    public static final String TIME_LEFT_TO_CHOOSE_WPC_P2 = (String) jsonLanguage.get("TIME_LEFT_TO_CHOOSE_WPC_P2");

    public static final String ASK_WHAT_TO_DO = (String) jsonLanguage.get("ASK_WHAT_TO_DO");
    public static final String PLACE_DICE = (String) jsonLanguage.get("PLACE_DICE");
    public static final String USE_TOOLCARD = (String) jsonLanguage.get("USE_TOOLCARD");
    public static final String CLOSE_PARENTHESIS = (String) jsonLanguage.get("CLOSE_PARENTHESIS");
    public static final String END_TURN = (String) jsonLanguage.get("END_TURN");
    public static final String COMMAND_NOT_RECOGNIZED = (String) jsonLanguage.get("COMMAND_NOT_RECOGNIZED");
    public static final String INSERT_ACTION_NUMBER = (String) jsonLanguage.get("INSERT_ACTION_NUMBER");
    public static final String NO_STANDARD_ACTION_PASSED = (String) jsonLanguage.get("NO_STANDARD_ACTION_PASSED");
    public static final String PRESENT_PRIVATE_OBJS = (String) jsonLanguage.get("PRESENT_PRIVATE_OBJS");
    public static final String PRESENT_PRIVATE_OBJ = (String) jsonLanguage.get("PRESENT_PRIVATE_OBJ");
    public static final String PRESENT_POCS = (String) jsonLanguage.get("PRESENT_POCS");
    public static final String PRESENT_TOOLCARDS = (String) jsonLanguage.get("PRESENT_TOOLCARDS");
    public static final String PRESENT_WPCS = (String) jsonLanguage.get("PRESENT_WPCS");
    public static final String PRESENT_NEW_DICE = (String) jsonLanguage.get("PRESENT_NEW_DICE");
    public static final String ID = (String) jsonLanguage.get("ID");
    public static final String NAME = (String) jsonLanguage.get("NAME");
    public static final String DESCRIPTION = (String) jsonLanguage.get("DESCRIPTION");
    public static final String INSERT_DICE_ID_TO_PLACE = (String) jsonLanguage.get("INSERT_DICE_ID_TO_PLACE");
    public static final String INSERT_DICE_ID_TO_USE = (String) jsonLanguage.get("INSERT_DICE_ID_TO_USE");
    public static final String INSERT_ROW_TO_PLACE_DICE = (String) jsonLanguage.get("INSERT_ROW_TO_PLACE_DICE");
    public static final String INSERT_COL_TO_PLACE_DICE = (String) jsonLanguage.get("INSERT_COL_TO_PLACE_DICE");
    public static final String INSERT_DICE_ROW = (String) jsonLanguage.get("INSERT_DICE_ROW");
    public static final String INSERT_DICE_COL = (String) jsonLanguage.get("INSERT_DICE_COL");
    public static final String DICE_ALREADY_PRESENT = (String) jsonLanguage.get("DICE_ALREADY_PRESENT");
    public static final String DICE_NOT_FOUND = (String) jsonLanguage.get("DICE_NOT_FOUND");
    public static final String GOING_TO_PLACE_DICE = (String) jsonLanguage.get("GOING_TO_PLACE_DICE");
    public static final String GOING_TO_PLACE_DICE_INTO_DICEBAG = (String) jsonLanguage.get("GOING_TO_PLACE_DICE_INTO_DICEBAG");
    public static final String INCREMENT_DECREMENT_DICE = (String) jsonLanguage.get("INCREMENT_DECREMENT_DICE");

    public static final String SELECT_TOOLCARD_ID = (String) jsonLanguage.get("SELECT_TOOLCARD_ID");
    public static final String CANT_DO_PICK_FROM_THIS_POSITION = (String) jsonLanguage.get("CANT_DO_PICK_FROM_THIS_POSITION");
    public static final String SELECT_DICE_FROM_WPC = (String) jsonLanguage.get("SELECT_DICE_FROM_WPC");
    public static final String INSERT_NUMBER_FROM_PRESENTED = (String) jsonLanguage.get("INSERT_NUMBER_FROM_PRESENTED");
    public static final String ALL_PLAYERS_CHOOSE_WPC = (String) jsonLanguage.get("ALL_PLAYERS_CHOOSE_WPC");
    public static final String PLAYER_CHOSE_WPC = (String) jsonLanguage.get("PLAYER_CHOSE_WPC");

    public static final String GAME_STARTED = (String) jsonLanguage.get("GAME_STARTED");
    public static final String PLAYER_ENTER_GAME = (String) jsonLanguage.get("PLAYER_ENTER_GAME");
    public static final String PLAYER_EXIT_GAME = (String) jsonLanguage.get("PLAYER_EXIT_GAME");
    public static final String IN_GAME_PLAYERS = (String) jsonLanguage.get("IN_GAME_PLAYERS");
    public static final String OF = (String) jsonLanguage.get("OF");
    public static final String NEEDED = (String) jsonLanguage.get("NEEDED");
    public static final String TURN = (String) jsonLanguage.get("TURN");
    public static final String ROUND = (String) jsonLanguage.get("ROUND");
    public static final String ACTIVE_PLAYER = (String) jsonLanguage.get("ACTIVE_PLAYER");
    public static final String PLAYER_PLACED_DICE = (String) jsonLanguage.get("PLAYER_PLACED_DICE");
    public static final String IN_POSITION = (String) jsonLanguage.get("IN_POSITION");
    public static final String PLAYER_USED_TOOLCARD = (String) jsonLanguage.get("PLAYER_USED_TOOLCARD");
    public static final String CHANGED_DICE = (String) jsonLanguage.get("CHANGED_DICE");
    public static final String INTO_DICE = (String) jsonLanguage.get("INTO_DICE");
    public static final String POSITION = (String) jsonLanguage.get("POSITION");
    public static final String PLAYER_REPLACED_DICE = (String) jsonLanguage.get("PLAYER_REPLACED_DICE");
    public static final String PLACED_IN = (String) jsonLanguage.get("PLACED_IN");
    public static final String WITH_DICE = (String) jsonLanguage.get("WITH_DICE");
    public static final String EXTRACTED_DICES_REPLACED = (String) jsonLanguage.get("EXTRACTED_DICES_REPLACED");
    public static final String PLAYER_DISCONNECTED = (String) jsonLanguage.get("PLAYER_DISCONNECTED");
    public static final String PLAYER_RECONNECTED = (String) jsonLanguage.get("PLAYER_RECONNECTED");
    public static final String LOST_CONNECTION = (String) jsonLanguage.get("LOST_CONNECTION");
    public static final String LOGIN_FROM_ANOTHER_DEVICE = (String) jsonLanguage.get("LOGIN_FROM_ANOTHER_DEVICE");
    public static final String YOU_HAVE_BEEN_DISCONNECTED = (String) jsonLanguage.get("YOU_HAVE_BEEN_DISCONNECTED");

    public static final String TIME_TO_WAIT_PLAYER_ENDED = (String) jsonLanguage.get("TIME_TO_WAIT_PLAYER_ENDED");
    public static final String POINTS = (String) jsonLanguage.get("POINTS");

    public static final String PLAYER_SKIPPED_TURN = (String) jsonLanguage.get("PLAYER_SKIPPED_TURN");
    public static final String PLAYER_SKIPPED_TURN_DUE_TO_TOOLCARD = (String) jsonLanguage.get("PLAYER_SKIPPED_TURN_DUE_TO_TOOLCARD");

    public static final String GAME_ENDED = (String) jsonLanguage.get("GAME_ENDED");
    public static final String FINAL_TABLE = (String) jsonLanguage.get("FINAL_TABLE");

    public static final String TURN_TIME_ENDED = (String) jsonLanguage.get("TURN_TIME_ENDED");
    public static final String TURN_TIME_LEFT_P1 = (String) jsonLanguage.get("TURN_TIME_LEFT_P1");
    public static final String TURN_TIME_LEFT_P2 = (String) jsonLanguage.get("TURN_TIME_LEFT_P2");

    private static ArrayList<String> getArrayElements(JSONArray array) {
        ArrayList<String> elements = new ArrayList<>();
        for(Object str : array) elements.add((String) str);
        return elements;
    }

    private static ArrayList<String> getQuitResponses(JSONArray array) {
        ArrayList<String> elements = new ArrayList<>();
        elements.add(QUIT_RESPONSE);
        for(Object str : array) elements.add((String) str);
        return elements;
    }

    private static int[] getSteps(JSONArray array) {
        int[] steps = new int[array.size()];
        int i = 0;
        for(Object step : array){
            steps[i] = ((Long) step).intValue();
        }
        return steps;
    }
}
