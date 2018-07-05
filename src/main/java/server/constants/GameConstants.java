package server.constants;

import org.json.simple.JSONObject;
import server.model.configLoader.ConfigLoader;

/**
 * This class contains all constants related to the abstract class Game and both single player game and multi-player game
 */
public class GameConstants {
    private static final JSONObject jsonServerObject = ConfigLoader.jsonServerObject;
    private static final JSONObject jsonGame = (JSONObject) jsonServerObject.get("game");

    //General: those constants are used by both single and multi player games
    public static int CHOOSE_WPC_WAITING_TIME = ((Long) jsonGame.get("CHOOSE_WPC_WAITING_TIME")).intValue();
    public static int TASK_DELAY = ((Long) jsonGame.get("TASK_DELAY")).intValue();
    public static final int NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER = ((Long) jsonGame.get("NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER")).intValue();
    public static final int PRIVATE_OBJ_SCORE_VALUE = ((Long) jsonGame.get("PRIVATE_OBJ_SCORE_VALUE")).intValue();
    public static final int MIN_NUM_PLAYERS = ((Long) jsonGame.get("MIN_NUM_PLAYERS")).intValue();
    public static final int MAX_NUM_PLAYERS = ((Long) jsonGame.get("MAX_NUM_PLAYERS")).intValue();
    public static final int NUM_OF_ROUNDS = ((Long) jsonGame.get("NUM_OF_ROUNDS")).intValue();
    public static final int MAX_DICES_FOR_ROUND = ((Long) jsonGame.get("MAX_DICES_FOR_ROUND")).intValue();

    //MultiplayerGame
    public static final int NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME = ((Long) jsonGame.get("NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME")).intValue();
    public static final int NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME = ((Long) jsonGame.get("NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME")).intValue();
    public static final int NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME = ((Long) jsonGame.get("NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME")).intValue();
    public static final int NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME = ((Long) jsonGame.get("NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME")).intValue();
    public static final int MULTIPLAYER_MIN_NUM_PLAYERS = ((Long) jsonGame.get("MULTIPLAYER_MIN_NUM_PLAYERS")).intValue();
    public static final int TIME_TO_PLAY_TURN_MULTIPLAYER = ((Long) jsonGame.get("TIME_TO_PLAY_TURN_MULTIPLAYER")).intValue();
    public static final int TIME_WAITING_PLAYERS_TO_ENTER_GAME = ((Long) jsonGame.get("TIME_WAITING_PLAYERS_TO_ENTER_GAME")).intValue();
    public static final int DEFAULT_SCORE_MULTIPLAYER_GAME = ((Long) jsonGame.get("DEFAULT_SCORE_MULTIPLAYER_GAME")).intValue();
    public static final int DEFAULT_SCORE_MULTIPLAYER_GAME_LEFT = -10;

    //SingleplayerGame
    public static final int NUM_PRIVATE_OBJ_FOR_PLAYER_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("NUM_PRIVATE_OBJ_FOR_PLAYER_IN_SINGLEPLAYER_GAME")).intValue();
    public static final int NUM_PUBLIC_OBJ_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("NUM_PUBLIC_OBJ_IN_SINGLEPLAYER_GAME")).intValue();
    public static final int MIN_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("MIN_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME")).intValue();
    public static final int MAX_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("MAX_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME")).intValue();
    public static final int NUM_OF_TURNS_FOR_PLAYER_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("NUM_OF_TURNS_FOR_PLAYER_IN_SINGLEPLAYER_GAME")).intValue();


}
