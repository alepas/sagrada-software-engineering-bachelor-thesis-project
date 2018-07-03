package client.constants;

import java.util.ArrayList;
import java.util.Arrays;

public class CliConstants {
    public static int COUNTER_START_NUMBER = 0;

    public static int WpcSpacing = 10;
    public static int[] WPC_WAITING_STEPS = {0, 5, 15, 30, 50};

    public static String YES_RESPONSE = "y";
    public static String NO_RESPONSE = "n";
    public static String ESCAPE_RESPONSE = "back";
    public static String QUIT_RESPONSE = "quit";

    public static String SOCKET_OR_RMI = ">>> Vuoi usare socket o rmi?";
    public static ArrayList<String> SOCKET_RESPONSES = new ArrayList<String>(
            Arrays.asList("socket", "s"));
    public static ArrayList<String> RMI_RESPONSES = new ArrayList<String>(
            Arrays.asList("rmi", "r"));
    public static ArrayList<String> QUIT_RESPONSES = new ArrayList<String>(
            Arrays.asList(QUIT_RESPONSE, "q"));

    public static String INSTRUCTION_NOT_RECOGNIZED = ">>> Istruzione non riconosciuta. Perfavore inserisci '" + SOCKET_RESPONSES.get(0) +
            "' o '" + RMI_RESPONSES.get(0) + "'";
    public static String INSTRUCTION_NOT_RECOGNIZED_P1 = ">>> Istruzione non riconosciuta. Perfavore inserisci '";
    public static String INSTRUCTION_NOT_RECOGNIZED_P2 = "' o '";
    public static String INSTRUCTION_NOT_RECOGNIZED_P3 = "'";

    public static String CANNOT_CONNECT_WITH_SOCKET_SERVER = ">>> Impossibile stabilire connessione Socket con il Server";
    public static String CANNOT_CONNECT_WITH_RMI_SERVER = ">>> Impossibile stabilire connessione RMI con il Server";
    public static String VOID_STRING = "";

    public static String TECHNOLOGY_NOT_SUPPORTED_P1 = ">>> Tecnologia ";
    public static String TECHNOLOGY_NOT_SUPPORTED_P2 = " non ancora supportata";

    public static String PRESENT_RETURN_BACK = " (digita '" + ESCAPE_RESPONSE + "' per annullare la mossa)";
    public static String PRESENT_QUIT = " (Digita '" + QUIT_RESPONSE + "' per uscire)";
    public static String INSERT_YES_OR_NO = "Digita '" + YES_RESPONSE + "' oppure '" + NO_RESPONSE + "'";

    public static String CHOOSE_LOG_TYPE =
            "Benvenuto in Sagrada. Prima di iniziare è necessario registrarsi. " +
            "Ha già un account? " + YES_RESPONSE + "/" + NO_RESPONSE + PRESENT_QUIT;

    public static String CHOOSE_LOG_TYPE_ERROR =
            "Comando non riconosciuto." + INSERT_YES_OR_NO + ". Hai già un account?";

    public static String CREATE_USER_PHASE = "Creazione nuovo utente";
    public static String LOGIN_PHASE = "Accedi al tuo account";
    public static String LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE = "Scrivi '" + QUIT_RESPONSE + "', '" + YES_RESPONSE + "' oppure '" + NO_RESPONSE + "'";

    public static String INSERT_USERNAME = "Inserisci l'username. (Digita \"" + ESCAPE_RESPONSE +
            "\" per tornare indietro)";
    public static String INSERT_PASS = "Inserisci la password.";

    public static String CREATE_USER_ERROR = "Impossibile creare l'utente";
    public static String LOGIN_ERROR = "Impossibile effettuare il login";

    public static String LOG_SUCCESS = "Sei connesso come: ";

    public static String PRESENT_MAIN_MENU = "Cosa vuoi fare? Digita:\n>>> 1) Cerca partita\n" +
            ">>> 2) Visualizza statistiche giocatore\n>>> 3) Logout";

    public static String SELECT_NUM_PLAYERS = "Seleziona numero giocatori: (digita '" + ESCAPE_RESPONSE + "' per tornare indietro)";

    public static String PRESS_ENTER_TO_CONTINUE = "Premi invio per continuare";
    public static String PRESS_ENTER_TO_MENU = "Premi invio per tornare al menù principale";

    public static String NUM_PLAYERS_ERROR = "Numero giocatori non consentito";

    public static String RECONNECTED_TO_GAME = "Riconnesso alla partita: ";

    public static String WERE_PLACING_DICE_STRING = "Stavi posizionando il dado: ";

    public static String DICE_COME_FROM = "Il dado provine da: ";

    public static String DICE_SHOULD_BE_PUT = "Il dado deve essere posizionato: ";

    public static String USERNAME = "Username: ";
    public static String WON_GAMES = "Partite vinte: ";
    public static String LOST_GAMES = "Partite perse: ";
    public static String ABANDONED_GAMES = "Partite abbandonate: ";
    public static String RANKING = "Ranking: ";

    public static String IMPOSSIBLE_TO_FIND_GAME = "Impossibile trovare partita. Riprovare più tardi";
    public static String INSERT_INT_NUMBER = "Perfavore inserire un numero intero";

    public static String WAITING_PLAYERS = "In attesa di altri giocatori...";
    public static String WAITING_GAME = "Attendo che la partita inizi...";
    public static String WAITING_PRIVATE_OBJECTIVE = "Attendo i private objectives...";
    public static String WAITING_WPCS = "Attendo le wpc...";
    public static String WAITING_TOOLCARD = "In attesa delle toolcard...";
    public static String WAITING_POCS = "In attesa degli obiettivi pubblici...";
    public static String WAITING_TURN = "In attesa del mio turno...";

    public static String SELECT_WPC = "Seleziona la wpc che vuoi utilizzare";
    public static String WAITING_PLAYERS_TO_SELECT_WPC= "Attendo che gli altri giocatori selezionino la wpc";
    public static String TIMES_UP = "Tempo scaduto";
    public static String PRESENT_WPC = "Questa è la tua wpc";
    public static String FAVOURS_LEFT = "Favours rimasti: ";
    public static String EXTRACTED_DICES = "Dadi estratti:";
    public static String RETURN = "\n";

    public static String SHOW_PRIVATE_OBJECTIVE = ") Visualizza Obiettivo Privato";
    public static String SHOW_POCS = ") Visualizza Obiettivi Pubblici";
    public static String SHOW_TOOLCARDS = ") Visualizza Carte Utensile";
    public static String SHOW_ROUNDTRACK = ") Visualizza RoundTrack";
    public static String SHOW_ALL_WPCS = ") Visualizza tutte le wpc";

    public static String TIME_LEFT_TO_CHOOSE_WPC_P1 = "Rimangono ";
    public static String TIME_LEFT_TO_CHOOSE_WPC_P2 = " secondi per scegliere le wpc";

    public static String ASK_WHAT_TO_DO = "Cosa vuoi fare?";
    public static String PLACE_DICE = ") Posiziona dado";
    public static String USE_TOOLCARD = ") Usa toolcard (Favours rimasti: ";
    public static String CLOSE_PARENTHESIS = ")";
    public static String END_TURN = ") Passa turno";
    public static String COMMAND_NOT_RECOGNIZED = "Comando non riconosciuto";
    public static String INSERT_ACTION_NUMBER = "Perfavore inserire il numero dell'azione che si vuole eseguire";
    public static String NO_STANDARD_ACTION_PASSED = "Passata azione non standard";
    public static String PRESENT_PRIVATE_OBJS = "I tuoi private objective sono: ";
    public static String PRESENT_PRIVATE_OBJ = "Il tuo private objective è: ";
    public static String PRESENT_POCS = "Gli obbiettivi pubblici della partita sono:\n";
    public static String PRESENT_TOOLCARDS = "Le toolcards della partita sono:\n";
    public static String PRESENT_WPCS = "Le tue wpc sono:\n\n";
    public static String PRESENT_NEW_DICE = "Il nuovo dado è:";
    public static String ID = "ID: ";
    public static String NAME = "Nome: ";
    public static String DESCRIPTION = "Descrizione: ";
    public static String INSERT_DICE_ID_TO_PLACE = "Inserisci l'ID del dado da posizionare";
    public static String INSERT_DICE_ID_TO_USE = "Inserisci l'ID del dado da utilizzare.";
    public static String INSERT_ROW_TO_PLACE_DICE = "Inserisci la riga in cui posizionare il dado.";
    public static String INSERT_COL_TO_PLACE_DICE = "Inserisci la colonna in cui posizionare il dado.";
    public static String INSERT_DICE_ROW = "Inserisci la riga del dado.";
    public static String INSERT_DICE_COL = "Indica la colonna del dado.";
    public static String DICE_ALREADY_PRESENT = "É gia presente un dado nella posizione (";
    public static String DICE_NOT_FOUND = "Non è presente alcun dado nella posizione inserita o la posizione non esiste";
    public static String GOING_TO_PLACE_DICE = "Stai per posizionare il dado";
    public static String GOING_TO_PLACE_DICE_INTO_DICEBAG = "Stai posizionando il dado nel sacchetto";
    public static String INCREMENT_DECREMENT_DICE = "Scegli di quanto aumentare/diminuire il valore del dado";

    public static String SELECT_TOOLCARD_ID = "Seleziona l'ID della toolcard da utilizzare.";
    public static String CANT_DO_PICK_FROM_THIS_POSITION = "Non so come fare una pick dalla posizione passata";
    public static String SELECT_DICE_FROM_WPC = "Seleziona dalla wpc il dado da utilizzare";
    public static String INSERT_NUMBER_FROM_PRESENTED = "Inserire un numero tra quelli presenti";
    public static String ALL_PLAYERS_CHOOSE_WPC = "Tutti i giocatori hanno scelto la wpc";
    public static String PLAYER_CHOSE_WPC = " ha scelto la wpc: ";

    public static String GAME_STARTED = "Partita iniziata";
    public static String PLAYER_ENTER_GAME = " è entrato in partita.";
    public static String PLAYER_EXIT_GAME = " è uscito dalla partita.";
    public static String IN_GAME_PLAYERS = "Giocatori presenti: ";
    public static String OF = " di ";
    public static String NEEDED = " necessari.";
    public static String TURN = "Turno: ";
    public static String ROUND = "\tRound: ";
    public static String ACTIVE_PLAYER = "\tGiocatore attivo: ";
    public static String PLAYER_PLACED_DICE = " ha posizionato il dado ";
    public static String IN_POSITION = " in posizione (";
    public static String PLAYER_USED_TOOLCARD = " ha usato la toolcard ";
    public static String CHANGED_DICE = " ha cambiato il dado\n\n";
    public static String INTO_DICE = "\nnel dado\n\n";
    public static String POSITION = "Posizione: ";
    public static String PLAYER_REPLACED_DICE = " ha sostituio il dado\n\n";
    public static String PLACED_IN = "\nposizionato in: ";
    public static String WITH_DICE = " con il dado\n\n";
    public static String EXTRACTED_DICES_REPLACED = "I dadi estratti sono stati rilanciati, ecco i nuovi:";
    public static String PLAYER_DISCONNECTED = " si è disconesso";
    public static String PLAYER_RECONNECTED = " si è riconesso";
    public static String LOST_CONNECTION = "Connessione con il server persa";
    public static String LOGIN_FROM_ANOTHER_DEVICE = "Hai effettuato il login da un altro dispositivo";
    public static String YOU_HAVE_BEEN_DISCONNECTED = "Sei pertanto stato disconesso dalla sessione";

    public static String TIME_TO_WAIT_PLAYER_ENDED = "Tempo per attendere nuovi giocatori terminato. La partita inizierà comunque";
    public static String POINTS = " punti";

    public static String PLAYER_SKIPPED_TURN = " ha saltato il turno poichè non è attualmente connesso alla partita";
    public static String PLAYER_SKIPPED_TURN_DUE_TO_TOOLCARD = " ha saltato il turno a causa dell'utilizzo della toolcard ";

    public static String GAME_ENDED = "Partita terminata";
    public static String FINAL_TABLE = "Classifica finale:";



}
