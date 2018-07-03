package client.constants;

public class GuiConstants {

    /*
    Constants for the StartController class
     */
    public static final String IMPOSSIBLE_RMI_CONNECTION = "Impossibile stabilire connessione RMI con il Server.";
    public static final String IMPOSSIBLE_SOCKET_CONNECTION ="Impossibile stabilire connessione Socket con il Server.";

     /*
    Constants for the SetNewGameController class
     */

    //entrance in a game
    public static final String SELECT_NUMBER_ERROR = "Seleziona un numero";
    public static final String ENTRANCE_IN_GAME = "Entrato nella partita: ";
    public static final String PLAYERS_IN_GAME = "Giocatori presenti: ";
    public static final String PREPOSITION = " di ";
    public static final String NECESSARY = " necessari.";
    public static final String ENTRANCE_NEW_PLAYER = " é entrato in partita!";

    //EXIT from game, disconnection and reconnection
    public static final String EXIT_PLAYER = " é uscito dalla partita!";
    public static final String DISCONNECTION_OF_PLAYER = " si è disconnesso!";
    public static final String RECONNECTION_OF_PLAYER = " si è riconnesso!";

    public static final String ANIMATION_DICE =  "violet5";
    public static final String PRIVATE_OBJ_IDENTIFIER_CSS = "prOC";
    public static final String TOOL_IDENTIFIER_CSS = "tool";
    public static final String POC_IDENTIFIER_CSS = "poc";
    public static final String DEFAULT_CELL_COLOR = "white";
    public static final String NUMBER_IDENTIFIER_CSS = "num";

    //timer constants
    public static final String MINUTES_TIMER = "00:";
    public static final String MINUTES_SECONDS_TIMER = "00:0";
    public static final String END_TIMER = "00:00";


    //schemas constants
    public static final String YOUR_SCHEMA_CHOICE = "Hai scelto lo schema ";
    public static final String OTHERS_SCHEMA_CHOICE = " ha scelto lo schema ";

    public static final String USING_TOOLCARD = "Stai usando la ToolCard";
    public static final String USED_TOOLCARD =  " ha usato la ToolCard ";
    public static final String FAVOURS =   "X";

    //turn and rund constrants
    public static final String ROUND = "Round numero ";
    public static final String WAIT_TURN = "Attendi il tuo turno.";
    public static final String YOUR_TURN = "Tocca a te!";
    public static final String TURN_OF = ", turno di ";
    public static final String SKIP_TURN =" salta il turno!";

    public static final String USE_TOOL_OR_END_TURN = "Usa una ToolCard o termina il turno!";
    public static final String PLACE_DICE_OR_END_TURN = "Posiziona un dado o passa il turno!";
    public static final String ONLY_END_TURN ="Passa il turno!";


    public static final String PLACE_DICE_FROM_WPC_TO_WPC = "Scegli un dado dallo schema e posizionalo.";
    public static final String PLACE_DICE_FROM_EXTRACTED_TO_DICEBAG = "Scegli un dado dalla riserva e mettilo nel sacchetto!";
    public static final String PLACE_DICE_FROM_EXTRACTED_TO_WPC = "Scegli un dado dalla riserva e posizionalo.";

    public static final String PICK_DICE_FROM_WPC = "Clicca su un dado del tuo schema di gioco!";
    public static final String PICK_DICE_FROM_EXTRACTED = "Clicca su un dado della riserva!";
    public static final String PICK_DICE_FROM_ROUNDTRACK = "Clicca su un dado del Round Track!";
    public static final String ACTIVE_TOOLCARD_SINGLE_PLAYER = "Attiva la ToolCard scegliendo dalla riserva un dado";

    public static final String ADD_SUBTRACT_ONE = "Aggiungi 1 o sottrai 1";
    public static final String CANCEL_TOOLCARD = "Tool Card cancellata";

    //possible values when the toolcard is interrupted
    public static final String TOOLCARD_BLOCKED = "Informazioni blocco ToolCard";
    public static final String YES_VALUE = "Yes";
    public static final String NO_VALUE = "No";
    public static final String OK_VALUE = "Ok";
    public static final String BACK_VALUE = "Indietro";

    public static final String GAME_LOST = "HAI PERSO!";
    public static final String GAME_WON = "HAI VINTO!";
}
