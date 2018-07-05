package shared.constants;

import org.json.simple.JSONObject;
import shared.configLoader.SharedConfigLoader;

/**
 * The class constants all constants related to all exceptions
 */
public class ExceptionConstants {
    private static final JSONObject jsonLanguage = SharedConfigLoader.jsonSharedLanguage;

    public static final String GAME_NOT_IN_AVAILABLE_LIST = (String) jsonLanguage.get("GAME_NOT_IN_AVAILABLE_LIST");
    public static final String DICEBAG_WRONG_NUMBER = (String) jsonLanguage.get("DICEBAG_WRONG_NUMBER");
    public static final String CANNOT_CREATE_PLAYER = (String) jsonLanguage.get("CANNOT_CREATE_PLAYER");

    public static final String INVALID_MULTIPLAYER_GAME_PLAYERS_P1 = (String) jsonLanguage.get("INVALID_MULTIPLAYER_GAME_PLAYERS_P1");
    public static final String INVALID_MULTIPLAYER_GAME_PLAYERS_P2 = (String) jsonLanguage.get("INVALID_MULTIPLAYER_GAME_PLAYERS_P2");
    public static final String INVALID_MULTIPLAYER_GAME_PLAYERS_P3 = (String) jsonLanguage.get("INVALID_MULTIPLAYER_GAME_PLAYERS_P3");
    public static final String INVALID_MULTIPLAYER_GAME_PLAYERS_P4 = (String) jsonLanguage.get("INVALID_MULTIPLAYER_GAME_PLAYERS_P4");

    public static final String INVALID_SINGLEPLAYER_GAME_PLAYERS_P1 = (String) jsonLanguage.get("INVALID_SINGLEPLAYER_GAME_PLAYERS_P1");
    public static final String INVALID_SINGLEPLAYER_GAME_PLAYERS_P2 = (String) jsonLanguage.get("INVALID_SINGLEPLAYER_GAME_PLAYERS_P2");

    public static final String INVALID_NUM_OF_PLAYERS = (String) jsonLanguage.get("INVALID_NUM_OF_PLAYERS");

    public static final String MAX_PLAYER_EXCEEDED_P1 = (String) jsonLanguage.get("MAX_PLAYER_EXCEEDED_P1");
    public static final String MAX_PLAYER_EXCEEDED_P2 = (String) jsonLanguage.get("MAX_PLAYER_EXCEEDED_P2");
    public static final String MAX_PLAYER_EXCEEDED_P3 = (String) jsonLanguage.get("MAX_PLAYER_EXCEEDED_P3");

    public static final String NOT_ENOUGH_PLAYERS_P1 = (String) jsonLanguage.get("NOT_ENOUGH_PLAYERS_P1");
    public static final String NOT_ENOUGH_PLAYERS_P2 = (String) jsonLanguage.get("NOT_ENOUGH_PLAYERS_P2");
    public static final String NOT_ENOUGH_PLAYERS_P3 = (String) jsonLanguage.get("NOT_ENOUGH_PLAYERS_P3");

    public static final String NOT_YOUR_WPC_P1 = (String) jsonLanguage.get("NOT_YOUR_WPC_P1");
    public static final String NOT_YOUR_WPC_P2 = (String) jsonLanguage.get("NOT_YOUR_WPC_P2");

    public static final String USER_ALREADY_IN_GAME = (String) jsonLanguage.get("USER_ALREADY_IN_GAME");
    public static final String USER_NOT_IN_GAME = (String) jsonLanguage.get("USER_NOT_IN_GAME");

    public static final String CANNOT_ADD_PLAYER_IN_DB = (String) jsonLanguage.get("CANNOT_ADD_PLAYER_IN_DB");

    public static final String CANNOT_CANCEL_ACTION_0 = (String) jsonLanguage.get("CANNOT_CANCEL_ACTION_0");
    public static final String CANNOT_CANCEL_ACTION_1_P1 = (String) jsonLanguage.get("CANNOT_CANCEL_ACTION_1_P1");
    public static final String CANNOT_CANCEL_ACTION_1_P2 = (String) jsonLanguage.get("CANNOT_CANCEL_ACTION_1_P2");
    public static final String CANNOT_CANCEL_ACTION_1_P3 = (String) jsonLanguage.get("CANNOT_CANCEL_ACTION_1_P3");
    public static final String CANNOT_CANCEL_ACTION_2_P1 = (String) jsonLanguage.get("CANNOT_CANCEL_ACTION_2_P1");
    public static final String CANNOT_CANCEL_ACTION_2_P2 = (String) jsonLanguage.get("CANNOT_CANCEL_ACTION_2_P2");
    public static final String CANNOT_CANCEL_ACTION_3_P1 = (String) jsonLanguage.get("CANNOT_CANCEL_ACTION_3_P1");
    public static final String CANNOT_CANCEL_ACTION_3_P2 = (String) jsonLanguage.get("CANNOT_CANCEL_ACTION_3_P2");

    public static final String CANNOT_CLOSE_OLD_CONNECTION_0 = (String) jsonLanguage.get("CANNOT_CLOSE_OLD_CONNECTION_0");
    public static final String CANNOT_CLOSE_OLD_CONNECTION_1 = (String) jsonLanguage.get("CANNOT_CLOSE_OLD_CONNECTION_1");
    public static final String CANNOT_CLOSE_OLD_CONNECTION_2 = (String) jsonLanguage.get("CANNOT_CLOSE_OLD_CONNECTION_2");

    public static final String CANNOT_FIND_GAME_FOR_USER = (String) jsonLanguage.get("CANNOT_FIND_GAME_FOR_USER");
    public static final String CANNOT_FIND_PLAYER_IN_DB = (String) jsonLanguage.get("CANNOT_FIND_PLAYER_IN_DB");
    public static final String CANNOT_FIND_USER_IN_DB_P1 = (String) jsonLanguage.get("CANNOT_FIND_USER_IN_DB_P1");
    public static final String CANNOT_FIND_USER_IN_DB_P2 = (String) jsonLanguage.get("CANNOT_FIND_USER_IN_DB_P2");

    public static final String CANNOT_INTERRUPT_TOOLCARD_P1 = (String) jsonLanguage.get("CANNOT_INTERRUPT_TOOLCARD_P1");
    public static final String CANNOT_INTERRUPT_TOOLCARD_P2 = (String) jsonLanguage.get("CANNOT_INTERRUPT_TOOLCARD_P2");

    public static final String CANNOT_LOGIN_USER_03_P1 = (String) jsonLanguage.get("CANNOT_LOGIN_USER_03_P1");
    public static final String CANNOT_LOGIN_USER_0_P2 = (String) jsonLanguage.get("CANNOT_LOGIN_USER_0_P2");
    public static final String CANNOT_LOGIN_USER_1_P1 = (String) jsonLanguage.get("CANNOT_LOGIN_USER_1_P1");
    public static final String CANNOT_LOGIN_USER_1_P2 = (String) jsonLanguage.get("CANNOT_LOGIN_USER_1_P2");
    public static final String CANNOT_LOGIN_USER_2_P1 = (String) jsonLanguage.get("CANNOT_LOGIN_USER_2_P1");
    public static final String CANNOT_LOGIN_USER_2_P2 = (String) jsonLanguage.get("CANNOT_LOGIN_USER_2_P2");
    public static final String CANNOT_LOGIN_USER_3_P2 = (String) jsonLanguage.get("CANNOT_LOGIN_USER_3_P2");
    public static final String CANNOT_LOGIN_USER_4 = (String) jsonLanguage.get("CANNOT_LOGIN_USER_4");

    public static final String CANNOT_PERFORM_THIS_MOVE_NAME_1 = (String) jsonLanguage.get("CANNOT_PERFORM_THIS_MOVE_NAME_1");
    public static final String CANNOT_PERFORM_THIS_MOVE_NAME_2 = (String) jsonLanguage.get("CANNOT_PERFORM_THIS_MOVE_NAME_2");
    public static final String CANNOT_PERFORM_THIS_MOVE_IMPOSSIBLE = (String) jsonLanguage.get("CANNOT_PERFORM_THIS_MOVE_IMPOSSIBLE");
    public static final String CANNOT_PERFORM_THIS_MOVE_FOR_PLAYER = (String) jsonLanguage.get("CANNOT_PERFORM_THIS_MOVE_FOR_PLAYER");
    public static final String CANNOT_PERFORM_THIS_MOVE_0 = (String) jsonLanguage.get("CANNOT_PERFORM_THIS_MOVE_0");
    public static final String CANNOT_PERFORM_THIS_MOVE_1 = (String) jsonLanguage.get("CANNOT_PERFORM_THIS_MOVE_1");
    public static final String CANNOT_PERFORM_THIS_MOVE_2 = (String) jsonLanguage.get("CANNOT_PERFORM_THIS_MOVE_2");
    public static final String CANNOT_PERFORM_THIS_MOVE_3 = (String) jsonLanguage.get("CANNOT_PERFORM_THIS_MOVE_3");

    public static final String CANNOT_PICK_DICE = (String) jsonLanguage.get("CANNOT_PICK_DICE");
    public static final String CANNOT_PICK_DICE_0_P1 = (String) jsonLanguage.get("CANNOT_PICK_DICE_0_P1");
    public static final String CANNOT_PICK_DICE_0_P2 = (String) jsonLanguage.get("CANNOT_PICK_DICE_0_P2");
    public static final String CANNOT_PICK_DICE_1 = (String) jsonLanguage.get("CANNOT_PICK_DICE_1");
    public static final String CANNOT_PICK_DICE_2 = (String) jsonLanguage.get("CANNOT_PICK_DICE_2");
    public static final String CANNOT_PICK_DICE_3 = (String) jsonLanguage.get("CANNOT_PICK_DICE_3");
    public static final String CANNOT_PICK_DICE_4 = (String) jsonLanguage.get("CANNOT_PICK_DICE_4");
    public static final String CANNOT_PICK_DICE_5 = (String) jsonLanguage.get("CANNOT_PICK_DICE_5");

    public static final String CANNOT_PICK_NUMBER = (String) jsonLanguage.get("CANNOT_PICK_NUMBER");
    public static final String CANNOT_PICK_POSITION = (String) jsonLanguage.get("CANNOT_PICK_POSITION");

    public static final String CANNOT_REGISTER_USER_0_P1 = (String) jsonLanguage.get("CANNOT_REGISTER_USER_0_P1");
    public static final String CANNOT_REGISTER_USER_0_P2 = (String) jsonLanguage.get("CANNOT_REGISTER_USER_0_P2");
    public static final String CANNOT_REGISTER_USER_1_P1 = (String) jsonLanguage.get("CANNOT_REGISTER_USER_1_P1");
    public static final String CANNOT_REGISTER_USER_1_P2 = (String) jsonLanguage.get("CANNOT_REGISTER_USER_1_P2");
    public static final String CANNOT_REGISTER_USER_2_P1 = (String) jsonLanguage.get("CANNOT_REGISTER_USER_2_P1");
    public static final String CANNOT_REGISTER_USER_2_P2 = (String) jsonLanguage.get("CANNOT_REGISTER_USER_2_P2");

    public static final String CANNOT_UPDATE_STATS_P1 = (String) jsonLanguage.get("CANNOT_UPDATE_STATS_P1");
    public static final String CANNOT_UPDATE_STATS_P2 = (String) jsonLanguage.get("CANNOT_UPDATE_STATS_P2");

    public static final String CANNOT_PICK_TOOLCARD = (String) jsonLanguage.get("CANNOT_PICK_TOOLCARD");
    public static final String CANNOT_USE_TOOLCARD = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD");
    public static final String CANNOT_USE_TOOLCARD_0 = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_0");
    public static final String CANNOT_USE_TOOLCARD_1 = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_1");
    public static final String CANNOT_USE_TOOLCARD_2 = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_2");
    public static final String CANNOT_USE_TOOLCARD_3 = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_3");
    public static final String CANNOT_USE_TOOLCARD_45 = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_45");
    public static final String CANNOT_USE_TOOLCARD_6 = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_6");
    public static final String CANNOT_USE_TOOLCARD_7 = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_7");
    public static final String CANNOT_USE_TOOLCARD_8 = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_8");
    public static final String CANNOT_USE_TOOLCARD_DEF = (String) jsonLanguage.get("CANNOT_USE_TOOLCARD_DEF");

    public static final String DATABASE_FILE_ERROR_0 = (String) jsonLanguage.get("DATABASE_FILE_ERROR_0");
    public static final String DATABASE_FILE_ERROR_1 = (String) jsonLanguage.get("DATABASE_FILE_ERROR_1");

    public static final String NO_SOCKET_FOUND_P1 = (String) jsonLanguage.get("NO_SOCKET_FOUND_P1");
    public static final String NO_SOCKET_FOUND_P2 = (String) jsonLanguage.get("NO_SOCKET_FOUND_P2");

    public static final String NO_TOOLCARD_IN_USE = (String) jsonLanguage.get("NO_TOOLCARD_IN_USE");
    public static final String NULL_TOKEN = (String) jsonLanguage.get("NULL_TOKEN");
    public static final String PASSWORD_PARSING_ERROR = (String) jsonLanguage.get("PASSWORD_PARSING_ERROR");

    public static final String PLAYER_NOT_AUTHORIZED_P1 = (String) jsonLanguage.get("PLAYER_NOT_AUTHORIZED_P1");
    public static final String PLAYER_NOT_AUTHORIZED_P2 = (String) jsonLanguage.get("PLAYER_NOT_AUTHORIZED_P2");

    public static final String INVALID_WPC_ID = (String) jsonLanguage.get("INVALID_WPC_ID");
    public static final String NOT_EXISTING_CELL = (String) jsonLanguage.get("NOT_EXISTING_CELL");

    public static String INVALID_NUM_PLAYERS;
    public static String INVALID_TOOLCARD_LEVEL;

}
