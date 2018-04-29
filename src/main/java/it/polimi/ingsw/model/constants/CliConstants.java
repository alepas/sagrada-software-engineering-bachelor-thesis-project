package it.polimi.ingsw.model.constants;

public class CliConstants {

    public static final String YES_RESPONSE = "y";
    public static final String NO_RESPONSE = "n";
    public static final String ESCAPE_RESPONSE = "back";

    public static final String CHOOSE_LOG_TYPE =
            "Benvenuto in Sagrada. Prima di iniziare è necessario registrarsi. " +
            "Ha già un account? " + YES_RESPONSE + "/" + NO_RESPONSE;

    public static final String CHOOSE_LOG_TYPE_ERROR =
            "Comando non riconosciuto, perfavore rispondi \"" + YES_RESPONSE + "\" oppure \"" +
                    NO_RESPONSE + "\". Hai già un account? y/n";

    public static final String CREATE_USER_PHASE = "Creazione nuovo utente";
    public static final String LOGIN_PHASE = "Accedi al tuo account";

    public static final String INSERT_USERNAME = "Inserisci l'username. Digita \"" + ESCAPE_RESPONSE +
            "\" per tornare indietro";
    public static final String INSERT_PASS = "Inserisci la password. Digita \"" + ESCAPE_RESPONSE +
            "\" per tornare indietro";

    public static final String CREATE_USER_ERROR = "Impossibile creare l'utente";
    public static final String LOGIN_ERROR = "Impossibile creare l'utente";

    public static final String LOG_SUCCESS = "Sei connesso come: ";
}
