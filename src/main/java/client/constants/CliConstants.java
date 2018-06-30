package client.constants;

public class CliConstants {
    public static final int COUNTER_START_NUMBER = 0;

    public static final String YES_RESPONSE = "y";
    public static final String NO_RESPONSE = "n";
    public static final String ESCAPE_RESPONSE = "back";

    public static final String CHOOSE_LOG_TYPE =
            "Benvenuto in Sagrada. Prima di iniziare è necessario registrarsi. " +
            "Ha già un account? " + YES_RESPONSE + "/" + NO_RESPONSE + " (digita quit per uscire)";

    public static final String CHOOSE_LOG_TYPE_ERROR =
            "Comando non riconosciuto, perfavore rispondi \"" + YES_RESPONSE + "\" oppure \"" +
                    NO_RESPONSE + "\". Hai già un account? y/n";

    public static final String CREATE_USER_PHASE = "Creazione nuovo utente";
    public static final String LOGIN_PHASE = "Accedi al tuo account";

    public static final String INSERT_USERNAME = "Inserisci l'username. Digita \"" + ESCAPE_RESPONSE +
            "\" per tornare indietro";
    public static final String INSERT_PASS = "Inserisci la password.";

    public static final String CREATE_USER_ERROR = "Impossibile creare l'utente";
    public static final String LOGIN_ERROR = "Impossibile effettuare il login";

    public static final String LOG_SUCCESS = "Sei connesso come: ";

    public static final String PRESENT_MAIN_MENU = "Cosa vuoi fare? Digita:\n>>> 1) Cerca partita\n" +
            ">>> 2) Visualizza statistiche giocatore\n>>> 3) Logout";

    public static final String SELECT_NUM_PLAYERS = "Seleziona numero giocatori:";

    public static final String NUM_PLAYERS_ERROR = "Perfavore inserire un numero compreso tra 1 e 4";

    public static final int WpcSpacing = 10;
}
