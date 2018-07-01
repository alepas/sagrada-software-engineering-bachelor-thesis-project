package server.model.configLoader;

import org.json.simple.JSONObject;
import static shared.constants.GameConstants.*;

public class GameConfigLoader {

    public static void loadConfig(JSONObject jsonGame){
        //General
        CHOOSE_WPC_WAITING_TIME = ((Long) jsonGame.get("CHOOSE_WPC_WAITING_TIME")).intValue();
        TASK_DELAY = ((Long) jsonGame.get("TASK_DELAY")).intValue();
        NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER = ((Long) jsonGame.get("NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER")).intValue();
        PRIVATE_OBJ_SCORE_VALUE = ((Long) jsonGame.get("PRIVATE_OBJ_SCORE_VALUE")).intValue();
        MIN_NUM_PLAYERS = ((Long) jsonGame.get("MIN_NUM_PLAYERS")).intValue();
        MAX_NUM_PLAYERS = ((Long) jsonGame.get("MAX_NUM_PLAYERS")).intValue();
        NUM_OF_ROUNDS = ((Long) jsonGame.get("NUM_OF_ROUNDS")).intValue();
        MAX_DICES_FOR_ROUND = ((Long) jsonGame.get("MAX_DICES_FOR_ROUND")).intValue();

        //MultiplayerGame
        NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME = ((Long) jsonGame.get("NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME")).intValue();
        NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME = ((Long) jsonGame.get("NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME")).intValue();
        NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME = ((Long) jsonGame.get("NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME")).intValue();
        NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME = ((Long) jsonGame.get("NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME")).intValue();
        MULTIPLAYER_MIN_NUM_PLAYERS = ((Long) jsonGame.get("MULTIPLAYER_MIN_NUM_PLAYERS")).intValue();
        TIME_TO_PLAY_TURN_MULTIPLAYER = ((Long) jsonGame.get("TIME_TO_PLAY_TURN_MULTIPLAYER")).intValue();
        TIME_WAITING_PLAYERS_TO_ENTER_GAME = ((Long) jsonGame.get("TIME_WAITING_PLAYERS_TO_ENTER_GAME")).intValue();
        DEFAULT_SCORE_MULTIPLAYER_GAME = ((Long) jsonGame.get("DEFAULT_SCORE_MULTIPLAYER_GAME")).intValue();

        //SingleplayerGame
        NUM_PRIVATE_OBJ_FOR_PLAYER_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("NUM_PRIVATE_OBJ_FOR_PLAYER_IN_SINGLEPLAYER_GAME")).intValue();
        NUM_PUBLIC_OBJ_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("NUM_PUBLIC_OBJ_IN_SINGLEPLAYER_GAME")).intValue();
        MIN_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("MIN_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME")).intValue();
        MAX_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("MAX_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME")).intValue();
        NUM_OF_TURNS_FOR_PLAYER_IN_SINGLEPLAYER_GAME = ((Long) jsonGame.get("NUM_OF_TURNS_FOR_PLAYER_IN_SINGLEPLAYER_GAME")).intValue();
    }
}
