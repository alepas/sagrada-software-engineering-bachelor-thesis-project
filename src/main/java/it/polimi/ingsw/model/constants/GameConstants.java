package it.polimi.ingsw.model.constants;

public class GameConstants {
    //General
    public static final int CHOOSE_WPC_WAITING_TIME = 60 * 1000; //Tempo in millisecondi
    public static final int TASK_DELAY = 2 * 1000; //Ritardo per l'invio della notifica
    public static final int NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER = 4;
    public static final int PRIVATE_OBJ_SCORE_VALUE = 1;
    public static final int MIN_NUM_PLAYERS = 1;
    public static final int MAX_NUM_PLAYERS = 4;

    //MultiplayerGame
    public static final int NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME = 1;
    public static final int NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME = 3;
    public static final int NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME = 3;
    public static final int NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME = 2;
    public static final int MULTIPLAYER_MIN_NUM_PLAYERS = 2;

    //SingleplayerGame
    public static final int NUM_PRIVATE_OBJ_FOR_PLAYER_IN_SINGLEPLAYER_GAME = 2;
    public static final int NUM_PUBLIC_OBJ_IN_SINGLEPLAYER_GAME = 2;
    public static final int MIN_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME = 1;
    public static final int MAX_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME = 5;
    public static final int NUM_OF_TURNS_FOR_PLAYER_IN_SINGLEPLAYER_GAME = 2;


}
